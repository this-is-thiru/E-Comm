package com.mine.ecomm.addressservice.dto;

import java.util.ArrayList;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

/**
 * The type Address dto.
 */
@Data
public class AddressDTO {
    private String id;
    private String dno;
    private String street;
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s]*$", message = "City name should not contain numeric and special characters. Only spaces are allowed.")
    private String city;
    private int stateId;

    /**
     * ^ represents the starting of the number.
     * [1-9]{1} represents the starting digit in the pin code ranging from 1 to 9.
     * [0-9]{2} represents the next two digits in the pin code ranging from 0 to 9.
     * \\s{0, 1} represents the white space in the pin code that can occur once or never.
     * [0-9]{3} represents the last three digits in the pin code ranging from 0 to 9.
     * $ represents the ending of the number.
     */
    @Pattern(regexp = "^[1-9][0-9]{2}\\s?[0-9]{3}$", message = "Enter valid pin code.")
    private String pinCode;
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Enter valid phone number.")
    private String phoneNumber;

    @Getter
    private final static ArrayList<String> states = new ArrayList<>();

    static {
        states.add("Andhra Pradesh");
        states.add("Arunachal Pradesh");
        states.add("Assam");
        states.add("Bihar");
        states.add("Chhattisgarh");
        states.add("Goa");
        states.add("Gujarat");
        states.add("Haryana");
        states.add("Himachal Pradesh");
        states.add("Jammu and Kashmir");
        states.add("Jharkhand");
        states.add("Karnataka");
        states.add("Kerala");
        states.add("Madhya Pradesh");
        states.add("Maharashtra");
        states.add("Manipur");
        states.add("Meghalaya");
        states.add("Mizoram");
        states.add("Nagaland");
        states.add("Odisha");
        states.add("Punjab");
        states.add("Rajasthan");
        states.add("Sikkim");
        states.add("Tamil Nadu");
        states.add("Telangana");
        states.add("Tripura");
        states.add("Uttar Pradesh");
        states.add("Uttarakhand");
        states.add("West Bengal");
    }

}
