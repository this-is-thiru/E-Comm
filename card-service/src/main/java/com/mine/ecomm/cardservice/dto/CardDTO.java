package com.mine.ecomm.cardservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The type Card dto.
 */
@Data
public class CardDTO {

    @Pattern(regexp = "^[1-9][0-9]{15}$", message = "Enter valid card number.")
    private String cardNumber;

    @Max(value = 12, message = "Enter valid month.")
    private int expMonth;

    @Min(value = 2000, message = "Enter valid year.")
    private int expYear;

    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s]*$", message = "Enter valid name.")
    private String name;

    @Email
    private String email;

}
