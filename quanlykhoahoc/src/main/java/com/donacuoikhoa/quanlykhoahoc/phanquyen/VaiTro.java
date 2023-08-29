package com.donacuoikhoa.quanlykhoahoc.phanquyen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vaitro")
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten",length = 150,nullable = false,unique = true)
    @NotBlank(message = "Tên vai trò không được để trống")
    private String ten;

    @Override
    public String toString() {
        return ten;
    }
}
