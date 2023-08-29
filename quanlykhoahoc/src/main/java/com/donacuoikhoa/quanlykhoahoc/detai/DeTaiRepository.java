package com.donacuoikhoa.quanlykhoahoc.detai;

import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeTaiRepository extends JpaRepository<DeTai,Integer> {
    DeTai findByTenDeTai(String tenDeTai);

    DeTai findByMaSo(String maSo);
    public Integer countById(Integer id);

}
