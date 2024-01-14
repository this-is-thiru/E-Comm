package com.mine.ecomm.accountservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mine.ecomm.accountservice.dto.CreateAccountRequest;
import com.mine.ecomm.accountservice.dto.UpdateAccountRequest;
import com.mine.ecomm.accountservice.entity.AccountEntity;
import com.mine.ecomm.accountservice.entity.Buyer;
import com.mine.ecomm.accountservice.entity.Seller;
import com.mine.ecomm.accountservice.exception.ServiceException;
import com.mine.ecomm.accountservice.repository.BuyerRepository;
import com.mine.ecomm.accountservice.repository.SellerRepository;
import com.mine.ecomm.accountservice.validator.AccountValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final BuyerRepository buyerRepository;
    private final SellerRepository sellerRepository;

    private static final String BUYER = "buyer";

    public String registerNewCustomer(CreateAccountRequest createAccountRequest) {
        AccountValidator.validateCustomer(createAccountRequest);
        Boolean emailAvailable = checkAvailabilityOfEmailId(createAccountRequest.getEmailId().toLowerCase());
        Boolean phoneNumberAvailable = checkAvailabilityOfPhoneNumber(createAccountRequest.getPhoneNumber());
        if (emailAvailable) {
            if (phoneNumberAvailable) {
                String emailIdToDB = createAccountRequest.getEmailId().toLowerCase();
                createAccountRequest.setEmailId(emailIdToDB);
                if ("buyer".equals(createAccountRequest.getAccountType())) {
                    return saveAccount(createAccountRequest, new Buyer());
                } else if ("seller".equals(createAccountRequest.getAccountType())) {
                    return saveAccount(createAccountRequest, new Seller());
                }
                throw new ServiceException("The user type to register is missing");
            } else {
                throw new ServiceException("The phone number is already in use, Please try with a new phone number");
            }
        } else {
            throw new ServiceException("The email id is already in use. Please try with a new email id");
        }
    }

    private String saveAccount(CreateAccountRequest createAccountRequest, AccountEntity account){
        account.setEmailId(createAccountRequest.getEmailId());
        account.setName(createAccountRequest.getName().trim());
        account.setPassword(createAccountRequest.getPassword());
        account.setPhoneNumber(createAccountRequest.getPhoneNumber());

        if (account instanceof Buyer buyer){
            buyerRepository.saveAndFlush(buyer);
            System.out.println(buyer);
            return createAccountRequest.getEmailId() + "is successfully registered as buyer.";
        }
        if (account instanceof Seller seller) {
            sellerRepository.saveAndFlush(seller);
            System.out.println(seller);
            return createAccountRequest.getEmailId() + "is successfully registered as seller.";
        }
        return null;
    }

    public String updateCustomerDetails(final String emailId, final UpdateAccountRequest accountRequest) {
        final String accountType = accountRequest.getAccountType();
        final String oldPassword = accountRequest.getOldPassword();
        final String newPassword = accountRequest.getNewPassword();
        if (BUYER.equals(accountType)){
            final Optional<Buyer> optionalSeller = buyerRepository.findById(emailId);
            if (optionalSeller.isPresent()){
                final Buyer buyer = optionalSeller.get();
                final String accountPassword = buyer.getPassword();
                if (accountPassword.equals(oldPassword)) {
                    buyer.setPassword(newPassword);
                    buyerRepository.saveAndFlush(buyer);
                    return "The password is updated successfully.";
                }
            }
        } else {
            final Optional<Seller> optionalSeller = sellerRepository.findById(emailId);
            if (optionalSeller.isPresent()){
                final Seller seller = optionalSeller.get();
                final String accountPassword = seller.getPassword();
                if (accountPassword.equals(oldPassword)) {
                    seller.setPassword(newPassword);
                    sellerRepository.saveAndFlush(seller);
                    return "The password is updated successfully.";
                }
            }
        }
        return null;
    }

    public Boolean checkAvailabilityOfEmailId(String emailId) {
        Optional<Buyer> optionalAccount = buyerRepository.findById(emailId);
        return optionalAccount.isEmpty();
    }

    public Boolean checkAvailabilityOfPhoneNumber(String phoneNumber) {
        List<Buyer> list = buyerRepository.findByPhoneNumber(phoneNumber);
        return list.isEmpty();
    }
}
