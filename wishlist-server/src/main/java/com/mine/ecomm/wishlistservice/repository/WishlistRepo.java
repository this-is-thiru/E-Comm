package com.mine.ecomm.wishlistservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.wishlistservice.entity.Wishlist;

public interface WishlistRepo extends JpaRepository<Wishlist, String> {
}
