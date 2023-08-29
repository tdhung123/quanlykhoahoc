//package com.donacuoikhoa.quanlykhoahoc.khoa;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DuplicateKeyException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import javax.validation.Valid;
//import java.util.List;
//import java.util.Queue;
//import java.util.Stack;
//
//@Controller
//@RequestMapping("/khoa")
//public class KhoaController {
//    @Autowired KhoaService khoaService;
//
//    @GetMapping("")
//    public String danhSachKhoa(Model model) {
//        List<Khoa> danhSachKhoa = khoaService.danhSach();
//        model.addAttribute("danhsachkhoa", danhSachKhoa);
//        return "khoa/danhsach";
//    }
//    @GetMapping("/themmoi")
//    public String themMoiKhoa(Model model)
//    {
//        if (!model.containsAttribute("khoa")) {
//            model.addAttribute("khoa", new Khoa());
//        }
//        return "khoa/themmoi";
//    }
//
//    @PostMapping("/luu")
//    public String luu(@Valid @ModelAttribute("khoa") Khoa khoa, BindingResult bindingResult, RedirectAttributes red) {
//        if (bindingResult.hasErrors()) {
//            red.addFlashAttribute("khoa", khoa);
//            red.addFlashAttribute("errors", bindingResult.getAllErrors());
//            return "redirect:/khoa/themmoi";
//
//        } else {
//            try {
//                khoaService.luu(khoa);
//                red.addFlashAttribute("message1", "Thêm mới khoa thành công");
//                return "redirect:/khoa";
//            } catch (DuplicateKeyException e) {
//                red.addFlashAttribute("khoa", khoa);
//                red.addFlashAttribute("loi",e.getMessage());
//                return "redirect:/khoa/themmoi";
//            } catch (Exception e) {
//                red.addFlashAttribute("loi","Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
//                red.addFlashAttribute("khoa", khoa);
//                return "khoa/themmoi";
//            }
//        }
//    }
//
//    @GetMapping("/sua/{id}")
//    public  String sua(@PathVariable("id")Integer id,Model model,RedirectAttributes red){
//        try {
//            Khoa khoa = khoaService.get(id);
//            model.addAttribute("khoa", khoa);
//            model.addAttribute("page", "Chỉnh sửa thông tin có (Id: "+ id +")");
//            return "khoa/themmoi";
//        } catch (KhoaNotFoundException e) {
//            red.addFlashAttribute("message",e.getMessage());
//            return "redirect:/khoa";
//        }
//    }
//    @GetMapping("/xoa/{id}")
//    public String xoa(@PathVariable("id") Integer id,RedirectAttributes red){
//        try {
//            khoaService.delete(id);
//            red.addFlashAttribute("message1","Xoá id:"+id+"thành công");
//        } catch (KhoaNotFoundException e) {
//            red.addFlashAttribute("message",e.getMessage());
//        }
//        return "redirect:/khoa";
//
//    }
//    @GetMapping("/deleteall")
//    public String xoaTatCa(){
//        khoaService.deleteAll();
//        return "redirect:/khoa";
//    }
//
//}
package com.donacuoikhoa.quanlykhoahoc.khoa;

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
@RequestMapping("/khoa")
public class KhoaController {

    @Autowired
    private KhoaService khoaService;

    @GetMapping("")
    public String danhSachKhoa(Model model) {
        List<Khoa> danhSachKhoa = khoaService.danhSach();
        model.addAttribute("danhsachkhoa", danhSachKhoa);
        return "khoa/danhsach";
    }

    @GetMapping("/themmoi")
    public String themMoiKhoa(Model model) {
        if (!model.containsAttribute("khoa")) {
            model.addAttribute("khoa", new Khoa());
        }

        return "khoa/themmoi";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("khoa") Khoa khoa, BindingResult bindingResult, RedirectAttributes red) {
            if (bindingResult.hasErrors()) {
            red.addFlashAttribute("khoa", khoa);
            red.addFlashAttribute("errors", bindingResult.getAllErrors());
        } else {
            try {
                khoaService.luu(khoa);
                red.addFlashAttribute("message1", "Thêm mới khoa thành công");
                return "redirect:/khoa";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("khoa", khoa);
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
                red.addFlashAttribute("khoa", khoa);
            }
        }
        return "redirect:/khoa/themmoi";
    }

    @GetMapping("/sua/{id}")
    public String sua(@PathVariable("id") Integer id, Model model, RedirectAttributes red) {
        try {
            Khoa khoa = khoaService.get(id);
            model.addAttribute("khoa", khoa);
            model.addAttribute("page", "Chỉnh sửa thông tin có (Id: " + id + ")");
        } catch (KhoaNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
            return "redirect:/khoa";
        }
        return "khoa/themmoi";
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            khoaService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (KhoaNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/khoa";
    }

    @GetMapping("/deleteall")
    public String xoaTatCa() {
        khoaService.deleteAll();
        return "redirect:/khoa";
    }
}
