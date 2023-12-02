package org.example.controller;

import org.example.colors.StringColors;
import org.example.db.DatabaseUtil;
import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.dto.TerminalDTO;
import org.example.enums.ProfileRole;
import org.example.enums.Status;
import org.example.service.CardService;
import org.example.service.TerminalService;
import org.example.service.TransactionService;
import org.example.service.UserService;
import org.example.utils.ScannerUtils;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private String campanyCardNumber;

    public String getCampanyCardNumber() {
        return campanyCardNumber;
    }

    public void setCampanyCardNumber(String campanyCardNumber) {
        this.campanyCardNumber = campanyCardNumber;
    }

    static ScannerUtils scanner = new ScannerUtils();
    static UserService userService = new UserService();
    CardService cardService = new CardService();
    TerminalService terminalService = new TerminalService();
    TransactionService transactionService = new TransactionService();


    public void start() {
        setCampanyCardNumber("5");
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
                default -> {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWrong input!!!" + StringColors.ANSI_RESET);
                }
            }
        } while (true);

    }

    private void registration() {

        String name = scanner.nextLine(StringColors.GREEN + "Enter name:" + StringColors.ANSI_RESET);
//        String name = scanner.nextLineWithColor("Enter name:",BackgroundColors.BLUE_BACKGROUND,StringColors.RED);

        String surname = scanner.nextLine(StringColors.GREEN + "Enter surname:" + StringColors.ANSI_RESET);
        String phone;
        String password;
        do {
            phone = scanner.nextLine(StringColors.GREEN + "Enter phoneNumber: " + StringColors.ANSI_RESET);
            password = scanner.nextLine(StringColors.GREEN + "Enter password: " + StringColors.ANSI_RESET);
        } while (phone == null || password == null);


        ProfileDTO profile = new ProfileDTO();
        profile.setName(name);
        profile.setSurname(surname);
        profile.setPhone(phone);
        profile.setPassword(password);
        profile.setProfileRole(ProfileRole.USER);


        boolean result = userService.registration(profile);
        if (result) {
            System.out.println(StringColors.GREEN + "Successful 👌👌👌" + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + StringColors.BLACK + "Error registration!!!" + StringColors.ANSI_RESET);
        }

    }

    private void login() {
        String phoneNumber = null;
        String password = null;
        do {
            phoneNumber = scanner.nextLine(StringColors.GREEN + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter phoneNumber:  """ + StringColors.ANSI_RESET);
            password = scanner.nextLine(StringColors.GREEN + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter password:  """ + StringColors.ANSI_RESET);
//            System.out.println();
        } while (phoneNumber.trim().isEmpty() || password.trim().isEmpty());
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setPhone(phoneNumber);
        profileDTO.setPassword(password);

        ProfileDTO profile = userService.login(profileDTO);
        if (profile == null) {
            System.out.println(StringColors.RED + """
                                        \t\t\t\t\t\t\t\t\t\tNot found
                    """ + StringColors.ANSI_RESET);
            return;
        } else {
            if (profile.getStatus().equals(Status.NO_ACTIVE)) {
                System.out.println(StringColors.RED + """
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
        System.out.println();
        if (!profile.getName().trim().isEmpty()) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWelcome " + profile.getName() + StringColors.RED + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tYour status ADMIN" + StringColors.ANSI_RESET);
        } else if (!profile.getSurname().trim().isEmpty()) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWelcome " + profile.getSurname() + StringColors.RED + "\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tYour status ADMIN" + StringColors.ANSI_RESET);
        }
        do {
            System.out.println();
            System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t******* " + StringColors.BLUE + "ADMIN MENU" + StringColors.GREEN + " ******* " + StringColors.ANSI_RESET);
            showAdminMenu();
            int option = scanner.nextInt(StringColors.WHITE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tChoose option: " + StringColors.WHITE);
            switch (option) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    cardMenu(profile);
                }
                case 2 -> {
                    terminalMenu();
                }
                case 3 -> {
                    profileMenu(profile);
                }
                case 4 -> {
                    transactionForAdmin();
                }
                case 5 -> {
                    statistic();
                }
                default -> {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWrong input!!!" + StringColors.ANSI_RESET);
                }
            }
        } while (true);
    }

    private void statistic() {
        do {
        showStatisticMenu();
            int action = getAction();
            switch (action){
                case 0 -> {
                    return;
                }
                case 1 -> {
                    showTodaysTransactions();
                }
                case 2 -> {
                    sowDailyPayments();
                }
                case 3 -> {
                    showInterimPayments();

                }
                case 4 -> {
                    generalBalance();
                }
                case 5 -> {
                    showTransactionByTerminal();
                }
                case 6 -> {
                    showTransactionByCard();
                }

            }
        }while (true);
    }

    private void showTransactionByCard() {
        String cardNumber = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter card number: " + StringColors.ANSI_RESET);

        transactionService.showTransactionsByCard(cardNumber);
    }

    private void showTransactionByTerminal() {
        String terminalCode = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal code: " + StringColors.ANSI_RESET);

        transactionService.showTransactionsByTerminal(terminalCode);
    }

    private void generalBalance() {
        cardService.showCompanyCardBalance(getCampanyCardNumber());
    }

    private void sowDailyPayments() {
        LocalDate localDate = scanner.nextLocalDate(StringColors.GREEN+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Date:"+StringColors.ANSI_RESET);
        transactionService.showDailyPayments(localDate);

    }

    private void showInterimPayments() {
        LocalDate localDate1 = scanner.nextLocalDate(StringColors.GREEN+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter FromDate:"+StringColors.ANSI_RESET);
        LocalDate localDate2 = scanner.nextLocalDate(StringColors.GREEN+"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter ToDate:"+StringColors.ANSI_RESET);

        transactionService.showInterimPayments(localDate1,localDate2);
    }

    private void showTodaysTransactions() {
        transactionService.showTodaysTransactions();
    }

    private void showStatisticMenu() {
        System.out.println();
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t****** " + StringColors.BLUE + "STATISTIC SETTINGS" + StringColors.GREEN + " *****" + StringColors.ANSI_RESET);
        System.out.println(StringColors.BLUE + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Bugungi to'lovlar   -   Today's payments
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. Kunlik to'lovlar   -   Daily payments
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t3. Oraliq to'lovlar   -   Interim payments
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t4. Umumiy balance   -   General balance
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t5. Transaction by Terminal
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t6. Transaction By Card
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
    }

    private void transactionForAdmin() {
        do {
            showTransactionMenu();
            int action = getAction();
            switch (action) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    transactionService.showTransactions();
                }
                case 2 -> {
                    cardService.showCompanyCardBalance(getCampanyCardNumber());
                }
            }
        }while (true);
    }

    private void showTransactionMenu() {
        System.out.println();
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t****** " + StringColors.BLUE + "TRANSACTION SETTINGS" + StringColors.GREEN + " *****" + StringColors.ANSI_RESET);
        System.out.println(StringColors.BLUE + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Transaction List
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. Company Card Balance
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
    }

    private void changeCardStatusByAdmin() {
        String cardNumber;

        do {
            cardNumber = scanner.nextLine(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter card number:  " + StringColors.ANSI_RESET);
            if (cardNumber.trim().isEmpty()) {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard number cannot be empty!!!  " + StringColors.ANSI_RESET);
                continue;
            }

            showStatusToAdmin();
            int action = getAction();
            switch (action) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    cardService.changeCardStatusByAdmin(cardNumber, Status.ACTIVE.name());
                    return;
                }
                case 2 -> {
                    cardService.changeCardStatusByAdmin(cardNumber, Status.BLOCKED.name());
                    return;
                }
            }
        } while (true);

    }

    private void showStatusToAdmin() {
        System.out.println();
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t****** " + StringColors.BLUE + "CHOOSE STATUS" + StringColors.GREEN + " *****" + StringColors.ANSI_RESET);
        System.out.println(StringColors.BLUE + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Active
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. No Active
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
    }

    private void terminalMenu() {
        do {
            showTerminalMenu();
            int option = getAction();

            switch (option) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    createTerminal();
                }
                case 2 -> {
                    showTerminalList();
                }
                case 3 -> {
                    updateTerminal();
                }
                case 4 -> {
                    changeTerminalStatus();
                }
                case 5 -> {
                    deleteTerminal();
                }
            }
        } while (true);

    }

    private void deleteTerminal() {
        String terminalCode;
        do {
            terminalCode = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal code: " + StringColors.ANSI_RESET);

            if (terminalCode.trim().isEmpty()) {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal code cannot be empty!!!  " + StringColors.ANSI_RESET);
                continue;
            }
            terminalService.delete(terminalCode, Status.BLOCKED.name());
            return;

        } while (true);
    }

    private void changeTerminalStatus() {
        String terminalCode;
        do {
            terminalCode = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal code: " + StringColors.ANSI_RESET);

            if (terminalCode.trim().isEmpty()) {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal code cannot be empty!!!  " + StringColors.ANSI_RESET);
                continue;
            }
            showStatusToAdmin();
            int action = getAction();
            switch (action) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    terminalService.changeTerminalStatus(terminalCode, Status.ACTIVE.name());
                    return;
                }
                case 2 -> {
                    terminalService.changeTerminalStatus(terminalCode, Status.NO_ACTIVE.name());
                    return;
                }
            }

        } while (true);
    }

    private static void showTerminalMenu() {
        System.out.println();
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t****** " + StringColors.BLUE + "TERMINAL SETTINGS" + StringColors.GREEN + " *****" + StringColors.ANSI_RESET);
        System.out.println(StringColors.BLUE + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Create Terminal (code unique,address)
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. Terminal List
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t3. Update Terminal (code,address)
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t4. Change Terminal Status
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t5. Delete
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);

    }

    private void updateTerminal() {
        String terminalCode, terminalAddress;
        do {
            terminalCode = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal code: " + StringColors.ANSI_RESET);
            terminalAddress = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal address: " + StringColors.ANSI_RESET);
        } while (terminalCode.trim().isEmpty() || terminalAddress.trim().isEmpty());
        TerminalDTO terminal = new TerminalDTO();
        terminal.setCode(terminalCode);

        terminalService.updateTerminal(terminal, terminalAddress);
    }

    private void showTerminalList() {
        terminalService.showTerminalList();
    }

    private void createTerminal() {
        String terminalCode, terminalAddress;
        do {
            terminalCode = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal code: " + StringColors.ANSI_RESET);
            terminalAddress = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter terminal address: " + StringColors.ANSI_RESET);
        } while (terminalCode.trim().isEmpty() || terminalAddress.trim().isEmpty());
        TerminalDTO terminal = new TerminalDTO();
        terminal.setCode(terminalCode);
        terminal.setAddress(terminalAddress);

        terminalService.creatTerminal(terminal);

    }

    private static void showAdminMenu() {
        System.out.println(StringColors.BLUE + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1.Card
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2.Terminal
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t3.Profile
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t4.Transaction
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t5.Statistic
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
    }

    private void profileMenu(ProfileDTO profile) {
        do {
            System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t*****" + StringColors.BLUE + " PROFILE SETTINGS " + StringColors.GREEN + "*****" + StringColors.ANSI_RESET);
            System.out.println(StringColors.BLUE + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Profile List
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. Change Profile Status
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
            int action = getAction();
            switch (action) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    userService.showProfileList();
                }
                case 2 -> {
                    changeProfileStatus();
                }
                default -> {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWrong input!!!" + StringColors.ANSI_RESET);
                }
            }
        } while (true);
    }

    private void changeProfileStatus() {
        do {
            System.out.println();
            System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t*****" + StringColors.BLUE + " Change Profile Status " + StringColors.GREEN + "*****" + StringColors.ANSI_RESET);
            System.out.println(StringColors.BLUE + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Make active
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. Make block
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
            int action = getAction();

            switch (action) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    changeProfileStatus(Status.ACTIVE);
                }
                case 2 -> {
                    changeProfileStatus(Status.BLOCKED);
                }
            }

        } while (true);
    }

    private void changeProfileStatus(Status status) {
        String phoneNumber = scanner.nextLine(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Phone Number: " + StringColors.ANSI_RESET);
        cardService.changeProfileStatus(status, phoneNumber);
    }

    private void cardMenu(ProfileDTO profile) {
        do {
            System.out.println();
            System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t****** " + StringColors.BLUE + "CARD SETTINGS" + StringColors.GREEN + " *****" + StringColors.ANSI_RESET);
            System.out.println(StringColors.BLUE + """
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t1. Create Card(number,exp_date)
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t2. Card List
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t3. Update Card (number,exp_date)
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t4. Change Card status
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t5. Delete Card
                    \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t0. Exit""" + StringColors.ANSI_RESET);
            int option = scanner.nextInt("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tChoose option: ");

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
                    changeCardStatusByAdmin();
                }
                case 5 -> {
                    deleteCardByAdmin();
                }
                default -> {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWrong input!!!" + StringColors.ANSI_RESET);
                }
            }
        } while (true);

    }

    private void deleteCardByAdmin() {
        String cardNumber;

        do {
            cardNumber = scanner.nextLine(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter card number:  " + StringColors.ANSI_RESET);
            if (cardNumber.trim().isEmpty()) {
                System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard number cannot be empty!!!  " + StringColors.ANSI_RESET);
            }
        } while (cardNumber.trim().isEmpty());
        cardService.deleteCardByAdmin(cardNumber, Status.BLOCKED.name());
    }

    private void showCards() {
        List<CardDTO> cardList = cardService.getCardList();
        if (cardList != null) {
            for (CardDTO ownCard : cardList) {
                if (ownCard.getStatus().equals(Status.ACTIVE)) {
                    System.out.println(StringColors.YELLOW + ownCard + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
                } else if (ownCard.getStatus().equals(Status.NO_ACTIVE)) {
                    System.out.println(StringColors.WHITE + ownCard + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
                } else {
                    System.out.println(StringColors.RED + ownCard + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
                }
            }
        }
    }

    private void updateCard(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Card number: " + StringColors.ANSI_RESET);
        LocalDate expDate = scanner.nextLocalDate(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter expiration date: " + StringColors.ANSI_RESET);

        cardService.updateCard(cardNumber, expDate, profile);

    }

    private void createCard(ProfileDTO profile) {
        String cardNumber;
        int year;
        do {
            cardNumber = scanner.nextLine(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Card number: " + StringColors.ANSI_RESET);
            year = scanner.nextInt(StringColors.BLUE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the expiration date (3-10): " + StringColors.ANSI_RESET);
        } while (cardNumber.trim().isEmpty() || year <= 0);
        CardDTO card = new CardDTO();
        card.setNumber(cardNumber);
        card.setExp_date(LocalDate.now().plusYears(year));
        card.setStatus(Status.NO_ACTIVE);
        cardService.createCard(card);

    }

    private void userMenu(ProfileDTO profile) {

//        7. Make Payment (pul to'lash)
//        Enter cardNumber:
//        Enter terminal number:
//        (Transaction with type 'Payment')
        System.out.println();
        if (!profile.getName().trim().isEmpty()) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWelcome " + profile.getName() + StringColors.ANSI_RESET);
        } else if (!profile.getSurname().trim().isEmpty()) {
            System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWelcome " + profile.getSurname() + StringColors.ANSI_RESET);
        }
        do {
            showUserMenu();
            int action = getAction();
            switch (action) {
                case 0 -> {
                    return;
                }
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
                    transaction(profile);
                }
                case 7 -> {
                    makePayment();
                }
                default -> {
                    System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tWrong input!!!" + StringColors.ANSI_RESET);
                }
            }
        } while (true);


    }

    private void makePayment() {
        String cardNumber = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the card number: " + StringColors.ANSI_RESET);
        String terminalCode = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the terminal code: " + StringColors.ANSI_RESET);

        transactionService.makePayment(cardNumber, terminalCode, getCampanyCardNumber());

    }

    private void transaction(ProfileDTO profile) {
        transactionService.makeTransaction(profile);
    }

    private void reFillCard(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Card number: " + StringColors.ANSI_RESET);
        double amount = scanner.nextDouble(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter amount: " + StringColors.ANSI_RESET);
        cardService.reFillCard(profile, cardNumber, amount);

    }

    private void deleteCard(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Card number: " + StringColors.ANSI_RESET);
        cardService.deleteCard(profile, cardNumber);

    }

    private void changeCardStatus(ProfileDTO profile) {
        String cardNumber = scanner.nextLine(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter Card number: " + StringColors.ANSI_RESET);
        cardService.changeCardStatus(profile, cardNumber);
    }

    private static void showUserMenu() {

        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "1. Add Card:          " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "2. Card List:         " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "3. Card Change Status " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "4. Delete Card        " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "5. ReFill             " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "6. Transaction        " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "7. Make Payment       " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);
        System.out.print(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* " + StringColors.ANSI_RESET);
        System.out.print(StringColors.BLUE + "0. Exit               " + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "*" + StringColors.ANSI_RESET);
        System.out.println(StringColors.GREEN + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t* * * * * * * * * * * * *" + StringColors.ANSI_RESET);

    }

    private void showCards(ProfileDTO profile) {
        List<CardDTO> ownCards = cardService.getOwnCards(profile);
        if (ownCards.isEmpty()) {
            System.out.println(StringColors.WHITE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tYou have no cards !!!" + StringColors.ANSI_RESET);
            return;
        }
        for (CardDTO ownCard : ownCards) {
            if (ownCard.getStatus().equals(Status.ACTIVE)) {
                System.out.println(StringColors.YELLOW + ownCard + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
            } else if (ownCard.getStatus().equals(Status.NO_ACTIVE)) {
                System.out.println(StringColors.WHITE + ownCard + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
            } else if (ownCard.getStatus().equals(Status.BLOCKED)) {
                System.out.println(StringColors.RED + ownCard + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" + StringColors.ANSI_RESET);
            }
        }
    }

    private void addCard(ProfileDTO profile) {
        String cardNumber;
        do {
            cardNumber = scanner.nextLine(StringColors.WHITE + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tEnter the card number: " + StringColors.ANSI_RESET);
        } while (cardNumber.trim().isEmpty());
        boolean result = cardService.addCard(profile, cardNumber);
        if (result) {
            System.out.println(StringColors.YELLOW + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard added !!!" + StringColors.ANSI_RESET);
        } else {
            System.out.println(StringColors.RED + "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not added !!!" + StringColors.ANSI_RESET);
        }
    }

    private int getAction() {
        int option = scanner.nextInt(StringColors.WHITE + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\tChoose action: """ + StringColors.ANSI_RESET);
        return option;
    }

    private void showMain() {
        System.out.print(StringColors.GREEN + """
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t 1. Login        
                \t\t\t\t\t\t\t\t\t\t\t\t\t\t\t 2. Registration 
                """ + StringColors.ANSI_RESET);

    }


}
