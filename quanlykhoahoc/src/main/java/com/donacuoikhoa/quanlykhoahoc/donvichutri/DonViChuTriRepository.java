package com.donacuoikhoa.quanlykhoahoc.donvichutri;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonViChuTriRepository extends JpaRepository<DonViChuTri, Integer> {
    Integer countById(Integer id);

    DonViChuTri findByTenDonVi(String tenDonVi);
}
