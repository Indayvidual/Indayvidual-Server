package com.indayvidual.server.global.config.security;

import com.indayvidual.server.domain.user.entity.User;
import com.indayvidual.server.domain.user.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public record JwtUserPrincipal(Long userId, String email, Role role,
                               Collection<? extends GrantedAuthority> authorities) {
    // User 엔티티로부터 생성
    public static JwtUserPrincipal from(User user) {
        Collection<GrantedAuthority> authorities =
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));

        return new JwtUserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                authorities
        );
    }
}