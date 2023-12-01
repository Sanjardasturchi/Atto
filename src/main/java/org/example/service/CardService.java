package org.example.service;

import org.example.colors.BackgroundColors;
import org.example.colors.StringColors;
import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.dto.ResponsDTO;
import org.example.enums.Status;
import org.example.repository.CardRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class CardService {

    CardRepository cardRepository = new CardRepository();



    public boolean createCard(CardDTO card) {
        List<CardDTO> cardList = getCardList();
        if (cardList != null) {
            for (CardDTO cardDTO : cardList) {
                if (cardDTO.getNumber().equals(card.getNumber())) {
                    System.out.println(BackgroundColors.WHITE_BACKGROUND + "Card is available !!!" + StringColors.ANSI_RESET);
                    return false;
                }
            }
        }
        boolean result = cardRepository.createCard(card);
        if (result) {
            System.out.println(BackgroundColors.YELLOW_BACKGROUND + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard created successfuly 👌👌👌" + StringColors.ANSI_RESET);
        } else {
            System.out.println(BackgroundColors.RED_BACKGROUND + "An error occurred while creating the card !!!" + StringColors.ANSI_RESET);
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
            if (cardDTO.getExp_date() != null) {
                if (cardDTO.getPhone() != null) {
                    if (cardDTO.getPhone().equals(profile.getPhone())) {
                        cards.add(cardDTO);
                    }
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

    public boolean changeCardStatus(ProfileDTO profile, String cardNumber) {
        if (cardNumber.trim().isEmpty()) {
            System.out.println(BackgroundColors.RED_BACKGROUND + """
                                        \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not founded
                    """ + StringColors.ANSI_RESET);
            return false;
        }
        ResponsDTO result = cardRepository.changeStatus(cardNumber, profile);
        if (result.success()) {
            System.out.println(BackgroundColors.YELLOW_BACKGROUND + result.message() + "\n" + StringColors.ANSI_RESET);
        } else {
            System.out.println(BackgroundColors.RED_BACKGROUND + result.message() + "\n" + StringColors.ANSI_RESET);
        }
        return result.success();
    }

    public void deleteCard(ProfileDTO profile, String cardNumber) {
        ResponsDTO result = cardRepository.delete(cardNumber, profile);
        if (result.success()) {
            System.out.println(BackgroundColors.YELLOW_BACKGROUND + result.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(BackgroundColors.RED_BACKGROUND + result.message() + StringColors.ANSI_RESET);
        }

    }

    public void reFillCard(ProfileDTO profile, String cardNumber, double amount) {
        ResponsDTO result = cardRepository.reFill(cardNumber, profile, amount);

        if (result.success()) {
            System.out.println(StringColors.YELLOW + result.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + result.message() + StringColors.ANSI_RESET);
        }
    }

    public void updateCard(String cardNumber, LocalDate expDate, ProfileDTO profile) {
        if (cardNumber.trim().isEmpty()) {
            System.out.println(BackgroundColors.RED_BACKGROUND + """
                                        \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not founded
                    """ + StringColors.ANSI_RESET);
            return;
        }
        ResponsDTO result = cardRepository.update(cardNumber, expDate, profile);
        if (result.success()) {
            System.out.println(BackgroundColors.YELLOW_BACKGROUND + result.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(BackgroundColors.RED_BACKGROUND + result.message() + StringColors.ANSI_RESET);
        }

    }

    public void changeProfileStatus(Status status, String phoneNumber) {
        ResponsDTO result = cardRepository.changeProfileStatus(status, phoneNumber);
        if (result.success()) {
            System.out.println(StringColors.YELLOW + result.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + result.message() + StringColors.ANSI_RESET);
        }
    }

    public void changeCardStatusByAdmin(String cardNumber, String status) {
        ResponsDTO result = cardRepository.changeStatusByAdmin(cardNumber, status);
        if (result.success()) {
            System.out.println(StringColors.YELLOW + result.message() + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + result.message() + StringColors.ANSI_RESET);
        }
    }

    public void deleteCardByAdmin(String cardNumber, String status) {
        ResponsDTO result = cardRepository.changeStatusByAdmin(cardNumber, status);
        if (result.success()) {
            System.out.println(StringColors.YELLOW + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard deleted" + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not found" + StringColors.ANSI_RESET);
        }
    }

}
