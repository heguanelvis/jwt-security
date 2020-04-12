package io.guan.jwtsecurity.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtil {
    private static final String SECRET_KEY = "YjGWs2xpFl80wYtV5DcccTXNXkFNQnaK";
    private static final String BEARER_PREFIX = "Bearer ";
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        return (Collection<? extends GrantedAuthority>) extractAllClaims(token).get("authorities");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), userDetails.getAuthorities());
    }

    private String createToken(Map<String, Object> claims, String subject, Collection<? extends GrantedAuthority> authorities) {
        int validDurationMillis = 216_000_000;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .claim("authorities", authorities)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + validDurationMillis))
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
