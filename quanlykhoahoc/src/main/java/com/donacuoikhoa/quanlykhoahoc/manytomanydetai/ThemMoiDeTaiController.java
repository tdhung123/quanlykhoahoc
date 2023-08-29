package com.donacuoikhoa.quanlykhoahoc.manytomanydetai;

import com.donacuoikhoa.quanlykhoahoc.bomon.BoMonNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTai;
import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTaiService;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTai;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTaiNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTaiRepository;
import com.donacuoikhoa.quanlykhoahoc.detai.DeTaiService;
import com.donacuoikhoa.quanlykhoahoc.donvichutri.DonViChuTri;
import com.donacuoikhoa.quanlykhoahoc.donvichutri.DonViChuTriService;
import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungRepository;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungService;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTai;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTaiRepository;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTaiService;
import com.donacuoikhoa.quanlykhoahoc.utili.UploadFilePDF;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTai;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTaiRepository;
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
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/themmoidetai")
public class ThemMoiDeTaiController {
    @Autowired
    private CapDeTaiService capDeTaiService;
    @Autowired
    private DonViChuTriService donViChuTriService;
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private DeTaiService deTaiService;
    @Autowired
    private UploadFilePDF uploadFilePDF;
    @Autowired
    private VaiTroDeTaiRepository vaiTroDeTaiRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @GetMapping("")
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
        return "themmoidetai/themmoi";
    }

    @PostMapping("/luu")
    public String luDeTai(@Valid @ModelAttribute("detai") DeTai deTai, BindingResult bindingResult,
            @RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request,
            RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors()
                    .forEach(error -> red.addFlashAttribute(error.getField() + "Error", error.getDefaultMessage()));
            return "redirect:/themmoidetai";
        }

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
            int dem = 0;
            float tonghio = 0;
            float xdiemnhotruong = 0;
            float ydiemnhotruong = 0;
            float xdiemthanhvien = 0;
            for (int i = 0; i < canBoChuTri.length + canBoThamGia.length; i++) {
                dem += 1;
            }
            List<CapDeTai> capDeTais = capDeTaiService.danhSach();
            for (CapDeTai c : capDeTais) {
                xdiemnhotruong = c.getxDiemTruongNhom();
                ydiemnhotruong = c.getyDiemTruongNhom();
                xdiemthanhvien = c.getxDiemThanhVien();
                tonghio = c.getTongSoGio();
            }
            // Điểm từng thành viên
            float diemtungnhomtruong = tonghio * (xdiemnhotruong + ydiemnhotruong / dem);
            float diemtungthanhvien = tonghio * (xdiemthanhvien / dem);

            if (canBoChuTri != null && canBoThamGia != null) {
                for (String chuTriId : canBoChuTri) {
                    if (Arrays.asList(canBoThamGia).contains(chuTriId)) {
                        red.addFlashAttribute("loi",
                                "Tên cán bộ không được trùng nhau giữa Cán bộ chủ trì và Cán bộ tham gia.");
                        red.addFlashAttribute("detai", deTai);
                        return "redirect:/themmoidetai";
                    }
                }
            }

            if (canBoChuTri == null || canBoChuTri.length == 0) {
                red.addFlashAttribute("loi", "Bạn cần phải chọn ít nhất một cán bộ chủ trì.");
                red.addFlashAttribute("detai", deTai);
                return "redirect:/themmoidetai";
            }

            for (String chuTriId : canBoChuTri) {
                int nguoiDungId = Integer.parseInt(chuTriId);
                NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
                if (nguoiDung != null) {
                    deTai.addQuanLyDeTai(new QuanLyDeTai(deTai, nguoiDung, vaitroChuTri, diemtungnhomtruong));
                }
            }

            if (canBoThamGia != null) {
                for (String thamGiaId : canBoThamGia) {
                    int nguoiDungId = Integer.parseInt(thamGiaId);
                    NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
                    if (nguoiDung != null) {
                        deTai.addQuanLyDeTai(new QuanLyDeTai(deTai, nguoiDung, vaitroThamGia, diemtungthanhvien));
                    }
                }
            }
            deTaiService.luu(deTai);
            red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
            return "redirect:/detai";
        } catch (DuplicateKeyException e) {
            red.addFlashAttribute("loi", e.getMessage());
        } catch (Exception e) {
            red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin");
        }

        red.addFlashAttribute("detai", deTai);
        return "redirect:/themmoidetai";
    }

    @GetMapping("/xoa/{id}")
    public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
        try {
            deTaiService.delete(id);
            red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
        } catch (DeTaiNotFoundException e) {
            red.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/detai";
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

            return "themmoidetai/update";
        } catch (DeTaiNotFoundException e) {
            return "redirect:/detai"; // Handle if DeTai not found
        }
    }

    @Autowired
    QuanLyDeTaiRepository quanLyDeTaiRepository;

    @PostMapping("/capnhat")
    public String capNhat(@Valid @ModelAttribute("detai") DeTai deTai, BindingResult bindingResult,
            @RequestParam("file") MultipartFile multipartFile,
            HttpServletRequest request,
            RedirectAttributes red) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors()
                    .forEach(error -> red.addFlashAttribute(error.getField() + "Error", error.getDefaultMessage()));
            return "redirect:/themmoidetai";
        }

        try {
            deleteQuanLyDeTaiByDeTai(deTai);
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
            String[] quanLyDeTaiID = request.getParameterValues("quanlyid");
            String[] canBoChuTri = request.getParameterValues("truongnhom");
            String[] canBoThamGia = request.getParameterValues("thanhvien");
            VaiTroDeTai vaitroChuTri = vaiTroDeTaiRepository.findByTenVaiTro("Cán bộ chủ trì");
            VaiTroDeTai vaitroThamGia = vaiTroDeTaiRepository.findByTenVaiTro("Cán bộ tham gia");
            deTai.getQuanLyDeTai().clear();
            int dem = 0;
            float tonghio = 0;
            float xdiemnhotruong = 0;
            float ydiemnhotruong = 0;
            float xdiemthanhvien = 0;
            for (int i = 0; i < canBoChuTri.length + canBoThamGia.length; i++) {
                dem += 1;
            }
            List<CapDeTai> capDeTais = capDeTaiService.danhSach();
            for (CapDeTai c : capDeTais) {
                xdiemnhotruong = c.getxDiemTruongNhom();
                ydiemnhotruong = c.getyDiemTruongNhom();
                xdiemthanhvien = c.getxDiemThanhVien();
                tonghio = c.getTongSoGio();
            }
            // Điểm từng thành viên
            float diemtungnhomtruong = tonghio * (xdiemnhotruong + ydiemnhotruong / dem);
            float diemtungthanhvien = tonghio * (xdiemthanhvien / dem);
            saveOrUpdateQuanLyDeTai(canBoChuTri, quanLyDeTaiID, deTai, vaitroChuTri, diemtungnhomtruong);
            saveOrUpdateQuanLyDeTai(canBoThamGia, quanLyDeTaiID, deTai, vaitroThamGia, diemtungthanhvien);

            red.addFlashAttribute("message1", "Thêm mới thành công");
            return "redirect:/detai";
        } catch (DuplicateKeyException e) {
            red.addFlashAttribute("loi", e.getMessage());
        } finally {
            System.out.println(deTai);
        }
        // catch (Exception e) {
        // red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông
        // tin");
        // }

        red.addFlashAttribute("detai", deTai);
        return "redirect:/themmoidetai";
    }

    private void saveOrUpdateQuanLyDeTai(String[] canBoList, String[] quanLyDeTaiID, DeTai deTai,
            VaiTroDeTai vaiTroDeTai, float diem) {

        if (canBoList != null && canBoList.length > 0) {
            for (int i = 0; i < canBoList.length; i++) {
                int nguoiDungId = Integer.parseInt(canBoList[i]);
                int id = Integer.parseInt(quanLyDeTaiID[i]);
                NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
                if (nguoiDung != null) {
                    QuanLyDeTai quanLyDeTai;
                    if (id != 0) {
                        // If quanLyDeTaiID is not 0, fetch the existing QuanLyDeTai from the database
                        quanLyDeTai = quanLyDeTaiRepository.findById(id).orElse(null);
                        if (quanLyDeTai == null) {
                            // Handle the case where the QuanLyDeTai with the given ID does not exist
                            quanLyDeTai = new QuanLyDeTai();
                            quanLyDeTai.setId(id); // Set the existing ID if it should be preserved
                        }
                    } else {
                        // If quanLyDeTaiID is 0, create a new QuanLyDeTai entity
                        quanLyDeTai = new QuanLyDeTai();
                    }

                    quanLyDeTai.setDeTai(deTai);
                    quanLyDeTai.setVaiTroDeTai(vaiTroDeTai);
                    quanLyDeTai.setNguoiDung(nguoiDung);

                    quanLyDeTaiRepository.save(quanLyDeTai);
                }
            }
        }
    }

    private void deleteQuanLyDeTaiByDeTai(DeTai deTai) {
        List<QuanLyDeTai> quanLyDeTais = quanLyDeTaiRepository.findByDeTai(deTai);
        quanLyDeTaiRepository.deleteAll(quanLyDeTais);
    }
}

// @Controller
// @RequestMapping("/themmoidetai")
// public class ThemMoiDeTaiController {
// @Autowired
// private CapDeTaiService capDeTaiService;
// @Autowired
// private DonViChuTriService donViChuTriService;
// @Autowired
// private NguoiDungService nguoiDungService;
// @Autowired
// private DeTaiService deTaiService;
// @Autowired
// private UploadFilePDF uploadFilePDF;
// @Autowired
// private VaiTroDeTaiRepository vaiTroDeTaiRepository;
// @Autowired
// private NguoiDungRepository nguoiDungRepository;
//
// @GetMapping("")
// public String themMoiDeTai(Model model) {
// if (!model.containsAttribute("detai")) {
// model.addAttribute("detai", new DeTai());
// }
// List<CapDeTai> capDeTais = capDeTaiService.danhSach();
// List<DonViChuTri> donViChuTris = donViChuTriService.danhSach();
// List<NguoiDung> nguoiDungs = nguoiDungService.danhSach();
// model.addAttribute("danhSachDonViChuTri", donViChuTris);
// model.addAttribute("danhSachCapDeTai", capDeTais);
// model.addAttribute("danhSachNguoiDung", nguoiDungs);
// return "themmoidetai/themmoi";
// }
//
// @PostMapping("/luu")
// public String luDeTai(@Valid @ModelAttribute("detai") DeTai deTai,
// BindingResult bindingResult,
// @RequestParam("file") MultipartFile multipartFile,
// HttpServletRequest request,
// RedirectAttributes red) {
// if (bindingResult.hasErrors()) {
// handleValidationErrors(bindingResult, red);
// red.addFlashAttribute("detai", deTai);
// return "redirect:/themmoidetai";
// }
//
// try {
// processFileUpload(deTai, multipartFile);
// deTaiService.luu(deTai);
// processCanBoDeTai(deTai, request);
// red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
// return "redirect:/quanlydetai";
// } catch (DuplicateKeyException e) {
// red.addFlashAttribute("loi", e.getMessage());
// } catch (Exception e) {
// red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông
// tin");
// }
//
// red.addFlashAttribute("detai", deTai);
// return "redirect:/themmoidetai";
// }
//
// @GetMapping("/xoa/{id}")
// public String xoa(@PathVariable("id") Integer id, RedirectAttributes red) {
// try {
// deTaiService.delete(id);
// red.addFlashAttribute("message1", "Xoá id:" + id + " thành công");
// } catch (DeTaiNotFoundException e) {
// red.addFlashAttribute("message", e.getMessage());
// }
// return "redirect:/nguoidung";
// }
//
// @GetMapping("sua/{id}")
// public String sua(@PathVariable("id") Integer id, Model model,
// RedirectAttributes red) {
// try {
// DeTai deTai = deTaiService.get(id);
// List<CapDeTai> capDeTais = capDeTaiService.danhSach();
// List<DonViChuTri> donViChuTris = donViChuTriService.danhSach();
// List<NguoiDung> nguoiDungs = nguoiDungService.danhSach();
// List<Khoa> danhSachKhoa = nguoiDungService.danhSachKhoa();
// model.addAttribute("danhSachDonViChuTri", donViChuTris);
// model.addAttribute("danhSachCapDeTai", capDeTais);
// model.addAttribute("danhSachNguoiDung", nguoiDungs);
// return "themmoidetai/update";
// } catch (DeTaiNotFoundException e) {
// red.addFlashAttribute("message", e.getMessage());
// return "redirect:/nguoidung";
// }
// }
//
// private void handleValidationErrors(BindingResult bindingResult,
// RedirectAttributes red) {
// if (bindingResult.hasFieldErrors("tenDeTai")) {
// red.addFlashAttribute("tenDeTaiError",
// bindingResult.getFieldError("tenDeTai").getDefaultMessage());
// }
// if (bindingResult.hasFieldErrors("maSo")) {
// red.addFlashAttribute("maSoError",
// bindingResult.getFieldError("maSo").getDefaultMessage());
// }
// if (bindingResult.hasFieldErrors("thongTinDeTai")) {
// red.addFlashAttribute("thongTinDeTaiError",
// bindingResult.getFieldError("thongTinDeTai").getDefaultMessage());
// }
// if (bindingResult.hasFieldErrors("thoiGianBatDau")) {
// red.addFlashAttribute("thoiGianBatDauError",
// bindingResult.getFieldError("thoiGianBatDau").getDefaultMessage());
// }
// if (bindingResult.hasFieldErrors("thoiGianKetThuc")) {
// red.addFlashAttribute("thoiGianKetThucError",
// bindingResult.getFieldError("thoiGianKetThuc").getDefaultMessage());
// }
// }
//
// private void processFileUpload(DeTai deTai, MultipartFile multipartFile) {
// if (multipartFile != null && !multipartFile.isEmpty()) {
// String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
// if (StringUtils.hasText(fileName)) {
// deTai.setFileDinhKem(fileName);
// File savedFile = uploadFilePDF.handleUploadFile(multipartFile);
// String fileDinhKem = savedFile.getName();
// deTai.setFileLuuTru(fileDinhKem);
// }
// } else {
// deTai.setFileDinhKem(null);
// deTai.setFileLuuTru(null);
// }
// }
//
// private void processCanBoDeTai(DeTai deTai, HttpServletRequest request) {
// String[] canBoChuTri = request.getParameterValues("truongnhom");
// String[] canBoThamGia = request.getParameterValues("thanhvien");
// VaiTroDeTai vaitroChuTri = vaiTroDeTaiRepository.findByTenVaiTro("Cán bộ chủ
// trì");
// VaiTroDeTai vaitroThamGia = vaiTroDeTaiRepository.findByTenVaiTro("Cán bộ
// tham gia");
//
// if (canBoChuTri != null && canBoThamGia != null) {
// for (String chuTriId : canBoChuTri) {
// for (String thamGiaId : canBoThamGia) {
// if (chuTriId.equals(thamGiaId)) {
// throw new IllegalArgumentException("Tên cán bộ không được trùng nhau giữa Cán
// bộ chủ trì và Cán bộ tham gia.");
// }
// }
// }
// }
//
// if (canBoChuTri == null || canBoChuTri.length == 0) {
// throw new IllegalArgumentException("Bạn cần phải chọn ít nhất một cán bộ chủ
// trì.");
// }
//
// for (String chuTriId : canBoChuTri) {
// int nguoiDungId = Integer.parseInt(chuTriId);
// NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
// if (nguoiDung != null) {
// deTai.addQuanLyDeTai(new QuanLyDeTai(deTai, nguoiDung, vaitroChuTri, 10F));
// }
// }
//
// if (canBoThamGia != null && canBoThamGia.length > 0) {
// for (String thamGiaId : canBoThamGia) {
// int nguoiDungId = Integer.parseInt(thamGiaId);
// NguoiDung nguoiDung = nguoiDungRepository.findById(nguoiDungId).orElse(null);
// if (nguoiDung != null) {
// deTai.addQuanLyDeTai(new QuanLyDeTai(deTai, nguoiDung, vaitroThamGia, 10F));
// }
// }
// }
// }
// }
