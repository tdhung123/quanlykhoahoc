package com.donacuoikhoa.quanlykhoahoc.quanlydetai;

import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTai;
import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTaiService;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTai;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTaiNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTaiService;
import com.donacuoikhoa.quanlykhoahoc.donvichutri.DonViChuTri;
import com.donacuoikhoa.quanlykhoahoc.donvichutri.DonViChuTriService;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungRepository;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungService;
import com.donacuoikhoa.quanlykhoahoc.utili.UploadFilePDF;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTai;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTaiRepository;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("quanlydetai")
public class QuanLyDeTaiController {
    @Autowired
    private QuanLyDeTaiService quanLyDeTaiService;
    @Autowired
    private DeTaiService deTaiService;
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private CapDeTaiService capDeTaiService;
    @Autowired
    private DonViChuTriService donViChuTriService;

    @GetMapping("")
    public String danhSach(Model model) {
        List<QuanLyDeTai> quanLyDeTais = quanLyDeTaiService.danhSach();
        model.addAttribute("danhsachquanlydetai", quanLyDeTais);
        model.addAttribute("danhSachThanhVien", nguoiDungService.danhSach());
        return "quanlydetai/danhsach";
    }

    @GetMapping("/themmoi")
    public String themMoiDeTai(Model model) {
        if (!model.containsAttribute("detai")) {
            model.addAttribute("detai", new DeTai());
        }
        List<CapDeTai> capDeTais = capDeTaiService.danhSach();
        List<DonViChuTri> donViChuTris = donViChuTriService.danhSach();
        List<NguoiDung> nguoiDungs = nguoiDungService.danhSach();
        model.addAttribute("danhSachDonViChuTri", donViChuTris);
        model.addAttribute("danhSachCapDeTai", capDeTais);
        model.addAttribute("danhSachNguoiDung", nguoiDungs);
        return "quanlydetai/themmoi";
    }

    @Autowired
    private UploadFilePDF uploadFilePDF;
    @Autowired
    private VaiTroDeTaiRepository vaiTroDeTaiRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @PostMapping("/luu")
    public String luDeTai(@Valid @ModelAttribute("deTai") DeTai deTai, BindingResult bindingResult,
                          @RequestParam("file") MultipartFile multipartFile,
                          HttpServletRequest request,
                          RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("tenDeTai")) {
                red.addFlashAttribute("tenDeTaiError", bindingResult.getFieldError("tenDeTai").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("maSo")) {
                red.addFlashAttribute("maSoError", bindingResult.getFieldError("maSo").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("thongTinDeTai")) {
                red.addFlashAttribute("thongTinDeTaiError", bindingResult.getFieldError("thongTinDeTai").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("thoiGianBatDau")) {
                red.addFlashAttribute("thoiGianBatDauError", bindingResult.getFieldError("thoiGianBatDau").getDefaultMessage());
            }
            if (bindingResult.hasFieldErrors("thoiGianKetThuc")) {
                red.addFlashAttribute("thoiGianKetThucError", bindingResult.getFieldError("thoiGianKetThuc").getDefaultMessage());
            }

        } else {
            try {
                if (multipartFile != null && !multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    if (StringUtils.hasText(fileName)) {
                        deTai.setFileDinhKem(fileName);
                        File savedFile = uploadFilePDF.handleUploadFile(multipartFile);
                        String fileDinhKem = savedFile.getName();
                        deTai.setFileLuuTru(fileDinhKem);
                    }
                } else {
                    deTai.setFileDinhKem(null);
                    deTai.setFileLuuTru(null);
                }
                deTaiService.luu(deTai);

                String[] canBoChuTri = request.getParameterValues("truongnhom");
                String[] canBoThamGia = request.getParameterValues("thanhvien");
                VaiTroDeTai vaitroChuTri = vaiTroDeTaiRepository.findByTenVaiTro("Cán bộ chủ trì");
                VaiTroDeTai vaitroThamGia = vaiTroDeTaiRepository.findByTenVaiTro("Cán bộ tham gia");


                if (canBoChuTri != null && canBoChuTri.length > 0) {
                    for (int i = 0; i < canBoChuTri.length; i++) {
                        int nguoiDungId = Integer.parseInt(canBoChuTri[i]);
                        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
                        if (nguoiDung != null) {
                            QuanLyDeTai quanLyDeTai = new QuanLyDeTai();
                            quanLyDeTai.setDeTai(deTai);
                            quanLyDeTai.setVaiTroDeTai(vaitroChuTri);
                            quanLyDeTai.setNguoiDung(nguoiDung);
                            System.out.println(quanLyDeTai);
                            quanLyDeTaiService.quanLyDeTaiRepository.save(quanLyDeTai);
                        }
                    }
                }

                if (canBoThamGia != null && canBoThamGia.length > 0) {
                    for (int i = 0; i < canBoThamGia.length; i++) {
                        int nguoiDungId = Integer.parseInt(canBoThamGia[i]);
                        NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
                        if (nguoiDung != null) {
                            QuanLyDeTai quanLyDeTai = new QuanLyDeTai();
                            quanLyDeTai.setDeTai(deTai);
                            quanLyDeTai.setVaiTroDeTai(vaitroThamGia);
                            quanLyDeTai.setNguoiDung(nguoiDung);
                            quanLyDeTaiService.quanLyDeTaiRepository.save(quanLyDeTai);
                        }
                    }
                }
                red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
                return "redirect:/quanlydetai";
            } catch (DuplicateKeyException e) {
                red.addFlashAttribute("loi", e.getMessage());
            } catch (Exception e) {
                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
            }
        }
        red.addFlashAttribute("detai", deTai);
        return "redirect:/quanlydetai/themmoi";
    }

    @GetMapping("/sua/{id}")
    public String suaDeTai(@PathVariable("id") Integer id, Model model) {
        try {
            DeTai deTai = deTaiService.get(id);
            List<CapDeTai> capDeTais = capDeTaiService.danhSach();
            List<DonViChuTri> donViChuTris = donViChuTriService.danhSach();
            List<NguoiDung> nguoiDungs = nguoiDungService.danhSach();
            model.addAttribute("detai", deTai);
            model.addAttribute("danhSachDonViChuTri", donViChuTris);
            model.addAttribute("danhSachCapDeTai", capDeTais);
            model.addAttribute("danhSachNguoiDung", nguoiDungs);
            model.addAttribute("danhSachQuanLyDeTai", quanLyDeTaiService.quanLyDeTaiRepository.findAll());

            return "quanlydetai/themmoi";
        } catch (DeTaiNotFoundException e) {
            return "redirect:/quanlydetai"; // Handle if DeTai not found
        }
    }



}