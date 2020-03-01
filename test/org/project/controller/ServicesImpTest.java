package org.project.controller;

import org.junit.jupiter.api.Test;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.UsersDAOImpl;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ServicesImpTest {
UsersDAOImpl usersDAO;
    private ConnectionStrategy connectionStrategy;
    {
        connectionStrategy = MysqlConnection.getInstance();
        try {
            usersDAO = new UsersDAOImpl(connectionStrategy);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getUserData() {
    }

    @Test
    void register() {
    }

    @Test
    void checkUserLogin() {
    }

    @Test
    void getFriends() {
    }

    @Test
    void getNotifications() {
    }

    @Test
    void notifyUpdate() {
    }

    @Test
    void sendFile() {
    }

    @Test
    void sendMessage() {
    }

    @Test
    void registerClient() {
    }

    @Test
    void requestChatRoom() {
    }

    @Test
    void changeUserStatus() {
    }

    @Test
    void fileNotifyUser() {
    }

    @Test
    void acceptRequest() {
    }

    @Test
    void declineRequest() {
    }

    @Test
    void getClient() {
    }

    @Test
    void notifyUpdatedNotifications() {
    }

    @Test
    void addUsersToFriedNotifications() {
    }

    @Test
    void getUsersList() {
    }

    @Test
    void notifyRequestedContacts() {
    }

    @Test
    void getUserOnlineFriends() {
    }

    @Test
    void notifyNewGroup() {
    }

    @Test
    void logout() {
    }

    @Test
    void fileSendAccepted() {
    }

    @Test
    void setverIsAlive() {
    }

    @Test
    void updateCurrentUserIcon() {
    }

    @Test
    void notifyServerisDown() {
    }

    @Test
    void notifyServerisup() {
    }

    @Test
    void updateStatus() {
    }

    @Test
    void sendMessageFromAdminToOnlineUsers() {
    }
}