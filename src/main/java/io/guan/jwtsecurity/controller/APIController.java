package io.guan.jwtsecurity.controller;

import io.guan.jwtsecurity.security.authentication.AuthenticationRequest;
import io.guan.jwtsecurity.security.authentication.AuthenticationResponse;
import io.guan.jwtsecurity.security.jwt.JwtUtil;
import io.guan.jwtsecurity.security.registration.RegistrationRequest;
import io.guan.jwtsecurity.security.registration.RegistrationResponse;
import io.guan.jwtsecurity.service.AppUserDetailsService;
import io.guan.jwtsecurity.view.UserView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class APIController {
    private AuthenticationManager authenticationManager;
    private AppUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @GetMapping("/admin")
    public List<UserView> adminEndpoint() {
        return userDetailsService.getAllUserViews();
    }

    @GetMapping("/home")
    public String homeEndpoint() {
        return "Logged In Successfully! This is the homepage";
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        registrationRequest.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        userDetailsService.saveUser(userDetailsService.toUser(registrationRequest));

        return ResponseEntity.ok(RegistrationResponse.builder()
                .message("Your user is created successfully").build());
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateEndpoint(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(AuthenticationResponse.builder().jwt(jwt).build());
    }
}
