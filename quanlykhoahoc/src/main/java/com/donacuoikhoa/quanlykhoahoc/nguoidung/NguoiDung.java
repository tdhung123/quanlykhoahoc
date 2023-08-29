package com.donacuoikhoa.quanlykhoahoc.nguoidung;

import com.donacuoikhoa.quanlykhoahoc.bomon.BoMon;

import com.donacuoikhoa.quanlykhoahoc.phanquyen.VaiTro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nguoidung")
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "manguoidung",length = 150,nullable = false,unique = true)
    @NotBlank(message = "Mã người dùng không được để trống")
    @Size(min = 2, max=150,message = "Mã người dùng từ 2 đến 150 kí tự")
    private String maNguoiDung;


    @Column(name = "hovaten",length = 150,nullable = false)
    @NotBlank(message = "Họ và tên không chỉ được sử dụng mỗi dấu cách")
    @Size(min = 2, max = 150, message = "Họ và tên từ 2 đến 150 kí tự")
    private String hoVaTen;


    @Column(name = "ngaysinh")
    private Date ngaySinh;

    @Column(name = "donvicongtac" ,length = 250)
    private String donViCongTac;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "idbomon")
    private BoMon boMon;

    @Column(name = "email", length = 250,nullable = false,unique = true)
    @Email(message = "Vui lòng nhập đúng cấu trúc của email")
    @Size(min = 4,max = 250, message = "email từ 4 đến 250 kí tự")
    private String email;

    @Column(name = "matkhau",length = 512,nullable = false)
    @NotBlank(message = "mật không khẩu chỉ nên chứ mỗi dấu khoảng trắng")
    @Size(min = 2, max = 215,message = "Nhập mật khẩu trong khoảng 2 đến 215 kí tự")
    private String matKhau;

    @Column(name = "sodienthoai", length = 50)
    private String soDienThoai;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "nguoidung_vaitro",
                joinColumns = @JoinColumn (name = "nguoidung_id"),
            inverseJoinColumns = @JoinColumn(name = "vaitro_id")
    )
    private Set<VaiTro> vaiTros = new HashSet<>();
}
