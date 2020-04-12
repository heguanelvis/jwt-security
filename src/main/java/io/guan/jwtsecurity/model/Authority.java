package io.guan.jwtsecurity.model;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    ROLE_ADMIN, ROLE_VOLUNTEER, ROLE_PROVIDER;

    public String getAuthority() {
        return name();
    }
}