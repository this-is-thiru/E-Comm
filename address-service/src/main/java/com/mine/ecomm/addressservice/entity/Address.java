package com.mine.ecomm.addressservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * The type Address.
 */
@Entity
@Data
public class Address {
    @Id
    private String id;
    private String dno;
    private String street;
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s]*$", message = "City name should not contain numeric and special characters. Only spaces are allowed.")
    private String city;
    private String state;

    @Override
    public String toString() {
        return "Address Added successfully: {" + "id='" + id + '\'' + ", dno='" + dno + '\'' + ", street='" + street + '\'' + ", city='" + city + '\'' + ", state='" + state + '\'' + ", pinCode='" + pinCode + '\'' + ", phoneNumber='" + phoneNumber + '\'' + '}';
    }

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
    private String phoneNumber;

    /**
     * Sets id.
     */
    public void setId() {
        final LocalDateTime time = LocalDateTime.now();
        this.id = time.toString();
    }
}
