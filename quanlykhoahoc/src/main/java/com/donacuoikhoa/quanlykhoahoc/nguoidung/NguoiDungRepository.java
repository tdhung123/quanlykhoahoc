package com.donacuoikhoa.quanlykhoahoc.nguoidung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Integer> {

    NguoiDung findByMaNguoiDung(String maNguoiDung);

    NguoiDung findByEmail(String maNguoiDung);

    public Integer countById(Integer id);

    @Query("SELECT u FROM NguoiDung u WHERE u.email = :email")
    public NguoiDung getNguoiDungByEmail(@Param("email") String email);

    // String getLastMaNguoiDung();

    NguoiDung findTopByOrderByMaNguoiDungDesc();

    @Query("SELECT nd.maNguoiDung FROM NguoiDung nd ORDER BY nd.maNguoiDung DESC")
    String getLastMaNguoiDung();
}
