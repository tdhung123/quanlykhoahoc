package com.donacuoikhoa.quanlykhoahoc.vaitrodetai;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaitrodetai")
public class VaiTroDeTai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "vaitro", length = 150,unique = true,nullable = false)
    @Size(min = 2,max = 150, message = "Tên vai trò có độ dài từ 2 đến 150 kí tự")
    private String tenVaiTro;
}
