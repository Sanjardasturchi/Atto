package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.CardDTO;
import org.example.dto.ProfileDTO;
import org.example.dto.ResponsDTO;
import org.example.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CardRepository {
    public boolean createCard(CardDTO card) {
        int res = 0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "insert into card(number,exp_date,balance,phone) values (?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, card.getNumber());
            preparedStatement.setDate(2, Date.valueOf(card.getExp_date()));
            preparedStatement.setDouble(3, 0);
            preparedStatement.setString(4, card.getPhone());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res != 0;
    }

    public List<CardDTO> getCardList() {
        List<CardDTO> cardList = new LinkedList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "select * from card";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                CardDTO card = new CardDTO();
                card.setNumber(resultSet.getString("number"));
                card.setExp_date(resultSet.getDate("exp_date").toLocalDate());
                card.setBalance(resultSet.getDouble("balance"));
                card.setStatus(Status.valueOf(resultSet.getString("status")));
                card.setPhone(resultSet.getString("phone"));
                card.setCreated_date(resultSet.getTimestamp("created_date").toLocalDateTime());
                cardList.add(card);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardList;
    }

    public boolean addCard(String number, String phone) {
        int res = 0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update card set phone=?, status=? where number=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, phone);
            preparedStatement.setString(2, Status.ACTIVE.name());
            preparedStatement.setString(3, number);
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res != 0;
    }

    public ResponsDTO changeStatus(String cardNumber, ProfileDTO profile) {
        CardDTO card = new CardDTO();
        card.setStatus(Status.ACTIVE);
        for (CardDTO cardDTO : getCardList()) {
            if (cardDTO.getExp_date() != null) {
                if (cardDTO.getPhone().equals(profile.getPhone()) && cardDTO.getNumber().equals(cardNumber)) {
                    card.setStatus(cardDTO.getStatus());
                }
            }
        }
        int res = 0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update card set status=? where number=? and phone=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            if (card.getStatus().equals(Status.ACTIVE)) {
                preparedStatement.setString(1, Status.NO_ACTIVE.name());
            } else if (card.getStatus().equals(Status.NO_ACTIVE)) {
                preparedStatement.setString(1, Status.ACTIVE.name());
            } else {
                connection.close();
                return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard is blocked ", false);
            }

            preparedStatement.setString(2, cardNumber);
            preparedStatement.setString(3, profile.getPhone());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (res > 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard status changed ", true);
        }
        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard not founded", false);
    }

    public ResponsDTO delete(String cardNumber, ProfileDTO profile) {
        int res=0;

        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update card set status=? where number=? and phone=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, Status.BLOCKED.name());
            preparedStatement.setString(2, cardNumber);
            preparedStatement.setString(3, profile.getPhone());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (res > 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tCard deleted ", true);
        }

        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tError!", false);
    }

    public ResponsDTO reFill(String cardNumber, ProfileDTO profile,double amount) {
        int res=0;

        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update card set balance=balance+? where number=? and phone=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDouble(1, amount);
            preparedStatement.setString(2, cardNumber);
            preparedStatement.setString(3, profile.getPhone());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (res > 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tDepositing money to the card has been done successfully 👌👌👌", true);
        }


        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tError Fill!!!",false);
    }

    public ResponsDTO update(String cardNumber, LocalDate expDate, ProfileDTO profile) {
        int res=0;

        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update card set exp_date=? where number=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setDate(1, Date.valueOf(expDate));
            preparedStatement.setString(2, cardNumber);
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (res > 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tDepositing money to the card has been done successfully 👌👌👌", true);
        }


        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tError Fill!!!",false);
    }
}
