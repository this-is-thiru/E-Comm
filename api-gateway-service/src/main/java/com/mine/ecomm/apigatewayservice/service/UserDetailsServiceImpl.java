package com.mine.ecomm.apigatewayservice.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mine.ecomm.apigatewayservice.entity.UserDetailsEntity;
import com.mine.ecomm.apigatewayservice.repository.UserDetailsRepo;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {
    private final UserDetailsRepo userDetailsRepo;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        final Mono<UserDetailsEntity> userDetailsEntity = userDetailsRepo.findById(username);
        return userDetailsEntity.flatMap(entity -> {
            final UserDetails userDetails = new UserDetailsImpl(entity);
            return Mono.just(userDetails);
        }).switchIfEmpty(Mono.error(new UsernameNotFoundException("user not found " + username)));
    }
}
