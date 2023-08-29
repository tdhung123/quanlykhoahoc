package com.donacuoikhoa.quanlykhoahoc.donvichutri;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "donvichutri")
public class DonViChuTri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "tendonvi", length = 150, nullable = false,unique = true)
    @Size(min = 3, max= 150, message = "Tên đơn vị từ 3 đến 150 kí tự")
    @NotBlank(message = "Tên đơn vị không thể chứa mỗi dấu cách")
    private String tenDonVi;

    @Column(name = "diachi", length = 200)
    private String diaChi;

}
