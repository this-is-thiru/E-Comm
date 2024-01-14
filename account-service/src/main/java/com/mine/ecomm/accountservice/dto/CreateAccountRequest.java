package com.mine.ecomm.accountservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The type Account dto.
 */
@Data
public class CreateAccountRequest {
    @Email(message = "email should be in correct format.")
    private String emailId;
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s]*$", message = "Enter the valid name. name should not contain numeric and special characters.")
    private String name;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$",
            message = "Password should contains Uppercase, Lowercase, Numeric, Special characters.")
    private String password;
    private String confirmPassword;
    private String phoneNumber;

    // equals check using validation constraints
    @Pattern(regexp = "^(buyer|seller)$", message = "Account type should be buyer or seller.")
    private String accountType;
}
