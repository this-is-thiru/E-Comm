package com.mine.ecomm.apigatewayservice.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mine.ecomm.apigatewayservice.dto.LoginRequest;
import com.mine.ecomm.apigatewayservice.dto.RegistrationRequest;
import com.mine.ecomm.apigatewayservice.entity.UserDetailsEntity;
import com.mine.ecomm.apigatewayservice.exception.AuthException;
import com.mine.ecomm.apigatewayservice.service.AuthService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@CrossOrigin
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class GatewayController {

    private final AuthService authService;
    private final ReactiveAuthenticationManager authenticationManager;

    @GetMapping("/no")
    public Mono<String> get() {
        return Mono.just("Hello!, No Auth Required, Response");
    }

    @GetMapping("/yes")
    public Mono<String> get1() {
        return Mono.just("Hello!, Auth is Required, Response");
    }

    @GetMapping("/no/user/{email}")
    public Mono<UserDetailsEntity> get2(@PathVariable String email) {
        return authService.getUser(email);
    }


    @PostMapping("/register")
    public Mono<ResponseEntity<String>> addNewUser(@RequestBody final RegistrationRequest registrationRequest) {
        final Mono<String> registeredUser = authService.registerUser(registrationRequest);
        return registeredUser.filter(user -> !user.isEmpty())
                .switchIfEmpty(Mono.error(new AuthException("User not registered.")))
                .then(Mono.just(createdSuccessfully("User registered successfully." + registrationRequest.getUsername())));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody Mono<LoginRequest> loginRequest) {
        return loginRequest.flatMap(
                        request ->
                                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()))
                ).map(authService::createToken)
                .map(jwt -> {
                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
                            var tokenBody = Map.of("id_token", jwt);
                            return new ResponseEntity<>(tokenBody, httpHeaders, HttpStatus.OK);
                        }
                );
    }

    static <T> ResponseEntity<T> createdSuccessfully(T body) {
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }
}
