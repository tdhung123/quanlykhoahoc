package com.donacuoikhoa.quanlykhoahoc.khoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KhoaService {
    @Autowired KhoaRepository khoaRepository;

//    public Queue<Khoa> danhSach() {
//        List<Khoa> listKhoa = khoaRepository.findAll();
//        Queue<Khoa> queueKhoa = new LinkedList<>();
//        queueKhoa.addAll(listKhoa);
//        return queueKhoa;
//    }
    //--------------------Lấy ra danh sách
public List<Khoa> danhSach() {


    return khoaRepository.findAll();
}
//-----------------------------------Lưu thông tin
    public  void luu(Khoa khoa){
        String tenKhoa = khoa.getTenKhoa();
        Khoa existingKhoa = khoaRepository.findByTenKhoa(tenKhoa);
        if(existingKhoa != null && !existingKhoa.getId().equals(khoa.getId())){
            throw new DuplicateKeyException("Tên khoa: " +"'"+ tenKhoa+"'" + " đã tồn tại trong cơ sở dữ liệu");
        }
        khoaRepository.save(khoa);
    }
    //-----------------------------------------Sửa thông tin
    public Khoa get(Integer id) throws KhoaNotFoundException {
        Optional<Khoa> result = khoaRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new KhoaNotFoundException("Không thể tìm thấy id khoa = "+ id);
    }
    //-----------------------------------------Xoá thông tin
    public void delete(Integer id) throws KhoaNotFoundException {
        Integer cout = khoaRepository.countById(id);
        if(cout == null || cout == 0){
            throw new KhoaNotFoundException("Không thể tìm thấy id khoa"+ id);
        }
        khoaRepository.deleteById(id);
    }
    //-----------------------------------------Xoá tất cả
    public void deleteAll(){
    khoaRepository.deleteAll();
    }
    //------------------------------------------


}
