package com.mine.ecomm.accountservice.controller;

import java.util.Set;

import com.mine.ecomm.accountservice.dto.AccountDTO;
import com.mine.ecomm.accountservice.dto.UpdateAccountDTO;
import com.mine.ecomm.accountservice.exception.InvalidException;
import com.mine.ecomm.accountservice.exception.ServiceException;
import com.mine.ecomm.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * The type Ekart users controller.
 */
@CrossOrigin
@RestController
@RequestMapping("/account")
public class EkartAccountController {
    @Autowired
    private AccountService accountService;

    private final Set<String> validAccountTypes = Set.of("buyer", "seller");

    /**
     * Register customer.
     *
     * @param accountDTO the user dto
     *
     * @return the response entity
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody AccountDTO accountDTO) {
        final String successMessage = accountService.registerNewCustomer(accountDTO);
        return new ResponseEntity<>(successMessage, HttpStatus.OK);
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<String> updateCustomer(@PathVariable String email, @Valid @RequestBody UpdateAccountDTO updateAccountDTO) {
        final String oldPassword = updateAccountDTO.getOldPassword();
        final String newPassword = updateAccountDTO.getNewPassword();
        final String accountType = updateAccountDTO.getAccountType();

        if(accountType != null && validAccountTypes.contains(accountType)){
            final String successMsg = accountService.updateCustomerDetails(accountType, email, oldPassword, newPassword);
            if (successMsg != null) {
                return new ResponseEntity<>(successMsg, HttpStatus.OK);
            } else {
                throw new InvalidException("The old password did not match.");
            }
        }
        throw new InvalidException("Account type is not mentioned properly.");
    }


//    @GetMapping("/login/buyer")
//    public ResponseEntity<String> loginCustomer(@Valid @RequestBody UserDTO userDTO) {
//
//
//
//
//        final String registeredWithEmailID = accountService.registerNewCustomer(accountDTO);
//        final String successMessage = "You are successfully registered as customer with Email Id:" + registeredWithEmailID;
//        return new ResponseEntity<>(successMessage, HttpStatus.OK);
//    }
//
//    @GetMapping("/login/seller")
//    public ResponseEntity<String> loginSeller(@Valid @RequestBody UserDTO userDTO) {
//
//
//
//
//        final String registeredWithEmailID = accountService.registerNewCustomer(accountDTO);
//        final String successMessage = "You are successfully registered as customer with Email Id:" + registeredWithEmailID;
//        return new ResponseEntity<>(successMessage, HttpStatus.OK);
//    }

    /**
     * Register customer 1 response entity.
     *
     * @param id the id
     * @param accountDTO the user dto
     *
     * @return the response entity
     */
    @PostMapping("/exp/{id}")
    public ResponseEntity<AccountDTO> registerCustomer1(@PathVariable int id, @RequestBody AccountDTO accountDTO) {
        if (id == 1) {
            throw new ServiceException("error");
        }

        return new ResponseEntity<>(accountDTO, HttpStatus.OK);

    }

    /**
     * Get hello world response entity.
     *
     * @return the response entity
     */
    @GetMapping("/demo")
    public ResponseEntity<String> getHelloWorld() {
        throw new InvalidException("hoo");
    }
}
