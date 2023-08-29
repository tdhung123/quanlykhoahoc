package com.donacuoikhoa.quanlykhoahoc.quanlydetai;

import com.donacuoikhoa.quanlykhoahoc.detai.DeTai;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuanLyDeTaiRepository extends JpaRepository<QuanLyDeTai, Integer> {
//    @Query("SELECT detai.id AS detai_id, detai.tenDeTai, capdetai.tenCapDeTai, detai.fileDinhKem, detai.fileLuuTru, detai.kinhPhi, detai.maSo, detai.thoiGianBatDau, detai.thoiGianKetThuc, detai.thongTinDeTai, quanlydetai.diemThanhVien, nguoidung.hoVaTen, vaitrodetai.tenVaiTro " +
//            "FROM QuanLyDeTai quanlydetai " +
//            "LEFT JOIN quanlydetai.deTai detai " +
//            "LEFT JOIN quanlydetai.nguoiDung nguoidung " +
//            "LEFT JOIN detai.capDeTai capdetai " +
//            "LEFT JOIN quanlydetai.vaiTroDeTai vaitrodetai ")
//    List<Object[]> findAllData();
@Query("SELECT DISTINCT detai.id AS detai_id, detai.tenDeTai, capdetai.tenCapDeTai, detai.fileDinhKem, detai.fileLuuTru, detai.kinhPhi, detai.maSo, detai.thoiGianBatDau, detai.thoiGianKetThuc, detai.thongTinDeTai, quanlydetai.diemThanhVien, nguoidung.hoVaTen, vaitrodetai.tenVaiTro " +
        "FROM QuanLyDeTai quanlydetai " +
        "LEFT JOIN quanlydetai.deTai detai " +
        "LEFT JOIN quanlydetai.nguoiDung nguoidung " +
        "LEFT JOIN detai.capDeTai capdetai " +
        "LEFT JOIN quanlydetai.vaiTroDeTai vaitrodetai")
List<Object[]> findAllData();

    List<QuanLyDeTai> findAllByDeTai(DeTai deTai);

    List<QuanLyDeTai> findByDeTai(DeTai deTai);

    List<QuanLyDeTai> findByNguoiDung(int nguoiDung);

    List<QuanLyDeTai> findByNguoiDungId(Integer id);
}
