package com.donacuoikhoa.quanlykhoahoc;

import com.donacuoikhoa.quanlykhoahoc.detai.DeTai;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTaiService;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TrangChuController {
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private DeTaiService deTaiService;

    @GetMapping("/")
    public String trangChu() {
        return "trangchu";
    }

    @GetMapping("403")
    public String orr403() {
        return "403";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        List<NguoiDung> nguoiDungs = nguoiDungService.danhSach();
        List<DeTai> deTais = deTaiService.danhSach();
        int soLuongNguoi = nguoiDungs.size();
        int soLuongDeTai = deTais.size();
        model.addAttribute("soLuongNguoi", soLuongNguoi);
        model.addAttribute("soLuongDeTai", soLuongDeTai);
        return "admin/index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/trangchu")
    public String trangChu1() {
        return "trangchu";
    }
}
