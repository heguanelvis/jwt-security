package io.guan.jwtsecurity.service;

import io.guan.jwtsecurity.model.Authority;
import io.guan.jwtsecurity.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class AppUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return User.builder()
                .userId(1)
                .username("guan")
                .password("guan")
                .email("guan.he@slalom.com")
                .authorities(new ArrayList<>(Arrays.asList(Authority.ROLE_PROVIDER)))
                .build();
    }
}