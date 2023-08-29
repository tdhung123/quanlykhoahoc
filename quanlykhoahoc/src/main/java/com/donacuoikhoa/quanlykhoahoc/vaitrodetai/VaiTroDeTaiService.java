package com.donacuoikhoa.quanlykhoahoc.vaitrodetai;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VaiTroDeTaiService {
    @Autowired private VaiTroDeTaiRepository vaiTroDeTaiRepository;
    public List<VaiTroDeTai> danhSach() {


        return vaiTroDeTaiRepository.findAll();
    }
    //-----------------------------------Lưu thông tin
    public  void luu(VaiTroDeTai vaiTroDeTai){
        String tenVaiTro = vaiTroDeTai.getTenVaiTro();
        VaiTroDeTai existingVaiTro = vaiTroDeTaiRepository.findByTenVaiTro(tenVaiTro);
        if(existingVaiTro != null && !existingVaiTro.getId().equals(vaiTroDeTai.getId())){
            throw new DuplicateKeyException("Tên khoa: " +"'"+ vaiTroDeTai+"'" + " đã tồn tại trong cơ sở dữ liệu");
        }
        vaiTroDeTaiRepository.save(vaiTroDeTai);
    }
    //-----------------------------------------Sửa thông tin
    public VaiTroDeTai get(Integer id) throws VaiTroDeTaiNotFoundException {
        Optional<VaiTroDeTai> result = vaiTroDeTaiRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new VaiTroDeTaiNotFoundException("Không thể tìm thấy id vai trò đề âfi = "+ id);
    }
    //-----------------------------------------Xoá thông tin
    public void delete(Integer id) throws VaiTroDeTaiNotFoundException {
        Integer cout = vaiTroDeTaiRepository.countById(id);
        if(cout == null || cout == 0){
            throw new VaiTroDeTaiNotFoundException("Không thể tìm thấy id vai trò có:"+ id);
        }
        vaiTroDeTaiRepository.deleteById(id);
    }
    //-----------------------------------------Xoá tất cả

}
