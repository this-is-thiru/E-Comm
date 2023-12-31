package com.mine.ecomm.cardservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mine.ecomm.cardservice.dto.CardDTO;
import com.mine.ecomm.cardservice.entity.Card;
import com.mine.ecomm.cardservice.exception.MetadataException;
import com.mine.ecomm.cardservice.exception.ServiceException;
import com.mine.ecomm.cardservice.service.CardInfoService;

import jakarta.validation.Valid;

/**
 * The type Card info controller.
 */
@CrossOrigin
@RestController
@RequestMapping("/card")
public class CardInfoController {

    @Autowired
    private CardInfoService cardInfoService;

    /**
     * Add card response entity.
     *
     * @param cardDTO the card dto
     *
     * @return the response entity
     */
    @PostMapping("/add")
    public ResponseEntity<String> addCard(final @Valid @RequestBody CardDTO cardDTO) {
	try {
	    final Boolean response = cardInfoService.addCard(cardDTO);
	    final String message = response ? "Card added successfully." : "Card already present.";
	    return new ResponseEntity<>(message, HttpStatus.OK);
	} catch (final ServiceException e) {
	    throw new MetadataException(e.getMessage());
	}
    }

    /**
     * Add card response entity.
     *
     * @param email the email
     *
     * @return the response entity
     */
    @GetMapping("/all/{email}")
    public ResponseEntity<List<Card>> addCard(final @PathVariable("email") String email) {
	try {
	    final List<Card> cards = cardInfoService.getAllCards(email);
	    return new ResponseEntity<>(cards, HttpStatus.OK);
	} catch (final ServiceException e) {
	    throw new MetadataException(e.getMessage());
	}
    }
}
