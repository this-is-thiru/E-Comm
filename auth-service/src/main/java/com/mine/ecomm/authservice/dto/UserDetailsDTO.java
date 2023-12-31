package com.mine.ecomm.authservice.dto;

import com.mine.ecomm.authservice.entity.UserDetailsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDTO {
    private String username;
    private String password;
    private String userType;
    private String roles;

    public UserDetailsEntity toUserDetailsEntity() {
        final UserDetailsEntity entity = new UserDetailsEntity();
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setUserType(userType);
        entity.setRoles(roles);
        return entity;
    }
}
