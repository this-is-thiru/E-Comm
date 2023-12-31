package com.mine.ecomm.accountservice.validator;

import com.mine.ecomm.accountservice.dto.AccountDTO;
import com.mine.ecomm.accountservice.exception.InvalidException;

public class AccountValidator {

    public static void validateCustomer(AccountDTO accountDTO) throws InvalidException {

        if (!validateEmailId(accountDTO.getEmailId()))
            throw new InvalidException("The format of email id entered is incorrect.");

        if (!validatePhoneNumber(accountDTO.getPhoneNumber()))
            throw new InvalidException("The customer's phone number should contain 10 digits.");

        if (!validateName(accountDTO.getName()))
            throw new InvalidException("The customer's name is not in proper format.");

        if (!validatePassword(accountDTO.getPassword()))
            throw new InvalidException("Password should have at least one upper case, " +
                    "one lower case, one number, one special character and should be of 6 to 20 characters.");

    }

    public static Boolean validateName(String name) {
        Boolean flag = false;
        if (!name.matches("[ ]*") && name.matches("([A-Za-z])+(\\s[A-Za-z]+)*"))
            flag = true;
        return flag;
    }

    public static Boolean validatePhoneNumber(String phoneNumber) {
        Boolean flag = false;
        if (phoneNumber.matches("[0-9]+") && phoneNumber.length() == 10)
            flag = true;
        return flag;
    }


    public static Boolean validateEmailId(String emailId) {
        Boolean flag = false;
        if (emailId.matches("[a-zA-Z0-9._]+@[a-zA-Z]{2,}\\.[a-zA-Z][a-zA-Z.]+"))
            flag = true;
        return flag;
    }


    public static Boolean validatePassword(String password) {
        Boolean flag = false;
        if (password.length() >= 6 && password.length() <= 20)
            if (password.matches(".*[A-Z]+.*"))
                if (password.matches(".*[a-z]+.*"))
                    if (password.matches(".*[0-9]+.*"))
                        if (password.matches(".*[^a-zA-Z-0-9].*"))
                            flag = true;
        return flag;
    }
}
