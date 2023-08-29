package com.donacuoikhoa.quanlykhoahoc.utili;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTai;

public class ThongKeExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<QuanLyDeTai> quanLyDeTais;

    public ThongKeExcelExporter(List<QuanLyDeTai> quanlydetai) {
        this.quanLyDeTais = quanlydetai;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Thống kê thông tin điểm");
    }

    private void thongTin() {
        int rowCount = 0; // Start from the second row (A2)
        XSSFFont font = workbook.createFont();
        font.setFontHeight(16);
        CellStyle style = workbook.createCellStyle();
        style.setFont(font);

        for (QuanLyDeTai quanLyDeTai : quanLyDeTais) {
            Row row = sheet.createRow(rowCount);
            Cell cell = row.createCell(1);
            cell.setCellValue("Thống kê điểm: " + quanLyDeTai.getNguoiDung().getHoVaTen());
            cell.setCellStyle(style);
            Row row1 = sheet.createRow(1);
            Cell cell1 = row1.createCell(1);
            cell1.setCellValue("Mã : " + quanLyDeTai.getNguoiDung().getMaNguoiDung());
            cell1.setCellStyle(style);

        }
    }

    private void writeHeaderRow() {
        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        XSSFFont font = workbook.createFont();
        CellStyle style = workbook.createCellStyle();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        cell.setCellValue("Tên đề tài");
        cell.setCellStyle(style);
        cell = row.createCell(1);
        cell.setCellValue("Cấp đề tài");
        cell.setCellStyle(style);
        cell = row.createCell(2);
        cell.setCellValue("Vai Trò");
        cell.setCellStyle(style);
        cell = row.createCell(3);
        cell.setCellValue("Điểm");
        cell.setCellStyle(style);
    }

    private void writeDataRow() {
        int rowCount = 3;
        for (QuanLyDeTai quanLyDeTai : quanLyDeTais) {
            Row row = sheet.createRow(rowCount);
            Cell cell = row.createCell(0);
            cell.setCellValue(quanLyDeTai.getDeTai().getTenDeTai());
            cell = row.createCell(1);
            cell.setCellValue(quanLyDeTai.getDeTai().getCapDeTai().getTenCapDeTai());

            cell = row.createCell(2);
            cell.setCellValue(quanLyDeTai.getVaiTroDeTai().getTenVaiTro());
            cell = row.createCell(3);
            cell.setCellValue(quanLyDeTai.getDiemThanhVien());
            rowCount++;
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        thongTin();
        writeHeaderRow();
        writeDataRow();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
