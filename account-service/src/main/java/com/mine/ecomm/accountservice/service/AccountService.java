package com.mine.ecomm.accountservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mine.ecomm.accountservice.dto.AccountDTO;
import com.mine.ecomm.accountservice.entity.AccountEntity;
import com.mine.ecomm.accountservice.entity.Buyer;
import com.mine.ecomm.accountservice.entity.Seller;
import com.mine.ecomm.accountservice.exception.ServiceException;
import com.mine.ecomm.accountservice.repository.BuyerRepository;
import com.mine.ecomm.accountservice.repository.SellerRepository;
import com.mine.ecomm.accountservice.validator.AccountValidator;

@Service
public class AccountService {

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;

    public String registerNewCustomer(AccountDTO accountDTO) {
        AccountValidator.validateCustomer(accountDTO);
        Boolean emailAvailable = checkAvailabilityOfEmailId(accountDTO.getEmailId().toLowerCase());
        Boolean phoneNumberAvailable = checkAvailabilityOfPhoneNumber(accountDTO.getPhoneNumber());
        if (emailAvailable) {
            if (phoneNumberAvailable) {
                String emailIdToDB = accountDTO.getEmailId().toLowerCase();
                accountDTO.setEmailId(emailIdToDB);
                if ("buyer".equals(accountDTO.getAccountType())) {
                    return saveAccount(accountDTO, new Buyer());
                } else if ("seller".equals(accountDTO.getAccountType())) {
                    return saveAccount(accountDTO, new Seller());
                }
                throw new ServiceException("The user type to register is missing");
            } else {
                throw new ServiceException("The phone number is already in use, Please try with a new phone number");
            }
        } else {
            throw new ServiceException("The email id is already in use. Please try with a new email id");
        }
    }

    private String saveAccount(AccountDTO accountDTO, AccountEntity account){
        account.setEmailId(accountDTO.getEmailId());
        account.setName(accountDTO.getName().trim());
        account.setPassword(accountDTO.getPassword());
        account.setPhoneNumber(accountDTO.getPhoneNumber());

        if (account instanceof Buyer){
            Buyer buyer = (Buyer) account;
            buyerRepository.saveAndFlush(buyer);
            System.out.println(buyer);
            return accountDTO.getEmailId() + "is successfully registered as buyer.";
        }
        if (account instanceof Seller) {
            Seller seller = (Seller) account;
            sellerRepository.saveAndFlush(seller);
            System.out.println(seller);
            return accountDTO.getEmailId() + "is successfully registered as seller.";
        }
        return null;
    }

    public String updateCustomerDetails(final String accountType, final String emailId, final String oldPassword, final String newPassword) {
        if ("buyer".equals(accountType)){
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

//    public boolean isValidBuyer(UserDTO userDTO) {
//
//    }
//
//    public boolean isValidSeller(UserDTO userDTO) {
//
//    }

}
