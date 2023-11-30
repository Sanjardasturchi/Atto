package org.example.service;

import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.enums.Status;
import org.example.repository.CardRepository;

import java.util.LinkedList;
import java.util.List;

public class CardService {
    CardRepository cardRepository = new CardRepository();

    public boolean createCard(CardDTO card) {
        List<CardDTO> cardList = getCardList();
        if (cardList != null) {
            for (CardDTO cardDTO : cardList) {
                if (cardDTO.getNumber().equals(card.getNumber())) {
                    System.out.println("Card is available !!!");
                    return false;
                }
            }
        }
        boolean result = cardRepository.createCard(card);
        if (result) {
            System.out.println("Card created successfuly 👌👌👌");
        } else {
            System.out.println("An error occurred while creating the card !!!");
        }
        return result;
    }

    public List<CardDTO> getCardList() {
        List<CardDTO> cardList = cardRepository.getCardList();
        return cardList;
    }

    public List<CardDTO> getOwnCards(ProfileDTO profile) {
        List<CardDTO> cards = new LinkedList<>();
        List<CardDTO> cardList = getCardList();
        for (CardDTO cardDTO : cardList) {
            if (cardDTO.getStatus().equals(Status.ACTIVE)) {
                if (cardDTO.getPhone().equals(profile.getPhone())) {
                    cards.add(cardDTO);
                }
            }
        }
        return cards;
    }

    public boolean addCard(ProfileDTO profile, String cardNumber) {
        List<CardDTO> cardList = getCardList();
        CardDTO card = new CardDTO();
        for (CardDTO dto : cardList) {
            if (dto.getStatus().equals(Status.NO_ACTIVE)) {
                if (dto.getNumber() != null) {
                    if (dto.getNumber().equals(cardNumber)) {
                        boolean res = cardRepository.addCard(dto.getNumber(), profile.getPhone());
                        return res;
                    }
                }
            }
        }
        return false;
    }
}
