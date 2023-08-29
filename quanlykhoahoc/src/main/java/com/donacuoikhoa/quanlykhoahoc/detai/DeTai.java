package com.donacuoikhoa.quanlykhoahoc.detai;

import com.donacuoikhoa.quanlykhoahoc.capdetai.CapDeTai;
import com.donacuoikhoa.quanlykhoahoc.donvichutri.DonViChuTri;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.quanlydetai.QuanLyDeTai;
import com.donacuoikhoa.quanlykhoahoc.vaitrodetai.VaiTroDeTai;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "detai")
public class DeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idCapDeTai", nullable = false )
    private CapDeTai capDeTai;

    @Column(name = "tendetai", nullable = false, unique = true ,length = 150)
    @NotBlank(message = "Tên đề tài không chỉ chứa mỗi khoảng cách")
    @Size(min = 2,max = 150,message = "Tên đề tài trong khoảng 2 đến 150 kí tự")
    private String tenDeTai;

    @Column(name = "maso", unique = true,nullable = false,length = 150)
    @NotBlank(message = "Mã số không chỉ chứa mỗi khoảng cách")
    @Size(min = 2,max=150,message = "mã số chỉ có thể chứa 150 kí tự")
    private String maSo;

    @Column(name = "thongtindetai",length = 250)
    @Size(max=250,message = "Thông tin đề tài chỉ có thể chứa 250 kí tự")

    private String thongTinDeTai;

    @Column(name = "kinhphi")
    private float kinhPhi;

    @Column(name = "thoigianbatdau",nullable = false)
    @NotNull(message = "Thời gian bắt đầu không đuợc để trống")
    private Date thoiGianBatDau;

    @Column(name = "thoigianketthuc",nullable = false)
    @NotNull(message = "Thời gian kết thúc không đuợc để trống")
    private Date thoiGianKetThuc;

    @Column(name = "filedinhkem",length = 250)

    private String fileDinhKem;

    @Column(name = "fileluutru",length = 512)
    private String fileLuuTru;


    @ManyToOne
    @JoinColumn(name = "iddonvichutri", nullable = false )
    private DonViChuTri donViChuTri;
    @OneToMany(mappedBy = "deTai",cascade = CascadeType.ALL)
    private Set<QuanLyDeTai> quanLyDeTai = new HashSet<>();
    public Integer getId() {
        return id;
    }

    public CapDeTai getCapDeTai() {
        return capDeTai;
    }

    public String getTenDeTai() {
        return tenDeTai;
    }

    public String getMaSo() {
        return maSo;
    }

    public String getThongTinDeTai() {
        return thongTinDeTai;
    }

    public float getKinhPhi() {
        return kinhPhi;
    }

    public Date getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public Date getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public String getFileDinhKem() {
        return fileDinhKem;
    }

    public String getFileLuuTru() {
        return fileLuuTru;
    }

    public DonViChuTri getDonViChuTri() {
        return donViChuTri;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCapDeTai(CapDeTai capDeTai) {
        this.capDeTai = capDeTai;
    }

    public void setTenDeTai(String tenDeTai) {
        this.tenDeTai = tenDeTai;
    }

    public void setMaSo(String maSo) {
        this.maSo = maSo;
    }

    public void setThongTinDeTai(String thongTinDeTai) {
        this.thongTinDeTai = thongTinDeTai;
    }

    public void setKinhPhi(float kinhPhi) {
        this.kinhPhi = kinhPhi;
    }

    public void setThoiGianBatDau(Date thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public void setThoiGianKetThuc(Date thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public void setFileDinhKem(String fileDinhKem) {
        this.fileDinhKem = fileDinhKem;
    }

    public void setFileLuuTru(String fileLuuTru) {
        this.fileLuuTru = fileLuuTru;
    }

    public void setDonViChuTri(DonViChuTri donViChuTri) {
        this.donViChuTri = donViChuTri;
    }

    public Set<QuanLyDeTai> getQuanLyDeTai() {
        return quanLyDeTai;
    }

    public void setQuanLyDeTai(Set<QuanLyDeTai> quanLyDeTai) {
        this.quanLyDeTai = quanLyDeTai;
    }

    public void themChiTiet(NguoiDung nguoiDung, VaiTroDeTai vaiTroDeTai,float diem){
        this.quanLyDeTai.add(new QuanLyDeTai(this,nguoiDung,vaiTroDeTai,diem));
    }
    public void addQuanLyDeTai(QuanLyDeTai quanLyDeTai) {
        quanLyDeTai.setDeTai(this);
        this.quanLyDeTai.add(quanLyDeTai);
    }

    public void removeQuanLyDeTai(QuanLyDeTai quanLyDeTai) {
        quanLyDeTai.setDeTai(null);
        this.quanLyDeTai.remove(quanLyDeTai);
    }


}
