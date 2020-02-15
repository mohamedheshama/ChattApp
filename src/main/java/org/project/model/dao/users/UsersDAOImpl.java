package org.project.model.dao.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.project.exceptions.UserAlreadyExistException;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.dao.friends.RequestStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class UsersDAOImpl implements UsersDAO, ConnectionStrategy{

    ConnectionStrategy connectionStrategy;
    Connection connection;
    Logger logger = Logger.getLogger(UsersDAOImpl.class.getName());


    public UsersDAOImpl(ConnectionStrategy con) throws SQLException {
        this.connectionStrategy = con;
        connection = connectionStrategy.getConnection();
    }

    @Override
    public Users login(String phoneNumber) {
        Users user;
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT id,name,phone_number,email,picture,password,gender,country,date_of_birth,bio,status FROM users WHERE phone_number=?" , ResultSet.CLOSE_CURSORS_AT_COMMIT);){
            ps.setString(1, phoneNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                user = extractUserFromResultSet(rs);
                getUserFriends(user);
                getUserNotifications(user);
                return user;
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warning(e.getMessage());
                e.printStackTrace();
            }
        }
        return null;
    }



    @Override
    public boolean register(Users user) throws UserAlreadyExistException {
        if (isUserExist(user.getPhoneNumber()))
            throw new UserAlreadyExistException("User Already exist in our DB");
        //Check first if name exist using isUserExistMethod then register
        String sql = "Insert into users (phone_number,name,email,password)" +
                " values (?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setString(1, user.getPhoneNumber());
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateUser(Users user) {
        // make sure no empty mandatory fields
        // make sure input is validated
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT id,name,phone_number,email,picture,password,gender,country,date_of_birth,bio,status FROM users WHERE users.id=?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
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
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return false;
    }

    @Override
    public boolean deleteUSer(Users user) {
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT id,name,phone_number,email,picture,password,gender,country,date_of_birth,bio,status FROM users WHERE users.phone_number=?;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setString(1, user.getPhoneNumber());
            rs = ps.executeQuery();
            if (rs.next()) {
                rs.deleteRow();
                return true;
            }

        } catch (SQLException e) {
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return false;
    }

    @Override
    public ArrayList<Users> getUserFriends(Users user) {
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT u.id, u.name , u.phone_number, u.status" +
                " FROM users u JOIN friends f on f.friend_id=u.id" +
                " where f.user_id=? AND f.friend_status=?" +
                " union" +
                " SELECT u.id, u.name , u.phone_number, u.status" +
                " FROM users u JOIN friends f on f.friend_id=u.id" +
                " where f.friend_id=? AND f.friend_status=?;");) {
            ps.setInt(1, user.getId());
            ps.setString(2, String.valueOf(RequestStatus.Accepted));
            ps.setInt(3, user.getId());
            ps.setString(4, String.valueOf(RequestStatus.Accepted));
            rs = ps.executeQuery();
            while (rs.next()) {
                Users friend = extractFriendFromResultSet(rs);
                user.getFriends().add(friend);
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public ObservableList<Users> getUsers() {
        ObservableList<Users> users= FXCollections.observableArrayList();;
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("select * FROM users ;")) {

            rs = ps.executeQuery();
            while (rs.next()) {
                Users user =  extractUserFromResultSet(rs);
                users.add(user);

                //friend.setFriend(extractFriendFromResultSet(rs));
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return users;

    }

    @Override
    public ArrayList<Users> getUserNotifications(Users user) {
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("select u.id, u.name , u.phone_number, u.status FROM users u JOIN friends f on u.id=f.user_id where f.friend_id=? AND f.friend_status=? ;");) {
            ps.setInt(1, user.getId());
            ps.setString(2, String.valueOf(RequestStatus.Pending));
            rs = ps.executeQuery();
            while (rs.next()) {
                Users friend =  extractFriendFromResultSet(rs);

                //friend.setFriend(extractFriendFromResultSet(rs));
                user.getRequest_notifications().add(friend);
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    public boolean isUserExist(String phoneNumber) {
        try {
            PreparedStatement ps = connection.prepareStatement("select id,name from users where phone_number=?;");
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return true;

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
        }
        return false;


    }

    @Override
    public boolean matchUserNameAndPassword(String phoneNumber, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement("select id,name from users where phone_number=? And password=?");
            ps.setString(1, phoneNumber);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateStatus(Users user, UserStatus status) {
        ResultSet rs = null;
        try (PreparedStatement ps = connection.prepareStatement("select id,status from users where users.id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            if (rs.next()) {
                rs.updateString("status", String.valueOf(status));
                rs.updateRow();
            }
            return true;
        } catch (SQLException e) {
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return false;
    }

    @Override
    public Map<String, Integer> getUsersNumByCountry() {
        Map<String, Integer> map = new HashMap<String, Integer>();
        ResultSet resultSet = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT count(id) ,country from users group by(country);")) {
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                map.put(resultSet.getString(2), resultSet.getInt(1));
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public Map<String, Integer> getUsersByGender() {
        Map<String, Integer> usersNumByGendermap = new HashMap<String, Integer>();
        ResultSet resultSet = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT count(id) ,gender from users group by(gender);")) {
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                usersNumByGendermap.put(resultSet.getString(2), resultSet.getInt(1));
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usersNumByGendermap;
    }

    @Override
    public Map<String, Integer> getUsersByStatus() {
        Map<String, Integer> usersNumByStatusmap = new HashMap<String, Integer>();
        ResultSet resultSet = null;
        try (PreparedStatement ps = connection.prepareStatement("Select Count(id),status from users where status in('Available','Offline') group by(status);;")) {
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                usersNumByStatusmap.put(resultSet.getString(2), resultSet.getInt(1));
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return usersNumByStatusmap;
    }

    @Override
    public Connection getConnection() throws SQLException {
            Connection conn;
            conn = connectionStrategy.getConnection();
            return conn;

    }
}
