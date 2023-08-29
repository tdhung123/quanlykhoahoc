package com.donacuoikhoa.quanlykhoahoc.capdetai;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/capdetai")
public class CapDeTaiController {
    @Autowired CapDeTaiService capDeTaiService;
    @GetMapping("")
    public String danhSach(Model model) {
        List<CapDeTai> danhSachCapDeTai = capDeTaiService.danhSach();
        model.addAttribute("danhsachcapdetai", danhSachCapDeTai);
        return "capdetai/danhsach";
    }
    @GetMapping("/themmoi")
    public String themMoi(Model model) {
        if (!model.containsAttribute("capdetai")) {
            model.addAttribute("capdetai", new CapDeTai());
        }
        return "capdetai/themmoi";
    }
    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("capDeTai") CapDeTai capDeTai, BindingResult bindingResult, RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("tenCapDeTai")){
                red.addFlashAttribute("tenCapDeTaiError", bindingResult.getFieldError("tenCapDeTai").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("tongSoGio")){
                red.addFlashAttribute("tongSoGioError", bindingResult.getFieldError("tongSoGio").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("xDiemTruongNhom")){
                red.addFlashAttribute("xDiemTruongNhomError", bindingResult.getFieldError("xDiemTruongNhom").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("yDiemTruongNhom")){
                red.addFlashAttribute("yDiemTruongNhomError", bindingResult.getFieldError("yDiemTruongNhom").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("xDiemThanhVien")){
                red.addFlashAttribute("xDiemThanhVienError", bindingResult.getFieldError("xDiemThanhVien").getDefaultMessage());
            }
        } else {
            try {
                capDeTaiService.luu(capDeTai);
                red.addFlashAttribute("message1", "Thêm mới khoa thành công");
                return "redirect:/capdetai";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("capdetai", capDeTai);
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");

            }
        }
        red.addFlashAttribute("capdetai", capDeTai);
        return "redirect:/capdetai/themmoi";
    }
    @GetMapping("/sua/{id}")
    public String sua(@PathVariable("id") Integer id, Model model, RedirectAttributes red) {
        try {
            CapDeTai capDeTai = capDeTaiService.get(id);
            model.addAttribute("capdetai", capDeTai);
            model.addAttribute("page", "Chỉnh sửa thông tin có (Id: " + id + ")");
        } catch (CapDeTaiNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
            return "redirect:/capdetai";
        }
        return "capdetai/themmoi";
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            capDeTaiService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (CapDeTaiNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/capdetai";
    }


}

