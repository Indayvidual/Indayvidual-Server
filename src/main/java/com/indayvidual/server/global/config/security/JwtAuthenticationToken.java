package com.indayvidual.server.global.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final JwtUserPrincipal principal;
    private final String token;

    // 인증 전
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    // 인증 후 (JwtUserPrincipal 사용)
    public JwtAuthenticationToken(JwtUserPrincipal principal, String token,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}