package com.donacuoikhoa.quanlykhoahoc.bomon;

import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bomon")
public class BoMon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idkhoa", nullable = false )
    @JsonBackReference
    private Khoa khoa;

    @Size(min = 2, max = 150, message = "tên bộ môn được nhập trong khoảng từ 2 đê 150 kí tự")
    @NotBlank(message = "Tên bộ môn không chỉ chứa khoảng cách hoặc để trống")
    @Column(name = "tenbomon",unique = true,length = 150, nullable = false)
    private String tenBoMon;

    @OneToMany(mappedBy = "boMon")
    @JsonIgnore
    private List<NguoiDung> danhSachNguoiDung = new ArrayList<>();

    @Override
    public String toString() {
        return "BoMon{" +
                "id=" + id +
                ", tenBoMon='" + tenBoMon + '\'' +
                '}';
    }

}
