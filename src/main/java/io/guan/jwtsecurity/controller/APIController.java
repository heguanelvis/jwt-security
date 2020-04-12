package io.guan.jwtsecurity.controller;

import io.guan.jwtsecurity.security.authentication.AuthenticationRequest;
import io.guan.jwtsecurity.security.authentication.AuthenticationResponse;
import io.guan.jwtsecurity.security.jwt.JwtUtil;
import io.guan.jwtsecurity.service.AppUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class APIController {
    private AuthenticationManager authenticationManager;
    private AppUserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Hello Admin!";
    }

    @GetMapping("/home")
    public String homeEndpoint() {
        return "Logged In Successfully! This is the homepage";
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateEndpoint(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(AuthenticationResponse.builder().jwt(jwt).build());
    }
}