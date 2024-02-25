package com.mine.ecomm.apigatewayservice.dto;

import com.mine.ecomm.apigatewayservice.entity.UserDetailsEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String userType;
    private String roles;

    public UserDetailsEntity toUserDetailsEntity() {
        final UserDetailsEntity entity = new UserDetailsEntity();
        entity.setUsername(username);
        entity.setPassword(passwordEncoder().encode(password));
        entity.setUserType(userType);
        entity.setRoles(roles);
        return entity;
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
