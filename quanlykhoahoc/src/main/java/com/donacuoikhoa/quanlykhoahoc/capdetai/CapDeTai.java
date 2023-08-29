package com.donacuoikhoa.quanlykhoahoc.capdetai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "capdetai")
public class CapDeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tencapdetai", length = 250, nullable = false,unique = true)
    @NotBlank(message = "Cấp đề tài không chỉ có mỗi dấu cách")
    @Size(min = 2, max = 250 ,message = "tên cấp đề tài từ 2 đến 250 kí tự")
    private String tenCapDeTai;

    @Column(nullable = false, name = "tongsogio")
    @NotNull(message = "Tổng số giờ không được để trống")
    private float tongSoGio;


    @Column(nullable = false, name = "xdiemtruongnhom")
    @NotNull(message = "Không được để trống ô này :))")
    private float xDiemTruongNhom;

    @Column(nullable = false, name = "ydiemtruongnhom")
    @NotNull(message = "Không được để trống ô này :))")
    private float yDiemTruongNhom;

    @Column(nullable = false, name = "xdiemthanhvien")
    @NotNull(message = "Không được để trống ô này :))")
    private float xDiemThanhVien;

    public Integer getId() {
        return id;
    }

    public String getTenCapDeTai() {
        return tenCapDeTai;
    }

    public float getTongSoGio() {
        return tongSoGio;
    }

    public float getxDiemTruongNhom() {
        return xDiemTruongNhom;
    }

    public float getyDiemTruongNhom() {
        return yDiemTruongNhom;
    }

    public float getxDiemThanhVien() {
        return xDiemThanhVien;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTenCapDeTai(String tenCapDeTai) {
        this.tenCapDeTai = tenCapDeTai;
    }

    public void setTongSoGio(float tongSoGio) {
        this.tongSoGio = tongSoGio;
    }

    public void setxDiemTruongNhom(float xDiemTruongNhom) {
        this.xDiemTruongNhom = xDiemTruongNhom;
    }

    public void setyDiemTruongNhom(float yDiemTruongNhom) {
        this.yDiemTruongNhom = yDiemTruongNhom;
    }

    public void setxDiemThanhVien(float xDiemThanhVien) {
        this.xDiemThanhVien = xDiemThanhVien;
    }
    public String toString1() {
        return
                  tongSoGio +
                "*(" + xDiemTruongNhom +
                "+" + yDiemTruongNhom+"/n)"
                ;
    }
    public String toString2() {
        return
                tongSoGio + "*(" + xDiemThanhVien + "/n)";
    }
}
