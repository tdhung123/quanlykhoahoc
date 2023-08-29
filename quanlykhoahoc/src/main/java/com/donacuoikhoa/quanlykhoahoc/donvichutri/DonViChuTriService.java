package com.donacuoikhoa.quanlykhoahoc.donvichutri;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DonViChuTriService {
    @Autowired DonViChuTriRepository donViChuTriRepository;
    public List<DonViChuTri> danhSach() {

        return donViChuTriRepository.findAll();
    }
    //-----------------------------------Lưu thông tin
    public  void luu(DonViChuTri donViChuTri){
        String tenDonVi = donViChuTri.getTenDonVi();
        DonViChuTri existingKhoa = donViChuTriRepository.findByTenDonVi(tenDonVi);
        if(existingKhoa != null && !existingKhoa.getId().equals(donViChuTri.getId())){
            throw new DuplicateKeyException("Tên đơn vị: " +"'"+ tenDonVi+"'" + " đã tồn tại trong cơ sở dữ liệu");
        }
        donViChuTriRepository.save(donViChuTri);
    }
    //-----------------------------------------Sửa thông tin
    public DonViChuTri get(Integer id) throws DonViChuTriNotFoundException {
        Optional<DonViChuTri> result = donViChuTriRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new DonViChuTriNotFoundException("Không thể tìm thấy id đơn vị chủ trì = "+ id);
    }
    //-----------------------------------------Xoá thông tin
    public void delete(Integer id) throws DonViChuTriNotFoundException {
        Integer cout = donViChuTriRepository.countById(id);
        if(cout == null || cout == 0){
            throw new DonViChuTriNotFoundException("Không thể tìm thấy id đơn vị chủ trì"+ id);
        }
        donViChuTriRepository.deleteById(id);
    }
    //-----------------------------------------Xoá tất cả

    //------------------------------------------

}
