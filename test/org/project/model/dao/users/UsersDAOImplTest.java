package org.project.model.dao.users;

import org.junit.jupiter.api.Test;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UsersDAOImplTest {


    private UsersDAOImpl usersDAO;
    private ConnectionStrategy connectionStrategy;

    Connection connect;

    {
        connectionStrategy = MysqlConnection.getInstance();
        try {
            usersDAO = new UsersDAOImpl(connectionStrategy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Test
    void login() {
        Users users=new Users();
        users.setName("mohaaaa00");
        users.setPhoneNumber("01017274954");
       Users users1= usersDAO.login("01017274954");
        assertEquals(users.getName(),users1.getName());

    }

    @Test
    void register() {
       // matchUserNameAndPassword();

    }

    @Test
    void updateUser() {
    }

    @Test
    void updatePicture() {
    }

    @Test
    void deleteUSer() {
    }

    @Test
    void getUserFriends() {
    }

    @Test
    void getUsers() {

    }

    @Test
    void getUserNotifications() {
    }

    @Test
    void isUserExist() {
    }

    @Test
    void matchUserNameAndPassword() {
        Users users=new Users();
        users.setName("01017274954");
        users.setPassword("Mhesham010123*");


        assertEquals(true,usersDAO.matchUserNameAndPassword("01017274954","Mhesham010123*"));

    }

    @Test
    void updateStatus() {
    }

    @Test
    void getUserIDByPhoneNo() {
        Users users=new Users();
        users.setId(1);
        users.setPhoneNumber("01017274954");
        int id=usersDAO.getUserIDByPhoneNo("01017274954");
        assertEquals(1,id);
    }

    @Test
    void getUsersNumByCountry() {
    }

    @Test
    void getUsersByGender() {
    }

    @Test
    void getUsersByStatus() {
    }

    @Test
    void addContactRequest() {
    }

    @Test
    void getUsersList() {
    }

    @Test
    void acceptRequest() {
    }

    @Test
    void declineRequest() {
    }

    @Test
    void getUserOnlineFriends() {
    }

    @Test
    void getConnection() throws SQLException {
        connect=connectionStrategy.getConnection();
        Connection conn;
        conn = usersDAO.getConnection();
        assertNotSame(connect,conn);
    }

    @Test
    void getAllOnlineUsers() {
    }
}