package com.donacuoikhoa.quanlykhoahoc.khoa;

import com.donacuoikhoa.quanlykhoahoc.bomon.BoMon;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "khoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Khoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "tenkhoa", length = 150, nullable = false, unique = true)
    @Size(min = 2, max= 150, message = "Tên khoa từ 2 đến 150 kí tự")
    @NotBlank(message = "Tên Khoa không thể chứa mỗi dấu cách")
    private String tenKhoa;

    @OneToMany(mappedBy = "khoa", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<BoMon> boMons;

    @Override
    public String toString() {
        return "Khoa{" +
                "id=" + id +
                ", tenKhoa='" + tenKhoa + '\'' +

                '}';
    }

}
