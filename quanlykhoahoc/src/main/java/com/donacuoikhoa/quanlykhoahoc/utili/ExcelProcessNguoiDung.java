package com.donacuoikhoa.quanlykhoahoc.utili;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.donacuoikhoa.quanlykhoahoc.bomon.BoMon;
import com.donacuoikhoa.quanlykhoahoc.bomon.BoMonRepository;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.phanquyen.VaiTro;
import com.donacuoikhoa.quanlykhoahoc.phanquyen.VaiTroRepository;
import java.util.*;

@Service
public class ExcelProcessNguoiDung {
    @Autowired
    private BoMonRepository boMonRepository;
    @Autowired
    private VaiTroRepository vaiTroRepository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<NguoiDung> processNguoiDungExcelFile(MultipartFile file) throws IOException {
        List<NguoiDung> nguoiDungs = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null || sheet.getPhysicalNumberOfRows() <= 1) {
                return nguoiDungs;
            }

            // Skip the header row since it's already read in the excelToListOfLists method
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                NguoiDung nguoiDung = new NguoiDung();

                String maNguoiDung = getStringCellValue(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                nguoiDung.setMaNguoiDung(maNguoiDung);
                String hoVaTen = getStringCellValue(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                nguoiDung.setHoVaTen(hoVaTen);

                String ngaySinhStr = getStringCellValue(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                Date ngaySinhUtil = parseDate(ngaySinhStr); 
                if (ngaySinhUtil != null) {
                    java.sql.Date ngaySinhSql = new java.sql.Date(ngaySinhUtil.getTime()); // Convert to java.sql.Date
                    nguoiDung.setNgaySinh(ngaySinhSql);
                }

                String donViCongTac = getStringCellValue(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                nguoiDung.setDonViCongTac(donViCongTac);

                String tenBoMon = getStringCellValue(row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                BoMon boMon = boMonRepository.findByTenBoMon(tenBoMon);
                nguoiDung.setBoMon(boMon);

                String email = getStringCellValue(row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                nguoiDung.setEmail(email);
                String matKhau = getStringCellValue(row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String encodePasswword = this.passwordEncoder.encode(matKhau);
                nguoiDung.setMatKhau(encodePasswword);
                String soDienThoai = getStringCellValue(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                nguoiDung.setSoDienThoai(soDienThoai);

                String vaitros = getStringCellValue(row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
                String[] vaiTroNames = vaitros.split(",");
                Set<VaiTro> vaiTroSet = new HashSet<>();

                for (String vaiTroName : vaiTroNames) {
                    VaiTro vaiTro = vaiTroRepository.findByTen(vaiTroName.trim());
                    if (vaiTro != null) {
                        vaiTroSet.add(vaiTro);
                    }
                }

                nguoiDung.setVaiTros(vaiTroSet);

                nguoiDungs.add(nguoiDung);
            }
        }

        return nguoiDungs;
    }

    private String getStringCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula().trim();
            case BLANK:
            default:
                return "";
        }
    }

    private Date parseDate(String dateStr) {
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
