package com.donacuoikhoa.quanlykhoahoc.nguoidung;

import com.donacuoikhoa.quanlykhoahoc.bomon.BoMon;
import com.donacuoikhoa.quanlykhoahoc.bomon.BoMonNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.bomon.BoMonService;
import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.phanquyen.VaiTro;
import com.donacuoikhoa.quanlykhoahoc.phanquyen.VaiTroRepository;
import com.donacuoikhoa.quanlykhoahoc.utili.ExcelProcessNguoiDung;
import com.donacuoikhoa.quanlykhoahoc.utili.StringProcess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/nguoidung")
public class NguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private BoMonService boMonService;

    @Autowired
    private VaiTroRepository vaiTroRepository;
    @Autowired
    private StringProcess stringProcess;

    @GetMapping("")
    public String danhSachNguoiDung(Model model) {

        List<NguoiDung> nguoiDungs = nguoiDungService.danhSach();
        // Collections.reverse(nguoiDungs);
        model.addAttribute("danhsachnguoidung", nguoiDungs);

        return "nguoidung/danhsach";
    }

    // @GetMapping("/themmoi")
    // public String themMoiNguoiDung(Model model) {
    //     if (!model.containsAttribute("nguoidung")) {
    //         NguoiDung newNguoiDung = new NguoiDung();
    //         NguoiDung latestNguoiDung = nguoiDungRepository.findTopByOrderByMaNguoiDungDesc();
    //         String newMaNguoiDung = stringProcess.autoGenerateCode(latestNguoiDung.getMaNguoiDung());
    //         newNguoiDung.setMaNguoiDung(newMaNguoiDung);
    //         model.addAttribute("nguoidung", newNguoiDung);
    //     }
    //     List<Khoa> danhSachKhoa = nguoiDungService.danhSachKhoa();
    //     List<VaiTro> vaiTros = vaiTroRepository.findAll();
    //     model.addAttribute("vaitro", vaiTros);
    //     model.addAttribute("danhSachKhoa", danhSachKhoa);
    //     model.addAttribute("danhSachBoMon", boMonService.danhSach());
    //     return "nguoidung/themmoi";
    // }
    @GetMapping("/themmoi")
    public String themMoiNguoiDung(Model model) {
        if (!model.containsAttribute("nguoidung")) {
            NguoiDung newNguoiDung = new NguoiDung();
            NguoiDung latestNguoiDung = nguoiDungRepository.findTopByOrderByMaNguoiDungDesc();
            String latestMaNguoiDung = latestNguoiDung != null ? latestNguoiDung.getMaNguoiDung() : "";
            String newMaNguoiDung = stringProcess.autoGenerateCode(latestMaNguoiDung);
            newNguoiDung.setMaNguoiDung(newMaNguoiDung);
            model.addAttribute("nguoidung", newNguoiDung);
        }
        
        List<Khoa> danhSachKhoa = nguoiDungService.danhSachKhoa();
        List<VaiTro> vaiTros = vaiTroRepository.findAll();
        model.addAttribute("vaitro", vaiTros);
        model.addAttribute("danhSachKhoa", danhSachKhoa);
        model.addAttribute("danhSachBoMon", boMonService.danhSach());
        
        return "nguoidung/themmoi";
    }
    
    @GetMapping("/bomon/{khoaId}")
    @ResponseBody
    public List<BoMon> getBoMonByKhoa(@PathVariable Integer khoaId) {
        System.out.println(nguoiDungService.danhSachBoMonByKhoa(khoaId));
        return nguoiDungService.danhSachBoMonByKhoa(khoaId);
    }

    // ---------------------------
    @PostMapping("/luu")
    public String luuNguoiDung(@Valid @ModelAttribute("nguoidung") NguoiDung nguoiDung, BindingResult bindingResult,
            RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("maNguoiDung")) {
                red.addFlashAttribute("maNguoiDungError",
                        bindingResult.getFieldError("maNguoiDung").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("hoVaTen")) {
                red.addFlashAttribute("hoVaTenError", bindingResult.getFieldError("hoVaTen").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("email")) {
                red.addFlashAttribute("emailError", bindingResult.getFieldError("email").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("matKhau")) {
                red.addFlashAttribute("matKhauError", bindingResult.getFieldError("matKhau").getDefaultMessage());
            }
        } else {
            try {
                nguoiDungService.luu(nguoiDung);
                red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
                return "redirect:/nguoidung";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
            }
        }
        red.addFlashAttribute("nguoidung", nguoiDung);
        return "redirect:/nguoidung/themmoi";
    }

    @GetMapping("sua/{id}")
    public String sua(@PathVariable("id") Integer id, Model model, RedirectAttributes red) {
        try {

            NguoiDung nguoiDung = nguoiDungService.get(id);
            List<VaiTro> vaiTros = vaiTroRepository.findAll();
            model.addAttribute("vaitro", vaiTros);
            List<Khoa> danhSachKhoa = nguoiDungService.danhSachKhoa();
            model.addAttribute("nguoidung", nguoiDung);
            model.addAttribute("danhSachKhoa", danhSachKhoa);
            model.addAttribute("page", "Chỉnh sửa thông tin có (Id:" + id + ")");

            return "nguoidung/themmoi";
        } catch (NguoiDungNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
            return "redirect:/nguoidung";
        }

    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            nguoiDungService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (NguoiDungNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/nguoidung";
    }

    @GetMapping("/file")
    public String file() {
        return "nguoidung/upfile"; // Return the upload page template
    }

    @Autowired
    private ExcelProcessNguoiDung excelProcess;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Value("${file.upload.path}")
    private String fileUploadPath;

    @PostMapping("/import")

    public String importExcelData(@RequestParam("file") MultipartFile file, Model model, RedirectAttributes red) {
        if (file.isEmpty()) {
            red.addFlashAttribute("message", "Vui lòng chọn một file Excel để import.");
            return "redirect:/nguoidung";
        }

        try {
            List<NguoiDung> nguoiDungs = excelProcess.processNguoiDungExcelFile(file);
            nguoiDungRepository.saveAll(nguoiDungs);
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(fileUploadPath, fileName);
            Files.copy(file.getInputStream(), filePath);
            red.addFlashAttribute("message1", "Import thành công " + nguoiDungs.size() + " người dùng.");
        } catch (IOException e) {
            red.addFlashAttribute("message", "Lỗi khi import file: " + e.getMessage());
        }

        return "redirect:/nguoidung";
    }
}
