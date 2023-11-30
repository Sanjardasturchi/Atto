package org.example.controller;

import org.example.colors.BackgroundColors;
import org.example.colors.StringColors;
import org.example.db.DatabaseUtil;
import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.enums.ProfileRole;
import org.example.enums.Status;
import org.example.service.CardService;
import org.example.service.UserService;
import org.example.utils.ScannerUtils;

import java.time.LocalDate;
import java.util.List;

public class Controller {

    static ScannerUtils scanner = new ScannerUtils();
    static UserService userService = new UserService();
    CardService cardService = new CardService();


    public void start() {
        System.out.println();
        DatabaseUtil databaseUtil = new DatabaseUtil();
        databaseUtil.createProfileTable();
        databaseUtil.createCardTable();
        databaseUtil.createTerminalTable();
        databaseUtil.createTransactionTable();
        do {
            showMain();
            int action = getAction();
            switch (action) {
                case 1 -> {
                    login();
                }
                case 2 -> {
                    registration();
                }
            }
        } while (true);

    }

    private void registration() {

        String name = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + "Enter name:" + StringColors.ANSI_RESET);
//        String name = scanner.nextLineWithColor("Enter name:",BackgroundColors.BLUE_BACKGROUND,StringColors.RED);

        String surname = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + "Enter surname:" + StringColors.ANSI_RESET);
        String phone;
        String password;
        do {
            phone = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + "Enter phoneNumber: " + StringColors.ANSI_RESET);
            password = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + "Enter password: " + StringColors.ANSI_RESET);
        } while (phone == null || password == null);


        ProfileDTO profile = new ProfileDTO();
        profile.setName(name);
        profile.setSurname(surname);
        profile.setPhone(phone);
        profile.setPassword(password);
        profile.setProfileRole(ProfileRole.USER);


        boolean result = userService.registration(profile);
        if (!result) {
            System.out.println(BackgroundColors.GREEN_BACKGROUND + "Successful 👌👌👌" + StringColors.ANSI_RESET);
        } else {
            System.out.println(BackgroundColors.RED_BACKGROUND + StringColors.BLACK + "Error registration!!!" + StringColors.ANSI_RESET);
        }

    }

    private void login() {
        String phoneNumber = null;
        String password = null;
        do {
            phoneNumber = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter phoneNumber:  """ + StringColors.ANSI_RESET);
            password = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter password:  """ + StringColors.ANSI_RESET);
//            System.out.println();
        } while (phoneNumber.trim().isEmpty() || password.trim().isEmpty());
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setPhone(phoneNumber);
        profileDTO.setPassword(password);

        ProfileDTO profile = userService.login(profileDTO);
        if (profile == null) {
            System.out.println(BackgroundColors.RED_BACKGROUND + """
                                        \t\t\t\t\t\t\t\t\t\tNot found
                    """ + StringColors.ANSI_RESET);
            return;
        } else {
            if (profile.getStatus().equals(Status.NO_ACTIVE)) {
                System.out.println(BackgroundColors.RED_BACKGROUND + """
                                            \t\t\t\t\t\t\t\t\t\tNot found
                        """ + StringColors.ANSI_RESET);
                return;
            }
            if (profile.getProfileRole().equals(ProfileRole.USER)) {
                userMenu(profile);
            } else {
                adminMenu(profile);
            }
        }


    }

    private void adminMenu(ProfileDTO profile) {
        System.out.println("""
                1.Card
                2.Terminal
                3.Profile
                4.Transaction
                5.Statistic""");
        int option = scanner.nextInt("Choose option: ");
        switch (option) {
            case 1 -> {
                cardMenu(profile);
            }
            case 2 -> {
            }
            case 3 -> {
            }
            case 4 -> {
            }
            case 5 -> {
            }
            default -> {
                System.out.println("Wrong");
            }
        }
//        (Card)
//        1. Create Card(number,exp_date)
//        2. Card List
//        3. Update Card (number,exp_date)
//        4. Change Card status
//        5. Delete Card
//
//        (Terminal)
//                6. Create Terminal (code unique,address)
//        7. Terminal List
//        8. Update Terminal (code,address)
//        9. Change Terminal Status
//        10. Delete
//
//                (Profile)
//        11. Profile List
//        12. Change Profile Status (by phone)
//
//        (Transaction)
//                13. Transaction List
//        CardNumber, TerminalNumber, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        14. Company Card Balance
//                (card bo'ladi shu cardga to'lovlar tushadi. bu sql da insert qilinga bo'ladi)
//
//        (Statistic)
//                15. Bugungi to'lovlar
//        CardNumber, TerminalNumber, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        16. Kunlik to'lovlar (bir kunlik to'lovlar):
//        Enter Date: yyyy-MM-dd
//        CardNumber, TerminalNumber, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        17. Oraliq to'lovlar:
//        Enter FromDate: yyyy-MM-dd
//        Enter ToDate:   yyyy-MM-dd
//        18. Umumiy balance (company card dagi pulchalar)
//        19. Transaction by Terminal:
//        Enter terminal number:
//        20. Transaction By Card:
//        Enter Card number:
    }

    private void cardMenu(ProfileDTO profile) {
        System.out.println("""
                1. Create Card(number,exp_date)
                2. Card List
                3. Update Card (number,exp_date)
                4. Change Card status
                5. Delete Card
                0. Exit""");
        int option = scanner.nextInt("Choose option: ");

        switch (option) {
            case 0 -> {
                return;
            }
            case 1 -> {
                createCard(profile);
            }
            case 2 -> {
                showCards();
            }
            case 3 -> {
                updateCard(profile);
            }
            case 4 -> {
            }
            case 5 -> {
            }
            default -> {
                System.out.println("Wrong");
            }
        }

    }

    private void showCards() {
        List<CardDTO> cardList = cardService.getCardList();
        if (cardList != null) {
            for (CardDTO cardDTO : cardList) {
                System.out.println(cardDTO);
            }
        }
    }

    private void updateCard(ProfileDTO profile) {
//        3. Update Card (number,exp_date)
          String cardNumber = scanner.nextLine("Enter Card number: ");
          LocalDate expDate = scanner.nextLocalDate("Enter expiration date: ");

          cardService.updateCard(cardNumber,expDate,profile);

    }

    private void createCard(ProfileDTO profile) {
        String cardNumber;
        int year;
        do {
            cardNumber = scanner.nextLine("Enter Card number: ");
            year = scanner.nextInt("Enter the expiration date (3-10): ");
        } while (cardNumber.trim().isEmpty() || year <= 0);
        CardDTO card = new CardDTO();
        card.setNumber(cardNumber);
        card.setExp_date(LocalDate.now().plusYears(year));
        card.setStatus(Status.NO_ACTIVE);
//        card.setPhone(profile.getPhone());
        cardService.createCard(card);

    }

    private void userMenu(ProfileDTO profile) {
//        1. Add Card (number) - (cartani profile ga ulab qo'yamiz.) (added_date)
//        Enter Card Number:
//        (kiritilgan number database da bo'lishi kerak.)
//        2. Card List (number,exp_date,balance)
//        3. Card Change Status
//        4. Delete Card (visible_user = false,deleted_user)
//
//        4. ReFill (pul tashlash) (carta userga tegishli bo'lishi kerak.)
//        Enter card number:
//        Balance:
//        (Transaction with type 'ReFill')
//
//        (Transaction)
//                5. Transaction
//        CardNumber, Address, Amount,TransactionDate,Type (oxirgi birinchi ko'rinadi)
//        6. Make Payment (pul to'lash)
//        Enter cardNumber:
//        Enter terminal number:
//        (Transaction with type 'Payment')
        label:
        do {
            showUserMenu();
            int action = getAction();
            switch (action) {
                case 1 -> {
                    addCard(profile);
                }
                case 2 -> {
                    showCards(profile);
                }
                case 3 -> {
                    changeCardStatus(profile);
                }
                case 4 -> {
                    deleteCard(profile);
                }
                case 5 -> {
                    reFillCard(profile);
                }
                case 6 -> {
                }
            }
        } while (true);


    }

    private void reFillCard(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND+"Enter Card number: "+StringColors.ANSI_RESET);
        double amount = scanner.nextInt(BackgroundColors.GREEN_BACKGROUND+"Enter amount: "+StringColors.ANSI_RESET);
        cardService.reFillCard(profile,cardNumber,amount);

    }

    private void deleteCard(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND+"Enter Card number: "+StringColors.ANSI_RESET);
        cardService.deleteCard(profile,cardNumber);

    }

    private void changeCardStatus(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND+"Enter Card number: "+StringColors.ANSI_RESET);
        cardService.changeCardStatus(profile,cardNumber);
    }

    private static void showUserMenu() {
        System.out.println(BackgroundColors.GREEN_BACKGROUND + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 1. Add Card:          *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 2. Card List:         *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 3. Card Change Status *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 4. Delete Card        *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 5. ReFill             *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 6. Transaction        *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 7. Make Payment       *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 0. Exit               *
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t""" + StringColors.ANSI_RESET);
    }

    private void showCards(ProfileDTO profile) {
        List<CardDTO> ownCards = cardService.getOwnCards(profile);
        if (ownCards.isEmpty()) {
            System.out.println(BackgroundColors.WHITE_BACKGROUND+"You have no cards !!!"+StringColors.ANSI_RESET);
            return;
        }
        for (CardDTO ownCard : ownCards) {
            if (ownCard.getStatus().equals(Status.ACTIVE)) {
                System.out.println(BackgroundColors.YELLOW_BACKGROUND+ownCard+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+StringColors.ANSI_RESET);
            }else if (ownCard.getStatus().equals(Status.NO_ACTIVE)) {
                System.out.println(BackgroundColors.WHITE_BACKGROUND+ownCard+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+StringColors.ANSI_RESET);
            }else if (ownCard.getStatus().equals(Status.BLOCKED)) {
                System.out.println(BackgroundColors.RED_BACKGROUND+ownCard+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"+StringColors.ANSI_RESET);
            }
        }
    }

    private void addCard(ProfileDTO profile) {
        String cardNumber;
        do {
            cardNumber = scanner.nextLine(BackgroundColors.GREEN_BACKGROUND + "Enter the card number: " + StringColors.ANSI_RESET);
        } while (cardNumber.trim().isEmpty());
        boolean result = cardService.addCard(profile, cardNumber);
        if (result) {
            System.out.println(BackgroundColors.GREEN_BACKGROUND + "Card added !!!" + StringColors.ANSI_RESET);
        } else {
            System.out.println(BackgroundColors.RED_BACKGROUND + "Card not added !!!" + StringColors.ANSI_RESET);
        }
    }

    private int getAction() {
        int option = scanner.nextInt(BackgroundColors.WHITE_BACKGROUND + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tChoose action: """ + StringColors.ANSI_RESET);
//        int option = scanner.nextInt("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tChoose action: ");
//        System.out.println("");
        return option;
    }

    private void showMain() {
        System.out.print(BackgroundColors.GREEN_BACKGROUND + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t 1. Login        
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t 2. Registration 
                """ + StringColors.ANSI_RESET);

//        System.out.println(BackgroundColors.GREEN_BACKGROUND+"""
//                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * *
//                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 1. Login        *
//                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * *
//                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* 2. Registration *
//                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * *
//                """+StringColors.ANSI_RESET);
    }


}
