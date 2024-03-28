package com.spring.auth.domain.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter @Setter
@NoArgsConstructor
public class User implements UserDetails {

    private String name;
    private String ci;
    private String di;
    private String email;
    private String password;
    private String mobile;
    private String gender;
    private String birthDate;

    private String roles;


    @Builder
    public User(
            String name,
            String ci,
            String email,
            String password,
            String mobile,
            String gender,
            String role
    ){
        this.name = name;
        this.ci = ci;
        this.email = email;
        this.password = password;
        this.mobile = mobile;
        this.gender = gender;
        this.roles = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.roles == null) return Collections.singleton(new SimpleGrantedAuthority(null));
        return Collections.singleton(new SimpleGrantedAuthority(this.roles));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
}
