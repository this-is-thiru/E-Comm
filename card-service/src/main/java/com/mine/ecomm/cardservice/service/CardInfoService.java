package com.mine.ecomm.cardservice.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mine.ecomm.cardservice.dto.CardDTO;
import com.mine.ecomm.cardservice.entity.Card;
import com.mine.ecomm.cardservice.exception.ServiceException;
import com.mine.ecomm.cardservice.repository.CardRepository;

/**
 * The type Card info service.
 */
@Service
public class CardInfoService {

    @Autowired
    private CardRepository cardRepository;

    /**
     * Add card to the database.
     *
     * @param cardDTO the card dto
     *
     * @return the boolean
     *
     * @throws ServiceException the service exception
     */
    public Boolean addCard(final CardDTO cardDTO) throws ServiceException {
        boolean isCardAdded = false;
        if (validationOfCardDetails(cardDTO)) {
            final Optional<Card> optionalCard = cardRepository.findById(cardDTO.getCardNumber());
            if (optionalCard.isEmpty()) {
                final Card card = new Card();
                card.setCardNumber(cardDTO.getCardNumber());
                card.setExpMonth(cardDTO.getExpMonth());
                card.setExpYear(cardDTO.getExpYear());
                card.setName(cardDTO.getName());
                card.setEmail(cardDTO.getEmail());
                cardRepository.saveAndFlush(card);
                isCardAdded = true;
            }
        }
        return isCardAdded;
    }

    private Boolean validationOfCardDetails(final CardDTO cardDTO) throws ServiceException {
        final String cardNumber = cardDTO.getCardNumber();
        final int expMonth = cardDTO.getExpMonth();
        final int expYear = cardDTO.getExpYear();
        if (validateCardNumber(cardNumber)) {
            if (validateCardExpireDetails(expMonth, expYear)) {
                return true;
            }
            throw new ServiceException("check card's expiry month and year.");
        }
        throw new ServiceException("enter valid card number.");
    }

    /**
     * Validation card by using Luhn's Algorithm.
     *
     * @param cardNumber the card number
     *
     * @return the boolean
     */
    private Boolean validateCardNumber(final String cardNumber) {
        if (cardNumber.length() != 16) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 16; i++) {
            final String digitAsString = String.valueOf(cardNumber.charAt(i));
            int num;
            if (i % 2 == 0) {
                num = performDoubleOperation(digitAsString);

            } else {
                num = Integer.parseInt(digitAsString);
            }
            sum += num;
        }
        return sum % 10 == 0;
    }

    /**
     * perform doubling operation on every 2nd digit of card from last.
     *
     * @param digitAsString the digit
     *
     * @return the int the digit after 2x
     */
    private int performDoubleOperation(final String digitAsString) {
        int doubledDigit = Integer.parseInt(digitAsString) * 2;
        if (doubledDigit > 9) {
            // TODO: To be write the logic here
            int temp = 0;
            while (doubledDigit > 0) {
                temp += doubledDigit % 10;
                doubledDigit /= 10;
            }
            return temp;
        } else {
            return doubledDigit;
        }
    }

    /**
     * Validation of card expire month and year.
     *
     * @param month the expiry month
     * @param year the expiry year
     *
     * @return the boolean is valid month and year
     */
    private Boolean validateCardExpireDetails(final int month, final int year) {
        final LocalDate today = LocalDate.now();
        final int presentMonth = today.getMonthValue();
        final int presentYear = today.getYear();
        return year > presentYear || (year == presentYear && month > presentMonth);
    }

    /**
     * Gets all cards.
     *
     * @param email the email
     *
     * @return the all cards
     *
     * @throws ServiceException the service exception
     */
    public List<Card> getAllCards(final String email) throws ServiceException {
        final List<Card> cards = cardRepository.findByEmail(email);
        if (cards.isEmpty()) {
            throw new ServiceException("No cards is added.");
        }
        return cards;
    }
}
