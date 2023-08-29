package com.donacuoikhoa.quanlykhoahoc.bomon;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoMonRepository extends JpaRepository<BoMon, Integer> {
    BoMon findByTenBoMon(String tenBoMon);

    public Integer countById(Integer id);

    // List danh sách bộ môn từ khoa
    List<BoMon> findByKhoa(Khoa khoa);

//    @Modifying
//    @Query("UPDATE NguoiDung ng SET ng.boMon = null WHERE ng.boMon.id = :boMonId")
//    void updateBoMonForNguoiDung(@Param("boMonId") Integer boMonId);
}
