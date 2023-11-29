package org.example.db;

import org.example.dto.ProfileDTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    public void createProfileTable() {

        try {
            Connection con = DatabaseUtil.getConnection();
            Statement statement = con.createStatement(); // <3>
            String sql = "   create table  if not exists  profile(" +
                    "                name varchar not null ," +
                    "                surname varchar not null ," +
                    "                phone varchar(13) primary key," +
                    "                password varchar," +
                    "                created_date timestamp default now()," +
                    "                status varchar default  'ACTIVE'," +
                    "                profile_role varchar" +
                    ");";
            statement.executeUpdate(sql); // <4>
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createCardTable() {

        try {
            Connection con = DatabaseUtil.getConnection();
            Statement statement = con.createStatement(); // <3>
            String sql = "  create table  if not exists  card(" +
                    "                number varchar(16)primary key ," +
                    "                exp_date date," +
                    "                balance double precision," +
                    "                status varchar default  'NO_ACTIVE'," +
                    "                phone varchar(13) references profile (phone)," +
                    "                created_date timestamp default now()" +
                    ");";
            statement.executeUpdate(sql); // <4>
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTerminalTable() {

        try {
            Connection con = DatabaseUtil.getConnection();
            Statement statement = con.createStatement(); // <3>
            String sql = "   create table if not exists terminal(" +
                    "        code varchar  primary key," +
                    "        address varchar not null,   " +
                    "        status varchar default 'ACTIVE'," +
                    "        created_date timestamp default now()" +
                    "      );";
            statement.executeUpdate(sql); // <4>
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTransactionTable() {

        try {
            Connection con = DatabaseUtil.getConnection();
            Statement statement = con.createStatement(); // <3>
            String sql = "   create table if not exists transactions(" +
                    "        card_number varchar(16)  primary key," +
                    "        amount double precision,   " +
                    "        terminal_code varchar references terminal(code)," +
                    "        transaction_type varchar," +
                    "        transaction_time timestamp default now()" +
                    "      );";
            statement.executeUpdate(sql); // <4>
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc_db",
                    "jdbc_user", "123456"); // <2>
            return con;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
