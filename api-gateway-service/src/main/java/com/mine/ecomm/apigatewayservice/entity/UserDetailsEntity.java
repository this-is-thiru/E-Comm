package com.mine.ecomm.apigatewayservice.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsEntity implements Persistable<String> {
    @Id
    private String username;
    private String password;
    private String userType;
    private String roles;

    @Transient
    private boolean newProduct;

    @Override
    public String getId() {
        return username;
    }

    @Override
    public boolean isNew() {
        return this.newProduct;
    }

    public void setNewFlag() {
        this.newProduct = true;
    }
}
