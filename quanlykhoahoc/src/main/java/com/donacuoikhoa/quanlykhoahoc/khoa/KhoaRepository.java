package com.donacuoikhoa.quanlykhoahoc.khoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KhoaRepository extends JpaRepository<Khoa, Integer> {
    Khoa findByTenKhoa(String tenKhoa);

    public Integer countById(Integer id);


}
