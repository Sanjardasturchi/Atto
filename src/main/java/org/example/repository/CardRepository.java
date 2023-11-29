package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.CardDTO;
import org.example.enums.Status;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CardRepository {
    public boolean createCard(CardDTO card) {
        int res=0;
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
        return res!=0;
    }

    public List<CardDTO> getCardList() {
        List<CardDTO> cardList = new LinkedList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "select * from card";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                CardDTO card=new CardDTO();
                card.setNumber(resultSet.getString("number"));
                card.setExp_date(resultSet.getDate("exp_date").toLocalDate());
                card.setBalance(resultSet.getDouble("balance"));
                card.setStatus(Status.valueOf(resultSet.getString("status")));
                card.setNumber(resultSet.getString("phone"));
                card.setCreated_date(resultSet.getTimestamp("created_date").toLocalDateTime());
                cardList.add(card);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardList;
    }
}
