package com.mine.ecomm.authservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsEntity {
    @Id
    private String username;
    private String password;
    private String userType;
    private String roles;
}
