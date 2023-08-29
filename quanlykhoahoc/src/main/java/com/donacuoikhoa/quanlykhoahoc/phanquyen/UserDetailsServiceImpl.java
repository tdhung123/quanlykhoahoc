package com.donacuoikhoa.quanlykhoahoc.phanquyen;

import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungNotFoundException;
import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired private NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.getNguoiDungByEmail(username);
        if(nguoiDung == null){
            try {
                throw new NguoiDungNotFoundException("Không tìm thấy thông tin người dùng");
            } catch (NguoiDungNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return  new MyUserDetails(nguoiDung);
    }
}
