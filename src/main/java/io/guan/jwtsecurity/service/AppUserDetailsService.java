package io.guan.jwtsecurity.service;

import io.guan.jwtsecurity.model.User;
import io.guan.jwtsecurity.repository.UserRepository;
import io.guan.jwtsecurity.security.registration.RegistrationRequest;
import io.guan.jwtsecurity.view.UserView;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        return optionalUser.orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    public boolean hasUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean hasEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User toUser(RegistrationRequest registrationRequest) {
        return User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .password(registrationRequest.getPassword())
                .build();
    }

    @Transactional
    public List<UserView> getAllUserViews() {
        return userRepository.streamAll().map(user ->
                UserView.builder()
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .authorities(user.getAuthorities())
                        .build()
        ).collect(Collectors.toList());
    }
}