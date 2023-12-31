package com.mine.ecomm.authservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.authservice.dto.UserDetailsDTO;
import com.mine.ecomm.authservice.exception.AuthException;
import com.mine.ecomm.authservice.service.AuthService;


@CrossOrigin
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody final UserDetailsDTO userDetailsDTO){
        final String registeredUser = authService.registerUser(userDetailsDTO);
        if (registeredUser != null && !registeredUser.isEmpty()) {
            final String msg = "User with username "+ registeredUser + " registered successfully.";
            return new ResponseEntity<>(msg, HttpStatus.CREATED);
        } else {
            throw new AuthException("User not registered.");
        }
    }

    @PostMapping("/token")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody final UserDetailsDTO auth){
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword()));
        if (authentication.isAuthenticated()) {
            String responseMsg = authService.generateToken(auth.getUsername());
            return new ResponseEntity<>(responseMsg, HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }
}
