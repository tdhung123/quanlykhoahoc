package com.donacuoikhoa.quanlykhoahoc.quanlydetai;

import com.donacuoikhoa.quanlykhoahoc.detai.DeTai;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuanLyDeTaiService {
    @Autowired  QuanLyDeTaiRepository  quanLyDeTaiRepository;

    public List<QuanLyDeTai> danhSach(){

        return quanLyDeTaiRepository.findAll();
    }
    public List<Object[]> findAllData() {
        return quanLyDeTaiRepository.findAllData();
    }
    public List<QuanLyDeTai> getAllByDeTai(DeTai deTai) {
        return quanLyDeTaiRepository.findAllByDeTai(deTai);
    }
}
