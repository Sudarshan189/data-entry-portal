package com.sudarshan.portal.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true,fluent = true)
@ToString
public class User implements UserDetails {
    private String phoneNumber;
    private String otpGenerated;
    private Date lastLoginTime;
    private Set<Authority> authorities;

    public User(String phoneNumber) {
        this();
        this.phoneNumber = phoneNumber;
    }

    public User() {
        authorities = new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return otpGenerated;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
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

    public Set<Authority> addAuthority(Authority grantedAuthority) {
        this.authorities.add(grantedAuthority);
        return authorities;
    }
}
