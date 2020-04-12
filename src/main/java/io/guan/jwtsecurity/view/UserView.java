package io.guan.jwtsecurity.view;

import io.guan.jwtsecurity.model.Authority;
import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserView {
    private String username;
    private String email;
    private Set<Authority> authorities = new HashSet<>();
}
