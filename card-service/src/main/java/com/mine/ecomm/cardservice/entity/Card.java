package com.mine.ecomm.cardservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;

/**
 * The type Card.
 */
@Entity
@Data
public class Card {

    @Id
    private String cardNumber;

    private int expMonth;

    private int expYear;

    private String name;

    @Email
    private String email;
}
