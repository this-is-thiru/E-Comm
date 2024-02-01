package com.mine.ecomm.wishlistservice.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "WISHLIST")
public class Wishlist {
    @Id
    @Column(name = "buyer_email")
    @SuppressWarnings("JpaDataSourceORMInspection")
    private String buyerEmail;

    @OneToMany(mappedBy = "wishlist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Product> products;
}
