package org.example.service;

import org.example.colors.StringColors;
import org.example.dto.*;
import org.example.enums.Status;
import org.example.enums.TransactionType;
import org.example.repository.CardRepository;
import org.example.repository.TerminalRepository;
import org.example.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class TransactionService {
    TransactionRepository transactionRepository = new TransactionRepository();
    CardRepository cardRepository = new CardRepository();
    CardService cardService = new CardService();
    TerminalRepository terminalRepository = new TerminalRepository();

    public void makeTransaction(ProfileDTO profile) {
        System.out.println();

        List<String> cardNumbers = new LinkedList<>();

        List<CardDTO> cardList = cardRepository.getCardList();
        List<CardDTO> cards = new LinkedList<>();
        if (cardList != null) {
            for (CardDTO cardDTO : cardList) {
                if (cardDTO.getPhone() != null) {
                    if (cardDTO.getPhone().equals(profile.getPhone())) {
                        cards.add(cardDTO);
                        cardNumbers.add(cardDTO.getNumber());
                    }
                }
            }
        }
        if (cards.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tYou have no cards" + StringColors.ANSI_RESET);
            return;
        }


        List<TransactionDTO> transactions = transactionRepository.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        boolean bool = false;
        for (TransactionDTO transaction : transactions) {
            if (cardNumbers.contains(transaction.getCard_number())) {
                TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
                if (terminal != null) {
                    if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                        bool = true;
                        System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                    } else {
                        bool = true;
                        System.out.println(StringColors.GREEN + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                    }
                }
            }
        }
        if (!bool) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
        }
        System.out.println();

    }


    public void makePayment(String cardNumber, String terminalCode, String campanyCardNumber) {
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
                if (terminal.getStatus().equals(Status.NO_ACTIVE)) {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal error " + StringColors.ANSI_RESET);
                    return;
                }
                if (card.getBalance() >= 1700) {
                    TerminalDTO terminal1 = terminalRepository.getTerminalByCode(terminalCode);
                    if (terminal1.getAddress() != null && terminal1.getStatus().equals(Status.ACTIVE)) {
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
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere are insufficient funds on the card " + StringColors.ANSI_RESET);
                }
            } else {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error with the terminal " + StringColors.ANSI_RESET);
            }

        } else {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not found " + StringColors.ANSI_RESET);
        }
    }

    public void showTransactions() {
        List<TransactionDTO> transactions = transactionRepository.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        boolean bool = false;
        for (TransactionDTO transaction : transactions) {
                TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
                if (terminal != null) {
                    if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                        bool = true;
                        System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                    } else {
                        bool = true;
                        System.out.println(StringColors.GREEN + "Card number " + transaction.getCard_number() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                    }
                }

        }
    }

    public void showTodaysTransactions() {
        List<TransactionDTO> transactions = transactionRepository.getTodaysTransactions();

        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        boolean bool = false;
        for (TransactionDTO transaction : transactions) {
            TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
            if (terminal != null) {
                if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                    bool = true;
                    System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                }
            }

        }
    }

    public void showInterimPayments(LocalDate localDate1,LocalDate localDate2) {
        List<TransactionDTO> transactions = transactionRepository.getInterimPayments(localDate1, localDate2);
        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        for (TransactionDTO transaction : transactions) {
            TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
            if (terminal != null) {
                if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                    System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                }
            }

        }
    }

    public void showDailyPayments(LocalDate localDate) {
        List<TransactionDTO> transactions = transactionRepository.getDailyPayments(localDate);

        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        for (TransactionDTO transaction : transactions) {
            TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
            if (terminal != null) {
                if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                    System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                }
            }

        }

    }

    public void showTransactionsByTerminal(String terminalCode) {
        List<TransactionDTO> transactions = transactionRepository.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        boolean bool=false;
        for (TransactionDTO transaction : transactions) {
            TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
            if (terminal != null&&terminal.getCode()!=null) {
                if (terminal.getCode().equals(terminalCode)) {
                    if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                        bool = true;
                        System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode() + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                    }
                }
            }

        }
        if (!bool) {
            System.out.println(StringColors.RED+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions were found for this reference"+StringColors.ANSI_RESET);
        }
    }

    public void showTransactionsByCard(String cardNumber) {
        List<TransactionDTO> transactions = transactionRepository.getTransactions();

        if (transactions.isEmpty()) {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions available" + StringColors.ANSI_RESET);
            return;
        }
        boolean bool=false;
        for (TransactionDTO transaction : transactions) {
            TerminalDTO terminal = terminalRepository.getTerminalByCode(transaction.getTerminal_code());
            if (terminal != null&&transaction.getCard_number().equals(cardNumber)) {
                if (transaction.getTransactionType().equals(TransactionType.PAYMENT)) {
                    bool=true;
                    System.out.println(StringColors.WHITE + "Card number " + transaction.getCard_number() + "; Terminal code: " + terminal.getCode()  + "; Address " + terminal.getAddress() + "; Amount " + transaction.getAmount() + "; Time " + transaction.getTransactionTime() + "; Transaction type " + transaction.getTransactionType() + "; " + StringColors.ANSI_RESET);
                }
            }

        }
        if (!bool) {
            System.out.println(StringColors.RED+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tNo transactions were found for this reference"+StringColors.ANSI_RESET);
        }
    }
}
