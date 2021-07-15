package com.sudarshan.portal.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
/**
 * Created By Sudarshan Shanbhag
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authority")
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authorityId;
    private String authorityName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "phoneNumber", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;


    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String getAuthority() {
        return authorityName;
    }
}
