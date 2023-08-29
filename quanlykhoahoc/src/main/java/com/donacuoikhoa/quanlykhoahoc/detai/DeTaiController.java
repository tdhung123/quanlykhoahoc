package com.donacuoikhoa.quanlykhoahoc.detai;

import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTaiService;
import com.donacuoikhoa.quanlykhoahoc.donvichutri.DonViChuTriService;
import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.utili.UploadFilePDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/detai")
public class DeTaiController {
    @Autowired
    private DeTaiService deTaiService;
    @Autowired
    private CapDeTaiService capDeTaiService;
    @Autowired
    private DonViChuTriService donViChuTriService;
    @Autowired
    private DeTaiRepository deTaiRepository;

    @GetMapping("")
    public String danhSach(Model model) {
        List<DeTai> deTais = deTaiService.danhSach();
        Collections.reverse(deTais);
        model.addAttribute("danhsachdetai", deTais);
        return "detai/danhsach";
    }

    @GetMapping("/themmoi")
    public String themMoiDeTai(Model model) {
        if (!model.containsAttribute("detai")) {
            model.addAttribute("detai", new DeTai());
        }
        model.addAttribute("danhSachCapDeTai", capDeTaiService.danhSach());
        model.addAttribute("danhSachDonViChuTri", donViChuTriService.danhSach());
        return "detai/themmoi";
    }

    @Autowired
    private UploadFilePDF uploadFilePDF;
//    @PostMapping("/luu")
//    public String luuDeTai(@Valid @ModelAttribute("detai") DeTai deTai,
//                           @RequestParam("file") MultipartFile multipartFile,
//                           BindingResult bindingResult,
//                           RedirectAttributes red) throws IOException {
//        if (bindingResult.hasErrors()) {
//            return "detai/themmoi";
//        } else {
//            try {
//                if (multipartFile != null && !multipartFile.isEmpty()) {
//                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//                    if (StringUtils.hasText(fileName)) {
//                        deTai.setFileDinhKem(fileName);
//                        // Save the file and get the path
//                        File savedFile = uploadFilePDF.handleUploadFile(multipartFile);
//                        String fileDinhKem = savedFile.getName();
//                        // Set the file path in the 'DeTai' entity
//                        deTai.setFileLuuTru(fileDinhKem);
//                    }
//                } else {
//                    // Handle case when no file is uploaded
//                    deTai.setFileDinhKem(null);
//                    deTai.setFileLuuTru(null);
//                }
//                deTaiService.luu(deTai);
//                red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
//                return "redirect:/detai";
//            } catch (DuplicateKeyException e) {
//                red.addFlashAttribute("loi", e.getMessage());
//            } catch (Exception e) {
//                red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin.");
//            }
//        }
//        red.addFlashAttribute("detai", deTai);
//        return "redirect:/detai/themmoi";
//    }
@PostMapping("/luu")
public String luDeTai(@Valid @ModelAttribute("deTai") DeTai deTai, BindingResult bindingResult,
                      @RequestParam("file") MultipartFile multipartFile,
                           RedirectAttributes red){
    if (bindingResult.hasErrors()) {
        if (bindingResult.hasFieldErrors("tenDeTai")){
            red.addFlashAttribute("tenDeTaiError", bindingResult.getFieldError("tenDeTai").getDefaultMessage());
        }
        if (bindingResult.hasFieldErrors("maSo")){
            red.addFlashAttribute("maSoError", bindingResult.getFieldError("maSo").getDefaultMessage());
        }
        if (bindingResult.hasFieldErrors("thongTinDeTai")){
            red.addFlashAttribute("thongTinDeTaiError", bindingResult.getFieldError("thongTinDeTai").getDefaultMessage());
        }
        if (bindingResult.hasFieldErrors("thoiGianBatDau")){
            red.addFlashAttribute("thoiGianBatDauError", bindingResult.getFieldError("thoiGianBatDau").getDefaultMessage());
        }
        if (bindingResult.hasFieldErrors("thoiGianKetThuc")){
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
            }else {
                deTai.setFileDinhKem(null);
                deTai.setFileLuuTru(null);
            }
            deTaiService.luu(deTai);
            red.addFlashAttribute("message1", "Thêm mới bộ môn thành công");
            return "redirect:/detai";
        } catch (DuplicateKeyException e) {
            red.addFlashAttribute("loi", e.getMessage());
        } catch (Exception e) {
            red.addFlashAttribute("loi", "Đã xảy ra lỗi gì đó hãy kiểm tra lại các thông tin ");
        }
    }
    red.addFlashAttribute("detai", deTai);
    return "redirect:/detai/themmoi";
}


    @GetMapping("/download")
    public void downloadFile(@Param("id") Integer id, HttpServletResponse response) throws Exception {
        Optional<DeTai> result = deTaiRepository.findById(id);
        String folderPath = "C:\\Users\\Admin\\Desktop\\doancuoikhoa\\quanlykhoahoc\\src\\main\\resources\\static\\pdf";

        if (!result.isPresent()) {
            throw new Exception("Could not find document with ID: " + id);
        }

        DeTai deTai = result.get();
        String fileName = deTai.getFileLuuTru();
        File imageFile = new File(folderPath, fileName);

        if (!imageFile.exists()) {
            throw new Exception("Could not find image file with name: " + fileName);
        }

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);

        try (FileInputStream fis = new FileInputStream(imageFile);
             ServletOutputStream outputStream = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new Exception("Error while reading or writing the image file: " + e.getMessage());
        }
    }



}
