package com.sudarshan.portal.domain.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * Created By Sudarshan Shanbhag
 */
@Getter
@Setter
@Accessors(chain = true,fluent = true)
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private String phoneNumber;
    @Column(columnDefinition = "CLOB")
    private String otpGenerated;
    @UpdateTimestamp
    private Date lastLoginTime;
    private Date otpExpiryTime;
    @Transient
    private Boolean credentialsNonExpired;
    @OneToMany(fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    mappedBy = "user")
    private Set<Authority> authorities;

    public User(String phoneNumber) {
        this();
        this.phoneNumber = phoneNumber;
    }

    public User() {
        this.credentialsNonExpired = false;
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
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Set<Authority> addAuthority(Authority grantedAuthority) {
        this.authorities.add(grantedAuthority);
        return authorities;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
}
