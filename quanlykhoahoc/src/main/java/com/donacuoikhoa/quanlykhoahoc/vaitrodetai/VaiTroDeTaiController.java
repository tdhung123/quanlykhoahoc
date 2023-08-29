package com.donacuoikhoa.quanlykhoahoc.vaitrodetai;

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
@RequestMapping("/vaitrodetai")
public class VaiTroDeTaiController {
    @Autowired VaiTroDeTaiService vaiTroDeTaiService;
    @GetMapping("")
    public String danhSachKhoa(Model model) {
        List<VaiTroDeTai> danhSachVaiTro = vaiTroDeTaiService.danhSach();
        model.addAttribute("danhsachvaitro", danhSachVaiTro);
        return "vaitrodetai/danhsach";
    }

    @GetMapping("/themmoi")
    public String themMoiKhoa(Model model) {
        if (!model.containsAttribute("vaitrodetai")) {
            model.addAttribute("vaitrodetai", new VaiTroDeTai());
        }
        return "vaitrodetai/themmoi";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("vaiTroDeTai") VaiTroDeTai vaiTroDeTai, BindingResult bindingResult, RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            red.addFlashAttribute("vaitrodetai", vaiTroDeTai);
            red.addFlashAttribute("errors", bindingResult.getAllErrors());
        } else {
            try {
                vaiTroDeTaiService.luu(vaiTroDeTai);
                red.addFlashAttribute("message1", "Thêm mới vai trò thành công");
                return "redirect:/vaitrodetai";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("vaitrodetai", vaiTroDeTai);
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("vaitrodetai", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
                red.addFlashAttribute("khoa", vaiTroDeTai);
            }
        }
        return "redirect:/vaitrodetai/themmoi";
    }

    @GetMapping("/sua/{id}")
    public String sua(@PathVariable("id") Integer id, Model model, RedirectAttributes red) {
        try {
            VaiTroDeTai vaiTroDeTai = vaiTroDeTaiService.get(id);
            model.addAttribute("vaitrodetai", vaiTroDeTai);
            model.addAttribute("page", "Chỉnh sửa thông tin có (Id: " + id + ")");
        } catch (VaiTroDeTaiNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
            return "redirect:/vaitrodetai";
        }
        return "vaitrodetai/themmoi";
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            vaiTroDeTaiService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (VaiTroDeTaiNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/vaitrodetai";
    }

}


