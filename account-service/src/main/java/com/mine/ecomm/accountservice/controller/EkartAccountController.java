package com.mine.ecomm.accountservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.accountservice.dto.CreateAccountRequest;
import com.mine.ecomm.accountservice.dto.UpdateAccountRequest;
import com.mine.ecomm.accountservice.exception.InvalidException;
import com.mine.ecomm.accountservice.exception.ServiceException;
import com.mine.ecomm.accountservice.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class EkartAccountController {
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        final String successMessage = accountService.registerNewCustomer(createAccountRequest);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<String> updateCustomer(final @PathVariable String email, final @Valid @RequestBody UpdateAccountRequest updateAccountRequest) {
        final String successMsg = accountService.updateCustomerDetails(email, updateAccountRequest);
        if (successMsg != null) {
            return new ResponseEntity<>(successMsg, HttpStatus.OK);
        } else {
            throw new InvalidException("The old password did not match.");
        }
    }

    @PostMapping("/exp/{id}")
    public ResponseEntity<CreateAccountRequest> registerCustomer1(@PathVariable int id, @RequestBody CreateAccountRequest createAccountRequest) {
        if (id == 1) {
            throw new ServiceException("error");
        }

        return new ResponseEntity<>(createAccountRequest, HttpStatus.OK);

    }

    @GetMapping("/demo")
    public ResponseEntity<String> getHelloWorld() {
        throw new InvalidException("hoo");
    }
}
