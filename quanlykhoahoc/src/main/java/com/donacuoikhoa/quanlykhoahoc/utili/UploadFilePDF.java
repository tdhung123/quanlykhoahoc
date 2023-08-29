package com.donacuoikhoa.quanlykhoahoc.utili;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class UploadFilePDF {
    public File handleUploadFile(MultipartFile upLoadFile) {
        // Check if the uploaded file is a PDF
        String contentType = upLoadFile.getContentType();
        if (!MediaType.APPLICATION_PDF_VALUE.equals(contentType)) {
            throw new IllegalArgumentException("Only PDF files are allowed to be uploaded.");
        }

        // Tạo Đường dẫn
        String folderPath = "C:\\Users\\Admin\\Desktop\\doancuoikhoa\\quanlykhoahoc\\src\\main\\resources\\static\\pdf";
        // Kiểu dữ liệu là file nhưng hướng đường dẫn file đến 1 nơi để ảnh nào đó
        File myUpLoadFolder = new File(folderPath);
        // Kiểm tra thư mục có tồn tại ko nếu ko có sẽ tạo thư  mục mới
        if (!myUpLoadFolder.exists()) {
            // nẾu file này không tồn tại
            // mkdir :Tạo tên đã được chỉ định nhưng nếu các thư mục mẹ không có nó chỉ tạo
            // thư mục đầu cuối ví ....Spring ... bị xoá mất thư mục src  --> Thì nó chỉ tạo ra thư mục đầu cuối
            // mkdirs: tạo từ đầu --> chú ý số lỗi như ko tạo được giá trị cuối vì 1 lý do nào đó

            myUpLoadFolder.mkdirs();
        }
        // upLoadFile.getOriginalFilename() Lấy tên gốc của file để upload lên
        File saveFile = null;
        try {
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "_" + upLoadFile.getOriginalFilename();
            saveFile = new File(myUpLoadFolder, fileName);
            upLoadFile.transferTo(saveFile);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return saveFile;
    }
}