package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.ResponsDTO;
import org.example.dto.TransactionDTO;
import org.example.enums.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TransactionRepository {
    //    card_number varchar(16)  primary key," +
//            "        amount double precision,   " +
//            "        terminal_code varchar references terminal(code)," +
//            "        transaction_type varchar," +
//            "        transaction_time timestamp default now()" +
    public ResponsDTO makeTransaction(String senderCardNumber, String address, double amount) {
        Connection connection = DatabaseUtil.getConnection();
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into transactions(card_number,amount,transaction_type,transaction_time) " +
                    "values (?,?,?,now())");

            preparedStatement.setString(1, senderCardNumber);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, TransactionType.REFILL.name());

            res = preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res != 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTransaction 👌👌👌 ", true);
        }


        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error in the information", false);
    }

    public ResponsDTO makePayment(String cardNumber, String terminalCode) {
        Connection connection = DatabaseUtil.getConnection();
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into transactions(card_number,amount,transaction_type,terminal_code,transaction_time) " +
                    "values (?,?,?,?,now())");

            preparedStatement.setString(1, cardNumber);
            preparedStatement.setDouble(2, 1700);
            preparedStatement.setString(3, TransactionType.PAYMENT.name());
            preparedStatement.setString(4, terminalCode);

            res = preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res != 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tPayment done 👌👌👌 ", true);
        }


        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error in the information", false);
    }

    public List<TransactionDTO> getTransactions() {
        List<TransactionDTO> transactions = new LinkedList<>();
        Connection connection = DatabaseUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from transactions");


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TransactionDTO transaction = new TransactionDTO();
                transaction.setCard_number(resultSet.getString("card_number"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setTerminal_code(resultSet.getString("terminal_code"));
                transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
                transaction.setTransactionTime(resultSet.getTimestamp("transaction_time").toLocalDateTime());
                transactions.add(transaction);
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public List<TransactionDTO> getTodaysTransactions() {
        List<TransactionDTO> transactions = new LinkedList<>();
        Connection connection = DatabaseUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from transactions");


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TransactionDTO transaction = new TransactionDTO();
                transaction.setCard_number(resultSet.getString("card_number"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setTerminal_code(resultSet.getString("terminal_code"));
                transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
                LocalDateTime transactionTime = resultSet.getTimestamp("transaction_time").toLocalDateTime();
                transaction.setTransactionTime(transactionTime);
                LocalDate localDate = transactionTime.toLocalDate();
                if (localDate.equals(LocalDate.now())) {
                    transactions.add(transaction);
                }
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public List<TransactionDTO> getInterimPayments(LocalDate localDate1,LocalDate localDate2) {
        List<TransactionDTO> transactions = new LinkedList<>();
        Connection connection = DatabaseUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from transactions");


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TransactionDTO transaction = new TransactionDTO();
                transaction.setCard_number(resultSet.getString("card_number"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setTerminal_code(resultSet.getString("terminal_code"));
                transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
                LocalDateTime transactionTime = resultSet.getTimestamp("transaction_time").toLocalDateTime();
                transaction.setTransactionTime(transactionTime);
                LocalDate localDate = transactionTime.toLocalDate();
                if (localDate.isAfter(localDate1)&&localDate.isBefore(localDate2)) {
                    transactions.add(transaction);
                }
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public List<TransactionDTO> getDailyPayments(LocalDate localDate) {
        List<TransactionDTO> transactions = new LinkedList<>();
        Connection connection = DatabaseUtil.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from transactions");


            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TransactionDTO transaction = new TransactionDTO();
                transaction.setCard_number(resultSet.getString("card_number"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setTerminal_code(resultSet.getString("terminal_code"));
                transaction.setTransactionType(TransactionType.valueOf(resultSet.getString("transaction_type")));
                LocalDateTime transactionTime = resultSet.getTimestamp("transaction_time").toLocalDateTime();
                transaction.setTransactionTime(transactionTime);
                LocalDate localDate1 = transactionTime.toLocalDate();
                if (localDate1.equals(localDate)) {
                    transactions.add(transaction);
                }
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;

    }
}
