package com.donacuoikhoa.quanlykhoahoc.capdetai;


import org.springframework.data.jpa.repository.JpaRepository;

public interface CapDeTaiRepository extends JpaRepository<CapDeTai, Integer> {
    CapDeTai findByTenCapDeTai(String tenCapDeTai);

    public Integer countById(Integer id);


}
