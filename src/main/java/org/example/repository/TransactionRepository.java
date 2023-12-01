package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.ResponsDTO;
import org.example.enums.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            preparedStatement.setDouble(2,amount);
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
            preparedStatement.setDouble(2,1700);
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
}
