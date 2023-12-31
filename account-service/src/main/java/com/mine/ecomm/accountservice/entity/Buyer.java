package com.mine.ecomm.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;


@Entity(name = "Buyers")
@Data
@ToString
public class Buyer implements AccountEntity {
    @Id
    private String emailId;

    private String name;

    private String password;

    private String phoneNumber;
}
