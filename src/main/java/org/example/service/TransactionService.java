package org.example.service;

import org.example.colors.StringColors;
import org.example.dto.CardDTO;
import org.example.dto.ResponsDTO;
import org.example.dto.TerminalDTO;
import org.example.repository.CardRepository;
import org.example.repository.TerminalRepository;
import org.example.repository.TransactionRepository;

public class TransactionService {
    TransactionRepository transactionRepository = new TransactionRepository();
    CardRepository cardRepository = new CardRepository();
    CardService cardService = new CardService();
    TerminalRepository terminalRepository = new TerminalRepository();

    public void makeTransaction(String senderCardNumber, String address, double amount) {
        if (senderCardNumber.trim().isEmpty() || address.trim().isEmpty() || amount <= 0) {
            System.out.println();
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tData entry error" + StringColors.ANSI_RESET);
            System.out.println();
            return;
        }
        CardDTO card = cardRepository.getCardByNumber(senderCardNumber);

        if (card != null) {
            if (card.getBalance() >= amount) {
                ResponsDTO result = transactionRepository.makeTransaction(senderCardNumber, address, amount);
                if (result.success()) {
                    cardRepository.getMoneyFromCardBalance(amount, senderCardNumber);
                    System.out.println(StringColors.YELLOW + result.message() + StringColors.ANSI_RESET);
                } else {
                    System.out.println(StringColors.RED + result.message() + StringColors.ANSI_RESET);
                }
            } else {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere are insufficient funds on the card " + StringColors.ANSI_RESET);
            }

        } else {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not found " + StringColors.ANSI_RESET);
        }

    }

    public void makePayment(String cardNumber, String terminalCode,String campanyCardNumber) {
        if (cardNumber.trim().isEmpty() || terminalCode.trim().isEmpty()) {
            System.out.println();
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tData entry error" + StringColors.ANSI_RESET);
            System.out.println();
            return;
        }
        CardDTO card = cardRepository.getCardByNumber(cardNumber);
        CardDTO campanyCard = cardRepository.getCardByNumber(campanyCardNumber);
        TerminalDTO terminal = terminalRepository.getTerminalByCode(terminalCode);
        if (card != null && campanyCard != null) {
            if (terminal != null) {
                if (card.getBalance() >= 1700) {
                    ResponsDTO result = transactionRepository.makePayment(cardNumber, terminalCode);
                    if (result.success()) {
                        cardRepository.getMoneyFromCardBalance(1700.0, cardNumber);
                        cardRepository.setMoneyToCampanyBalance(1700.0, campanyCard.getNumber()).message();
                        System.out.println(StringColors.YELLOW + result.message() + StringColors.ANSI_RESET);
                    } else {
                        System.out.println(StringColors.RED + result.message() + StringColors.ANSI_RESET);
                    }
                } else {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere are insufficient funds on the card " + StringColors.ANSI_RESET);
                }
            } else {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error with the terminal " + StringColors.ANSI_RESET);
            }

        } else {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not found " + StringColors.ANSI_RESET);
        }
    }
}
