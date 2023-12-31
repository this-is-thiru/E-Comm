package com.mine.ecomm.accountservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.ToString;


@Entity(name = "Sellers")
@Data
@ToString
public class Seller implements AccountEntity {
    @Id
    private String emailId;

    private String name;

    private String password;

    private String phoneNumber;
}
