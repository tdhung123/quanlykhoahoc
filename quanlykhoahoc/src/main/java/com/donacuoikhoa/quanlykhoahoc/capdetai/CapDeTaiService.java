package com.donacuoikhoa.quanlykhoahoc.capdetai;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CapDeTaiService {
    @Autowired CapDeTaiRepository capDeTaiRepository;

    public List<CapDeTai> danhSach() {
     return capDeTaiRepository.findAll();
    }
    //-----------------------------------Lưu thông tin
    public  void luu(CapDeTai capDeTai){
        String tenCapDeTai = capDeTai.getTenCapDeTai();
        CapDeTai existingCapDeTai = capDeTaiRepository.findByTenCapDeTai(tenCapDeTai);
        if(existingCapDeTai != null && !existingCapDeTai.getId().equals(capDeTai.getId())){
            throw new DuplicateKeyException("Tên cấp đề tài : " +"'"+ tenCapDeTai+"'" + " đã tồn tại trong cơ sở dữ liệu");
        }
        capDeTaiRepository.save(capDeTai);
    }
    //-----------------------------------------Sửa thông tin
    public CapDeTai get(Integer id) throws CapDeTaiNotFoundException {
        Optional<CapDeTai> result = capDeTaiRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new CapDeTaiNotFoundException("Không thể tìm thấy id khoa = "+ id);
    }
    //-----------------------------------------Xoá thông tin
    public void delete(Integer id) throws CapDeTaiNotFoundException {
        Integer cout = capDeTaiRepository.countById(id);
        if(cout == null || cout == 0){
            throw new CapDeTaiNotFoundException("Không thể tìm thấy id"+ id);
        }
        capDeTaiRepository.deleteById(id);
    }
    //-----------------------------------------Xoá tất cả



}

