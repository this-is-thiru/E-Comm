package com.mine.ecomm.apigatewayservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.mine.ecomm.apigatewayservice.filter.AuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebFluxSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthFilter authFilter;

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http ) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it ->
                        it.pathMatchers(HttpMethod.GET, "api/auth/no").permitAll()
                                .pathMatchers(HttpMethod.GET, "api/auth/no/user/**").permitAll()
                                .pathMatchers(HttpMethod.POST, "api/auth/token").permitAll()
                                .pathMatchers(HttpMethod.POST, "api/auth/login").permitAll()
                                .pathMatchers(HttpMethod.POST, "api/auth/register").permitAll()
                                .pathMatchers(HttpMethod.DELETE, "/posts/**").hasRole("ADMIN")
                                .pathMatchers(HttpMethod.GET, "api/auth/yes").authenticated()
                                .pathMatchers("/**").authenticated()
                                .anyExchange().permitAll()
                )
                .addFilterBefore(authFilter, SecurityWebFiltersOrder.HTTP_BASIC)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager(final ReactiveUserDetailsService userDetailsService, final PasswordEncoder passwordEncoder) {
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
