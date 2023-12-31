package com.mine.ecomm.cardservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mine.ecomm.cardservice.entity.Card;

/**
 * The interface Card repository.
 */
public interface CardRepository extends JpaRepository<Card, String> {
    /**
     * Find by email list.
     *
     * @param email the email
     *
     * @return the list
     */
    List<Card> findByEmail(String email);
}
