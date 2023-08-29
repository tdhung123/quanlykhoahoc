package com.donacuoikhoa.quanlykhoahoc.nguoidung;

import com.donacuoikhoa.quanlykhoahoc.bomon.BoMon;
import com.donacuoikhoa.quanlykhoahoc.bomon.BoMonNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.bomon.BoMonRepository;
import com.donacuoikhoa.quanlykhoahoc.khoa.Khoa;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaRepository;
import com.donacuoikhoa.quanlykhoahoc.khoa.KhoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private KhoaRepository khoaRepository;
    @Autowired
    private BoMonRepository boMonRepository;

    public List<NguoiDung> danhSach() {
        return nguoiDungRepository.findAll();
    }

    ///Sử lý lấy ra 1 bộ môn trong khoa
    public List<BoMon> danhSachBoMonByKhoa(Integer id) {
        Khoa khoa = khoaRepository.findById(id).orElse(null);
        if (khoa != null) {
            return boMonRepository.findByKhoa(khoa);
        }
        return Collections.emptyList();
    }

    public List<Khoa> danhSachKhoa() {
        return khoaRepository.findAll();
    }

    //    --------------------------------Luu thông tin
    public void luu(NguoiDung nguoiDung) {
        String maNguoiDung = nguoiDung.getMaNguoiDung();
        NguoiDung maNguoiDungTonTai = nguoiDungRepository.findByMaNguoiDung(maNguoiDung);
        String email = nguoiDung.getEmail();
        NguoiDung emailDaTonTai = nguoiDungRepository.findByEmail(email);
        if (maNguoiDungTonTai != null && !maNguoiDungTonTai.getId().equals(nguoiDung.getId())) {
            throw new DuplicateKeyException("Mã người dùng '" + maNguoiDung + "' Đã tồn tại");
        }
        if (emailDaTonTai != null && !emailDaTonTai.getId().equals(nguoiDung.getId())) {
            throw new DuplicateKeyException("Email '" + email + "' đã tồn tại");
        }
        String encodePasswword = this.passwordEncoder.encode(nguoiDung.getMatKhau());
        nguoiDung.setMatKhau(encodePasswword);
        nguoiDungRepository.save(nguoiDung);
    }
    //-----------------------------------
    public NguoiDung get(Integer id) throws NguoiDungNotFoundException {
        Optional<NguoiDung> result = nguoiDungRepository.findById(id);
        if(result.isPresent()){
            return result.get();
        }
        throw new NguoiDungNotFoundException("Không thể tìm thấy id người dùng: "+ id);
    }
    public void delete(Integer id) throws NguoiDungNotFoundException {
        Integer cout = nguoiDungRepository.countById(id);
        if(cout == null || cout == 0){
            throw new NguoiDungNotFoundException("Không thể tìm thấy id người dùng"+ id);
        }
        nguoiDungRepository.deleteById(id);
    }
    public String getLastMaNguoiDung() {
        NguoiDung lastNguoiDung = nguoiDungRepository.findTopByOrderByMaNguoiDungDesc();
        if (lastNguoiDung != null) {
            return lastNguoiDung.getMaNguoiDung();
        }
        return "";
    }

}

