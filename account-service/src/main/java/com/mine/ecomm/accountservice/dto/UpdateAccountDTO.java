package com.mine.ecomm.accountservice.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The type Update account dto.
 */
@Data
public class UpdateAccountDTO {
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$", message = "Password should contains Uppercase, Lowercase, Numeric, Special characters.")
    private String oldPassword;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,}$", message = "Password should contains Uppercase, Lowercase, Numeric, Special characters.")
    private String newPassword;

    private String confirmPassword;

    private String accountType;

}
