package com.donacuoikhoa.quanlykhoahoc.phanquyen;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VaiTroRepository extends JpaRepository<VaiTro,Integer> {

    VaiTro findByTen(String vaitros);
    
}
