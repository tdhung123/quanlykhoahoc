package com.donacuoikhoa.quanlykhoahoc.phanquyen;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // Các trang cho phép người dùng vào
                .antMatchers("/css/**", "/js/**", "/assets/**").permitAll()
                .antMatchers("/", "/detai", "detai/download/**").permitAll()

                // Phân quyền
                .antMatchers("/khoa/**", "/bomon/**", "/donvichutri/**", "/vaitrodetai/**",
                        "/capdetai/**", "/detai/**", "/themmoidetai/sua/**")
                .hasAuthority("admin")
                .antMatchers("/themmoidetai", "themmoidetai/luu", "/admin").hasAnyAuthority("tham gia đề tài", "admin")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // Đổi trang login
                // .loginPage("/login")
                // đổi email nhận
                // .usernameParameter("email")
                // đổi pass nhận vào
                // .passwordParameter("moto")
                // đổi đường dẫn nhận form
                .successHandler((request, response, authentication) -> {
                    for (GrantedAuthority auth : authentication.getAuthorities()) {
                        if ("admin".equals(auth.getAuthority())) {
                            response.sendRedirect("/admin");
                            return;
                        }
                    }

                    // If the user doesn't have admin authority, redirect them to a different page
                    response.sendRedirect("/admin");
                })

                // .loginProcessingUrl("/dologin")
                // Chuyển hướng người dùng khi đăng nhập
                // .defaultSuccessUrl()
                // Chuyển đế trang lỗi nếu người dùng đăng nhập sai
                // .failureUrl()
                .permitAll()
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("pass")
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/trangchu")
                // Tạo đường dẫn logout
                // .logoutUrl(null)
                // Điều hướng khi đăng suất
                // .logoutSuccessUrl(null)

                .and()
                .exceptionHandling()
                .accessDeniedPage("/403"); // Custom 403 access denied page
    }
}