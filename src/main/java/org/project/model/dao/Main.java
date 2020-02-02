package org.project.model.dao;


import javafx.collections.ObservableList;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    Connection connection = null;
    PreparedStatement ptmt = null;
    ResultSet resultSet = null;
    ConnectionStrategy connectionStrategy;
    UsersDAOImpl table = null;

    public Main(ConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

    private Connection getConnection() throws SQLException {
        Connection conn;
        conn = connectionStrategy.getConnection();
        return conn;
    }


    public static void main(String[] args) {
        ConnectionStrategy connectionStrategy = new MysqlConnection();
        Main main = new Main(connectionStrategy);
        main.login();
        main.Register();
        main.update();
        // write your code here
    }

    public void login() {
        try {
            connection = getConnection();
            table = new UsersDAOImpl(connection);
            Users user = table.login("01065001124", "iman");
            if (user != null) {
                ObservableList<Friends> friends = user.getFriends();
                for (Friends friend : friends) {
                    System.out.println(friend.getFriend().getId() + " " + friend.getFriend().getName());

                }
                ObservableList<Friends> notifications = user.getRequest_notifications();

                for (Friends friend : notifications) {
                    System.out.println(friend.getFriend().getId() + " " + friend.getFriend().getName());

                }
                System.out.println(user.getId() + " " + user.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Register() {
        Users user2 = new Users();
        user2.setName("Alyaa");
        user2.setPhoneNumber("0102354663");
        user2.setStatus(UserStatus.Available);
        user2.setGender(Gender.Female);
        if (!table.userExist(user2.getPhoneNumber()))
            table.register(user2);

    }

    public void update() {
        Users user3 = new Users();
        user3.setPhoneNumber("01065001124");
        user3.setId(1);
        user3.setGender(Gender.Female);
        user3.setStatus(UserStatus.Busy);
        user3.setPassword("hello");
        table.updateUser(user3);

    }


}
