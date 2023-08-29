package com.donacuoikhoa.quanlykhoahoc.phanquyen;

import com.donacuoikhoa.quanlykhoahoc.nguoidung.NguoiDung;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MyUserDetails implements UserDetails {
    private NguoiDung nguoiDung;
    public MyUserDetails(NguoiDung nguoiDung){
        this.nguoiDung =nguoiDung;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<VaiTro> vaiTros = nguoiDung.getVaiTros();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (VaiTro role : vaiTros) {
            authorities.add(new SimpleGrantedAuthority(role.getTen()));
        }

        return authorities;

    }

    @Override
    public String getPassword() {
        return nguoiDung.getMatKhau();
    }

    @Override
    public String getUsername() {
        return nguoiDung.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        return nguoiDung.getHoVaTen();
    }
}
