package com.donacuoikhoa.quanlykhoahoc.thongke;

import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTai;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungRepository;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTai;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTaiRepository;
import com.donacuoikhoa.quanlykhoahoc.utili.ThongKeExcelExporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/thongke")
public class ThongKeController {

    @Autowired
    private QuanLyDeTaiRepository quanLyDeTaiRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @GetMapping("")
    public String thongKe(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        NguoiDung nguoiDung = nguoiDungRepository.getNguoiDungByEmail(email);
        if (nguoiDung != null) {
            List<QuanLyDeTai> quanLyDeTaiList = quanLyDeTaiRepository.findByNguoiDungId(nguoiDung.getId());
            Collections.reverse(quanLyDeTaiList);
            model.addAttribute("nguoiDung", nguoiDung);
            model.addAttribute("quanLyDeTaiList", quanLyDeTaiList);
            System.out.println(quanLyDeTaiList);
            return "thongke/thongkediem"; // Trả về tên template (view) để render trang
        } else {
            return "redirect:/dangnhap"; // Chuyển hướng đến trang đăng nhập nếu không tìm thấy người dùng
        }
    }

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String fileName = "thongke_" + currentDateTime + ".xlsx";
        String headerValue = "attachment; filename=" + fileName;

        response.setHeader(headerKey, headerValue);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        NguoiDung nguoiDung = nguoiDungRepository.getNguoiDungByEmail(email);

        if (nguoiDung != null) {
            List<QuanLyDeTai> quanLyDeTaiList = quanLyDeTaiRepository.findByNguoiDungId(nguoiDung.getId());
            Collections.reverse(quanLyDeTaiList);

            ThongKeExcelExporter excelExporter = new ThongKeExcelExporter(quanLyDeTaiList);
            excelExporter.export(response);
        }
    }
}
