package com.mine.ecomm.apigatewayservice.service;

import static java.util.stream.Collectors.joining;

import java.security.Key;
import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.mine.ecomm.apigatewayservice.dto.RegistrationRequest;
import com.mine.ecomm.apigatewayservice.entity.UserDetailsEntity;
import com.mine.ecomm.apigatewayservice.repository.UserDetailsRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class AuthService {
    /**
     * <a href="https://www.grc.com/passwords.htm">Token Website</a>
     */
    final private static String SECRET = "80DC002A54B59ACF5F198D0A8D644EEE992C04FFBCF947DAAA90AA7DFDDA2A05";
    private static final String AUTHORITIES_KEY = "roles";
    private final UserDetailsRepo userDetailsRepo;
    private final Key signKey;
    public static final JwtParser JWT_PARSER = Jwts.parserBuilder().setSigningKey(getSignKey()).build();
    AuthService(final UserDetailsRepo userDetailsRepo) {
        this.userDetailsRepo = userDetailsRepo;
        signKey = Keys.hmacShaKeyFor(keyBytes());
    }

    public Mono<String> registerUser(final RegistrationRequest registrationRequest) {
        final UserDetailsEntity userDetails = registrationRequest.toUserDetailsEntity();
        userDetails.setNewFlag();
        Mono<UserDetailsEntity> entity = userDetailsRepo.save(userDetails);
        return entity.map(UserDetailsEntity::getUsername); // <--->
    }

    public static Claims extractAllClaims(String token) {
        return JWT_PARSER.parseClaimsJws(token).getBody();
    }

    public static Boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public static boolean isTokenExpired(final String token) {
        try {
            final Claims claims = extractAllClaims(token);
            return !isTokenExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace.", e);
        }
        return false;
    }

    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTHORITIES_KEY, authorities.stream().map(GrantedAuthority::getAuthority).collect(joining(",")));
        Date now = new Date();
        Date validity = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setSubject(username)
                .setExpiration(validity)//
                .signWith(signKey, SignatureAlgorithm.HS256)//
                .compact();
    }

    public Mono<UserDetailsEntity> getUser(String username) {
        return userDetailsRepo.findById(username);
    }

    private static byte[] keyBytes() {
        return Decoders.BASE64.decode(SECRET);
    }
    public static Key getSignKey() {
        final byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
