package com.example.ebook.domain.member.entity;

import com.example.ebook.domain.base.BaseTimeEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Member extends BaseTimeEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long authLevel;

    private LocalDateTime lastLoginTime;

    private Integer restCash;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() ->
                "ROLE_USER"
        );

        if (authLevel == 7) {
            collectors.add(() ->
                    "ROLE_ADMIN"
            );
        }

        if (!nickname.isBlank()) {
            collectors.add(() ->
                    "ROLE_WRITER"
            );
        }
        return collectors;
    }

    public List<String> getRoles() {
        return getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
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

    public void updateInfo(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void payRestCash(int price) {
        this.restCash -= price;
    }

    public void withdrawRestCash(int price) {
        this.restCash -= price;
    }

    public void addRestCash(int price) {
        this.restCash += price;
    }
}
