//package com.donacuoikhoa.quanlykhoahoc.bomon;
//
//import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
//import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@Controller
//@RequestMapping("/bomon")
//public class BoMonController {
//
//    @Autowired
//    private BoMonService boMonService;
//    @Autowired
//    private KhoaService khoaService;
//
//    @GetMapping("")
//    public String danhSachBoMon(Model model) {
//        List<BoMon> danhSachBoMon = boMonService.danhSach(); // Fetch BoMon entities along with their associated Khoa entities
//        model.addAttribute("danhSachBoMon", danhSachBoMon);
//        return "bomon/danhsach";
//    }
//
//    @GetMapping("/themmoi")
//    public String themMoiBoMon(Model model) {
//        if (!model.containsAttribute("bomon")) {
//            List<Khoa> khoas = khoaService.danhSach();
//            model.addAttribute("bomon", new BoMon());
//            model.addAttribute("khoas", khoas);
//        }
//        return "bomon/themmoi";
//    }
//
//    @PostMapping("/luu")
//    public String luu(@Valid @ModelAttribute("bomon") BoMon boMon, BindingResult bindingResult, RedirectAttributes red) {
//        List<Khoa> khoas = khoaService.danhSach();
//        System.out.println(khoas.toString());
//        if (bindingResult.hasErrors()) {
//
//            red.addFlashAttribute("bomon", boMon);
//            red.addFlashAttribute("errors", bindingResult.getAllErrors());
//            red.addFlashAttribute("khoas", khoas);
//            return "redirect:/bomon/themmoi";
//        } else {
//            try {
//                boMonService.luu(boMon);
//                red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
//                return "redirect:/bomon";
//            } catch (DuplicateKeyException e) {
//                red.addFlashAttribute("bomon", boMon);
//                red.addFlashAttribute("loi", e.getMessage());
//                red.addFlashAttribute("khoas", khoas);
//                return "redirect:/bomon/themmoi";
//            } catch (Exception e) {
//                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
//                red.addFlashAttribute("bomon", boMon);
//                red.addFlashAttribute("khoas", khoas);
//                return "bomon/themmoi";
//            }
//        }
//    }
//}

package com.donacuoikhoa.quanlykhoahoc.bomon;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaService;
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
@RequestMapping("/bomon")
public class BoMonController {

    @Autowired
    private BoMonService boMonService;

    @Autowired
    private KhoaService khoaService;

    @GetMapping("")
    public String danhSachBoMon(Model model) {
        List<BoMon> danhSachBoMon = boMonService.danhSach(); // Fetch BoMon entities along with their associated Khoa entities
        model.addAttribute("danhSachBoMon", danhSachBoMon);
        return "bomon/danhsach";
    }

    @GetMapping("/themmoi")
    public String themMoiBoMon(Model model) {
        if (!model.containsAttribute("bomon")) {
            model.addAttribute("bomon", new BoMon());
        }
        model.addAttribute("khoas", khoaService.danhSach());
        return "bomon/themmoi";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("bomon") BoMon boMon, BindingResult bindingResult, RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            red.addFlashAttribute("errors", bindingResult.getAllErrors());
        } else {
            try {
                boMonService.luu(boMon);
                red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
                return "redirect:/bomon";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
            }
        }
        red.addFlashAttribute("bomon", boMon);
        return "redirect:/bomon/themmoi";
    }
    @GetMapping("/sua/{id}")
    public String sua(@PathVariable("id") Integer id, Model model , RedirectAttributes red){
        try {

            BoMon boMon = boMonService.get(id);
            model.addAttribute("bomon", boMon);
            model.addAttribute("page", "Chỉnh sửa thông tin có (Id:"+ id +")");

            model.addAttribute("khoa", khoaService.danhSach());
            return "bomon/sua";
        } catch (BoMonNotFoundException e) {
            red.addFlashAttribute("message",e.getMessage());
            return "redirect:/bomon";
        }

    }


@PostMapping("/capnhat")
public String capNhat(@Valid @ModelAttribute("bomon") BoMon boMon, BindingResult bindingResult, RedirectAttributes red) {

    if (bindingResult.hasErrors()) {
        red.addFlashAttribute("errors", bindingResult.getAllErrors());

    } else {
        try {
            boMonService.luu(boMon);
            red.addFlashAttribute("message1", "Sửa thông tin thành công");
            return "redirect:/bomon";
        } catch (DuplicateKeyException e) {
            red.addFlashAttribute("loi", e.getMessage());
        } catch (Exception e) {
            red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
        }
    }
    red.addFlashAttribute("bomon", boMon);
    return "redirect:/bomon/themmoi";
}
    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            boMonService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (BoMonNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/bomon";
    }

}
