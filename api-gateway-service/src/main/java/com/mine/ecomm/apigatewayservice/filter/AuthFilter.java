package com.mine.ecomm.apigatewayservice.filter;


import static com.mine.ecomm.apigatewayservice.service.AuthService.JWT_PARSER;

import java.util.Collection;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.mine.ecomm.apigatewayservice.exception.AuthException;
import com.mine.ecomm.apigatewayservice.service.AuthService;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthFilter implements WebFilter {
    private static final String AUTHORITIES_KEY = "roles";
    public static final String BEARER_PREFIX = "Bearer ";

    @Nonnull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @Nonnull WebFilterChain chain) {
        final String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info(header);
        if (header == null) {
          throw new AuthException("Authorization header not found");
        }
        final AuthType authType = authType(header);
        final Authentication authentication;
        return switch (authType) {
            case TOKEN -> {
                log.info("Token Authentication Request");
                authentication = jwtTokenAuthentication(header);
                yield chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
            }
            case BASIC -> {
                // TODO
                log.info("Basic Authentication Request");
                yield chain.filter(exchange);
            }
            default -> chain.filter(exchange);
        };
    }

    private static Authentication jwtTokenAuthentication(final String bearerToken) {
        if (StringUtils.hasText(bearerToken)) {
            if (AuthService.isTokenExpired(bearerToken)) {
                throw new AuthException("Token Expired");
            }
            final String token = bearerToken.substring(7);
            return getAuthentication(token);
        }
        return null;
    }

    public static Authentication getAuthentication(final String token) {
        Claims claims = JWT_PARSER.parseClaimsJws(token).getBody();
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(claims.get(AUTHORITIES_KEY).toString());
        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    private static AuthType authType(final String value) {
       if (StringUtils.hasText(value) && value.startsWith(BEARER_PREFIX)) {
           return AuthType.TOKEN;
       }
       return AuthType.NONE;
    }

    private enum AuthType {
        TOKEN,
        BASIC,
        NONE
    }
}
