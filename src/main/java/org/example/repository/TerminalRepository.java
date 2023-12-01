package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.ResponsDTO;
import org.example.dto.TerminalDTO;
import org.example.enums.Status;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class TerminalRepository {
    public ResponsDTO creatTerminal(TerminalDTO terminal) {

        Connection connection = DatabaseUtil.getConnection();
        int res = 0;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into terminal(code,address) values (?,?)");

            preparedStatement.setString(1, terminal.getCode());
            preparedStatement.setString(2, terminal.getAddress());

            res = preparedStatement.executeUpdate();

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (res != 0) {
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal created ", true);
        }


        return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error in the information", false);
    }

    public List<TerminalDTO> getTerminalList() {

        List<TerminalDTO> terminalList = new LinkedList<>();
        try {
            Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "select * from terminal";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                TerminalDTO terminal = new TerminalDTO();
                terminal.setCode(resultSet.getString("code"));
                terminal.setAddress(resultSet.getString("address"));
                terminal.setStatus(Status.valueOf(resultSet.getString("status")));
                terminal.setCreatedDate(resultSet.getDate("created_date").toLocalDate());
                terminalList.add(terminal);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terminalList;
    }

    public ResponsDTO uptadeTerminal(TerminalDTO terminal,String address) {
        int res = 0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update terminal set address=? where code=? " ;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, address);
            preparedStatement.setString(2, terminal.getCode());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(res != 0){
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal updated ", true);
        }else{
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error in the information ", false);
        }
    }


    public ResponsDTO changeStatus(String terminalCode, String status) {
        int res = 0;
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "update terminal set status=? where code=? " ;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, terminalCode);
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(res != 0){
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tTerminal status updated ", true);
        }else{
            return new ResponsDTO("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tThere is an error in the information ", false);
        }


    }


    public TerminalDTO getTerminalByCode(String terminalCode) {
        TerminalDTO terminal = new TerminalDTO();
        try {
            Connection connection = DatabaseUtil.getConnection();
            Statement statement = connection.createStatement();

            String sql = "select * from terminal";
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                terminal.setCode(resultSet.getString("code"));
                terminal.setAddress(resultSet.getString("address"));
                terminal.setStatus(Status.valueOf(resultSet.getString("status")));
                terminal.setCreatedDate(resultSet.getDate("created_date").toLocalDate());
                return terminal;
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return terminal;
    }
}
