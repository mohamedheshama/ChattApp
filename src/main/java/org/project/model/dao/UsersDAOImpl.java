package org.project.model.dao;

import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UsersDAOImpl implements UsersDAO {

    Connection connection;

    public UsersDAOImpl(Connection con) {
        this.connection = con;
    }

    @Override
    public Users login(String phone_number, String password) {
        Users user;

        try {

            PreparedStatement ps = connection.prepareStatement("SELECT id,name,phone_number,email,picture,password,gender,country,date_of_birth,bio,status FROM users WHERE phone_number=? AND password=?");
            ps.setString(1, phone_number);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = extractUserFromResultSet(rs);
                getUserFriends(user);
                getUserNotifications(user);
                return user;
            }


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean register(Users user) {
        //Check first if name exist using userExistMethod then register

        try {
            String SQL = "Insert into users (phone_number,name,email,password,gender,country,date_of_birth,bio,status) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement prepared_statement = connection.prepareStatement(SQL);
            prepared_statement.setString(1, user.getPhoneNumber());
            prepared_statement.setString(2, user.getName());
            prepared_statement.setString(3, user.getEmail());
            prepared_statement.setString(4, user.getPassword());
            prepared_statement.setString(5, String.valueOf(user.getGender()));
            prepared_statement.setString(6, user.getCountry());
            prepared_statement.setDate(7, user.getDateOfBirth());
            prepared_statement.setString(8, user.getBio());
            prepared_statement.setString(9, String.valueOf(user.getStatus()));
            prepared_statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateUser(Users user) {
        // make sure no empty mandatory fields
        // make sure input is validated

        try {

            PreparedStatement ps = connection.prepareStatement("SELECT id,name,phone_number,email,picture,password,gender,country,date_of_birth,bio,status FROM users WHERE users.id=?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

            //ps = connection.prepareStatement("select id from users where users.id=?");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.updateString("phone_number", user.getPhoneNumber());
                rs.updateString("name", user.getName());
                rs.updateString("email", user.getEmail());
                rs.updateString("password", user.getPassword());
                rs.updateString("gender", String.valueOf(user.getGender()));
                rs.updateString("country", user.getCountry());
                rs.updateDate("date_of_birth", user.getDateOfBirth());
                rs.updateString("bio", user.getBio());
                rs.updateString("status", String.valueOf(user.getStatus()));
                rs.updateRow();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public ObservableList<Friends> getUserFriends(Users user) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT u.id, u.name , u.phone_number, u.status FROM users u JOIN friends f on f.friend_id=u.id where f.user_id=? AND f.friend_status=?;");
            ps.setInt(1, user.getId());
            ps.setString(2, String.valueOf(RequestStatus.Accepted));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Friends friend = new Friends();
                friend.setFriend(extractFriendFromResultSet(rs));
                user.getFriends().add(friend);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ObservableList<Friends> getUserNotifications(Users user) {

        try {

            PreparedStatement ps = connection.prepareStatement("select u.id, u.name , u.phone_number, u.status FROM users u JOIN friends f on u.id=f.user_id where f.friend_id=? AND f.friend_status=? ;");
            ps.setInt(1, user.getId());
            ps.setString(2, String.valueOf(RequestStatus.Pending));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Friends friend = new Friends();

                friend.setFriend(extractFriendFromResultSet(rs));
                user.getRequest_notifications().add(friend);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private Users extractUserFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(rs.getInt("id"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setGender(Gender.valueOf(rs.getString("gender")));
        user.setCountry(rs.getString("country"));
        user.setDateOfBirth(rs.getDate("date_of_birth"));
        user.setBio(rs.getString("bio"));
        user.setStatus(UserStatus.valueOf(rs.getString("status")));
        user.setPicture(rs.getBlob("picture"));
        return user;
    }

    private Users extractFriendFromResultSet(ResultSet rs) throws SQLException {
        Users user = new Users();
        user.setId(rs.getInt("id"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setName(rs.getString("name"));
        user.setStatus(UserStatus.valueOf(rs.getString("status")));

        return user;
    }

    public boolean userExist(String phone_number) {
        try {
            PreparedStatement ps = connection.prepareStatement("select id,name from users where phone_number=?;");
            ps.setString(1, phone_number);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return true;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;


    }

    @Override
    public boolean updateStatus(Users user, UserStatus status) {

        try {
            PreparedStatement ps = connection.prepareStatement("select id,status from users where users.id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            //rs.updateString("status", String.valueOf(user.getStatus()));
            if (rs.next()) {
                rs.updateString("status", String.valueOf(status));
                rs.updateRow();
            }


            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
}
