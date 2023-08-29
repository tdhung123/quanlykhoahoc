package com.donacuoikhoa.quanlykhoahoc.bomon;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoMonService {
    @Autowired BoMonRepository boMonRepository;
    public List<BoMon> danhSach(){
        return boMonRepository.findAll();
    }

    public void luu(BoMon boMon){
        String tenBoMon = boMon.getTenBoMon();
        BoMon boMonDaTonTai = boMonRepository.findByTenBoMon(tenBoMon);
        if (boMonDaTonTai!=null && !boMonDaTonTai.getId().equals(boMon.getId())){
            throw new DuplicateKeyException("Tên Bộ Môn '"+ tenBoMon + "' Đã tồn tại");
        }
        boMonRepository.save(boMon);
    }
    public BoMon get(Integer id) throws BoMonNotFoundException {
        Optional<BoMon> result = boMonRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new BoMonNotFoundException("Không thể tìm thấy id bộ môn: "+ id);
    }
    public void delete(Integer id) throws BoMonNotFoundException {
        Integer cout = boMonRepository.countById(id);
        if(cout == null || cout == 0){
            throw new BoMonNotFoundException("Không thể tìm thấy id khoa"+ id);
        }
        boMonRepository.deleteById(id);
    }

    public List<BoMon> danhSachBoMonTheoKhoa(Khoa khoa) {
        return boMonRepository.findByKhoa(khoa);
    }
}
