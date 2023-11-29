package org.example.repository;

import org.example.db.DatabaseUtil;
import org.example.dto.ProfileDTO;
import org.example.enums.ProfileRole;
import org.example.enums.Status;

import java.sql.*;

public class ProfileRepository {

    public ProfileDTO login(ProfileDTO profileDTO) {
        try {
            Connection connection = DatabaseUtil.getConnection();
            String sql = "select * from profile where phone=? and password=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1,profileDTO.getPhone());
            preparedStatement.setString(2,profileDTO.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                ProfileDTO profile=new ProfileDTO();
                profile.setName(resultSet.getString("name"));
                profile.setSurname(resultSet.getString("surname"));
                profile.setPhone(resultSet.getString("phone"));
                profile.setPassword(resultSet.getString("password"));
                profile.setCreated_date(resultSet.getTimestamp("created_date").toLocalDateTime());
                profile.setStatus(Status.valueOf(resultSet.getString("status")));
                profile.setProfileRole(ProfileRole.valueOf(resultSet.getString("profile_role")));
                connection.close();
                return profile;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean registration(ProfileDTO profile) {
        int res=0;
        try {
        Connection connection = DatabaseUtil.getConnection();
        String sql = "insert into profile(name,surname,phone,password,profile_role) values (?,?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, profile.getName());
            preparedStatement.setString(2, profile.getSurname());
            preparedStatement.setString(3, profile.getPhone());
            preparedStatement.setString(4, profile.getPassword());
            preparedStatement.setString(5, profile.getProfileRole().toString());
            res = preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res!=0;

    }
}
