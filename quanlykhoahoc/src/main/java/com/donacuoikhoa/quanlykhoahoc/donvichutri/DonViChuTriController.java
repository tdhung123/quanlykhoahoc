package com.donacuoikhoa.quanlykhoahoc.donvichutri;

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
@RequestMapping("/donvichutri")
public class DonViChuTriController {
    @Autowired private DonViChuTriService donViChuTriService;

    @GetMapping("")
    public String danhSachKhoa(Model model) {
        List<DonViChuTri> danhSachDonVi = donViChuTriService.danhSach();
        model.addAttribute("danhsachdonvichutri", danhSachDonVi);
        return "donvichutri/danhsach";
    }

    @GetMapping("/themmoi")
    public String themMoiKhoa(Model model) {
        if (!model.containsAttribute("donvichutri")) {
            model.addAttribute("donvichutri", new DonViChuTri());
        }
        return "donvichutri/themmoi";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("donViChuTri") DonViChuTri donViChuTri, BindingResult bindingResult, RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            red.addFlashAttribute("donvichutri", donViChuTri);
            red.addFlashAttribute("errors", bindingResult.getAllErrors());
        } else {
            try {
                donViChuTriService.luu(donViChuTri);
                red.addFlashAttribute("message1", "Thêm mới khoa thành công");
                return "redirect:/donvichutri";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("donvichutri", donViChuTri);
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
                red.addFlashAttribute("donvichutri", donViChuTri);
            }
        }
        return "redirect:/donvichutri/themmoi";
    }

    @GetMapping("/sua/{id}")
    public String sua(@PathVariable("id") Integer id, Model model, RedirectAttributes red) {
        try {
            DonViChuTri donViChuTri = donViChuTriService.get(id);
            model.addAttribute("donvichutri", donViChuTri);
            model.addAttribute("page", "Chỉnh sửa thông tin có (Id: " + id + ")");
        } catch (DonViChuTriNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
            return "redirect:/donvichutri";
        }
        return "donvichutri/themmoi";
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            donViChuTriService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (DonViChuTriNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/donvichutri";
    }


}
