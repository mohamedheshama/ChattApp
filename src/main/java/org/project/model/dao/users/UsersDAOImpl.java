package org.project.model.dao.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.project.exceptions.UserAlreadyExistException;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.dao.friends.RequestStatus;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        ByteArrayInputStream bais=null;
        try {
            bais = new ByteArrayInputStream(user.getDisplayPicture());
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                updatePicture(user);
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
    public void updatePicture(Users user){
        ByteArrayInputStream bais =null;
        PreparedStatement pstmt = null;
        int rowsAdded = -0;
        try {
            bais=new ByteArrayInputStream(user.getDisplayPicture());
            String SQL = "UPDATE users SET picture = ? WHERE  id= ?";
            pstmt = connection.prepareStatement(SQL);
            pstmt.setBinaryStream(1, bais, user.getDisplayPicture().length);
            pstmt.setInt(2,user.getId());
            rowsAdded = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rowsAdded > 0) {
            System.out.println("user was inserted successfully!");
        }
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
                " FROM users u JOIN friends f on f.user_id=u.id" +
                " where f.friend_id=? AND f.friend_status=?;");) {
            ps.setInt(1, user.getId());
            ps.setString(2, String.valueOf(RequestStatus.Accepted));
            ps.setInt(3, user.getId());
            ps.setString(4, String.valueOf(RequestStatus.Accepted));
            rs = ps.executeQuery();
            user.getFriends().clear();
            while (rs.next()) {
                Users friend = extractFriendFromResultSet(rs);
                user.getFriends().add(friend);
            }
            return user.getFriends();

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
            user.getRequest_notifications().clear();
            while (rs.next()) {
                Users friend =  extractFriendFromResultSet(rs);

                //friend.setFriend(extractFriendFromResultSet(rs));
                user.getRequest_notifications().add(friend);
            }
            return user.getRequest_notifications();

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
        //user.setDisplayPicture(rs.getBytes("picture"));

        System.out.println("inside get frinds bytes"+user.getDisplayPicture());

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
        try (PreparedStatement ps = connection.prepareStatement("update users set Status =? where id = ?;")) {
            ps.setString(1, String.valueOf(status));
            ps.setInt(2, user.getId());
            if (ps.executeUpdate() > 0){
                System.out.println(user);
                return true;
            }
                return true;
        } catch (SQLException e) {
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        }


        return false;
    }

    @Override
    public int getUserIDByPhoneNo(String phoneNo) {
        int userId = 0;
        ResultSet resultSet = null;
        if (isUserExist(phoneNo)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT id from users where phone_number=?;")) {
                preparedStatement.setString(1, phoneNo);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    userId = resultSet.getInt(1);
                }

            } catch (SQLException ex) {
                logger.warning(ex.getSQLState());
                logger.warning(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return userId;
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
        try (PreparedStatement ps = connection.prepareStatement("Select Count(id),status from users where status in('Available','Offline') group by(status);")) {
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
    public boolean addContactRequest(List<String> contactList, Users user) {
        int friendId = 0;
        int result = 0;
        boolean added = false;
        if (contactList.size() > 0) {
            for (String phoneNo : contactList) {
                friendId = getUserIDByPhoneNo(phoneNo);
                String sql = "Insert into friends (friend_status,user_id,friend_id)" +
                        " values (?,?,?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                    preparedStatement.setString(1, "Pending");
                    preparedStatement.setInt(2, user.getId());
                    preparedStatement.setInt(3, friendId);
                    result = preparedStatement.executeUpdate();
                    if (result > 0) {
                        added = true;
                    }
                } catch (SQLException e) {
                    logger.warning(e.getSQLState());
                    logger.warning(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return added;
    }

    @Override
    public List<String> getUsersList(int userId) {
        List<String> usersList = new ArrayList<>();
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement("select phone_number from users  where id != ALL (select friend_id from friends where user_id = ? And friend_status IN ('Accepted','Pending' ) ) And id != ?;")) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, userId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                usersList.add(resultSet.getString(1));
            }

        } catch (SQLException ex) {
            logger.warning(ex.getSQLState());
            logger.warning(ex.getMessage());
            ex.printStackTrace();
        }
        return usersList;
    }

    @Override
    public boolean acceptRequest(Users currentUser, Users friend) {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE friends SET friend_status='Accepted' WHERE user_id=? AND friend_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setInt(1, friend.getId());
            ps.setInt(2,currentUser.getId());
            ps.executeUpdate();
            currentUser.setRequest_notifications(getUserNotifications(currentUser));
            currentUser.setFriends(getUserFriends(currentUser));
            System.out.println("done Accepted");

            return true;
        } catch (SQLException e) {
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean declineRequest(Users currentUser, Users friend) {
        try (PreparedStatement ps = connection.prepareStatement("DELETE from friends WHERE friend_status='Pending' AND user_id=? AND friend_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);) {
            ps.setInt(1, friend.getId());
            ps.setInt(2,currentUser.getId());
            ps.executeUpdate();
            currentUser.setRequest_notifications(getUserNotifications(currentUser));
            System.out.println("done Declined");
            return true;
        } catch (SQLException e) {
            logger.warning(e.getSQLState());
            logger.warning(e.getMessage());
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public ArrayList<Users> getUserOnlineFriends(Users user) {
        ResultSet rs = null;
        ArrayList<Users> OnlineFriendsList = new ArrayList<Users>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT  f.friend_id id ,u2.phone_number,u2.name ,u2.email,u2.password,u2.gender,u2.country,u2.date_of_birth,u2.bio,u2.status,u2.picture" +
                " FROM users u , friends f, users u2 where f.user_id=u.id and" +
                " f.friend_id = u2.id AND f.friend_status='Accepted' and u.id = ? AND u2.status='Available';")){
            ps.setInt(1, user.getId());
            rs = ps.executeQuery();
            while (rs.next())
            {
                OnlineFriendsList.add(extractUserFromResultSet(rs));

            }

        } catch (SQLException ex) {
            ex.printStackTrace();

        }
        return OnlineFriendsList;
    }


    @Override
    public Connection getConnection() throws SQLException {
            Connection conn;
            conn = connectionStrategy.getConnection();
            return conn;

    }
    @Override
    public ArrayList<Users> getAllOnlineUsers() {
        ArrayList<Users> onlineUserslist = new ArrayList<>();
        ResultSet resultSet = null;
        try (PreparedStatement ps = connection.prepareStatement("select id,phone_number,name,email,picture, password,gender,country,date_of_birth,bio,status FROM users  where status='Available' ;")) {
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                onlineUserslist.add(extractUserFromResultSet(resultSet));
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
        return onlineUserslist;
    }



}
