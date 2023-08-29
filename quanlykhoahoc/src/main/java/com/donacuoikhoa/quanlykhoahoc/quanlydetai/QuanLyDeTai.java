package com.donacuoikhoa.quanlykhoahoc.quanlydetai;


import com.donacuoikhoa.quanlykhoahoc.detai.DeTai;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTai;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "quanlydetai",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"iddetai","idnguoidung"})
})
public class QuanLyDeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @ManyToOne
    @JoinColumn(name = "iddetai",nullable = false)
    private DeTai deTai;

    @ManyToOne
    @JoinColumn(name = "idnguoidung",nullable = false)
    private NguoiDung nguoiDung;

    @ManyToOne
    @JoinColumn(name = "vaitrodetai",nullable = false)
    private VaiTroDeTai vaiTroDeTai;

    @Column(name = "diemthanhvien")
    private Float diemThanhVien;

    public QuanLyDeTai(DeTai deTai, NguoiDung nguoiDung, VaiTroDeTai vaiTroDeTai, Float diemThanhVien) {
        this.deTai = deTai;
        this.nguoiDung = nguoiDung;
        this.vaiTroDeTai = vaiTroDeTai;
        this.diemThanhVien = diemThanhVien;
    }

    public QuanLyDeTai(Integer id, DeTai deTai, NguoiDung nguoiDung, VaiTroDeTai vaiTroDeTai, Float diemThanhVien) {
        this.id = id;
        this.deTai = deTai;
        this.nguoiDung = nguoiDung;
        this.vaiTroDeTai = vaiTroDeTai;
        this.diemThanhVien = diemThanhVien;
    }

    public String toString1() {
        return nguoiDung.getHoVaTen() ;
    }

    public Integer getId() {
        return id;
    }
}
