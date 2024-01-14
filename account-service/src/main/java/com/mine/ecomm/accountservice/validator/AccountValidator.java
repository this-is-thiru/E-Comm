package com.mine.ecomm.accountservice.validator;

import com.mine.ecomm.accountservice.dto.CreateAccountRequest;
import com.mine.ecomm.accountservice.exception.InvalidException;

public class AccountValidator {

    public static void validateCustomer(CreateAccountRequest createAccountRequest) throws InvalidException {

        if (!validateEmailId(createAccountRequest.getEmailId()))
            throw new InvalidException("The format of email id entered is incorrect.");

        if (!validatePhoneNumber(createAccountRequest.getPhoneNumber()))
            throw new InvalidException("The customer's phone number should contain 10 digits.");

        if (!validateName(createAccountRequest.getName()))
            throw new InvalidException("The customer's name is not in proper format.");

        if (!validatePassword(createAccountRequest.getPassword()))
            throw new InvalidException("Password should have at least one upper case, " +
                    "one lower case, one number, one special character and should be of 6 to 20 characters.");

    }

    public static Boolean validateName(String name) {
        return !name.matches("[ ]*") && name.matches("([A-Za-z])+(\\s[A-Za-z]+)*");
    }

    public static Boolean validatePhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]+") && phoneNumber.length() == 10;
    }


    public static Boolean validateEmailId(String emailId) {
        return emailId.matches("[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+");
    }


    public static Boolean validatePassword(String password) {
        boolean flag = false;
        if (password.length() >= 6 && password.length() <= 20)
            if (password.matches(".*[A-Z]+.*"))
                if (password.matches(".*[a-z]+.*"))
                    if (password.matches(".*[0-9]+.*"))
                        if (password.matches(".*[^a-zA-Z-0-9].*"))
                            flag = true;
        return flag;
    }
}
