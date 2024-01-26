package com.mine.ecomm.wishlistservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "WISHLISTS")
public class Wishlist {
    @Id
    private String buyerEmail;

    @OneToMany(mappedBy = "buyerEmail")
    private List<Product> products;
}
