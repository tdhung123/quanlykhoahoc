package com.donacuoikhoa.quanlykhoahoc.vaitrodetai;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaiTroDeTaiRepository extends JpaRepository<VaiTroDeTai, Integer> {
    VaiTroDeTai findByTenVaiTro(String tenVaiTro);

    public Integer countById(Integer id);

}
