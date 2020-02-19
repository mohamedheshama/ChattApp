package org.project.model.dao;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.project.model.connection.ConnectionStrategy;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.Gender;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAOImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws SQLException {
        ConnectionStrategy connectionStrategy = MysqlConnection.getInstance();
        UsersDAOImpl usersDAO = new UsersDAOImpl(connectionStrategy);
        //Map<String,Integer> map = usersDAO.getUsersNumByCountry();
        //List<String> contactListViewCell = usersDAO.getUsersList(1);

        /*for(String string: contactListViewCell){
            System.out.println("kkkkkkkkkkk"+string);
        }*/

        // Users user = usersDAO.login("0102354663");
        //ystem.out.println(user);

        /*Users user2 = new Users();
        user2.setName("naruto");
        user2.setPhoneNumber("01091390545");
        user2.setStatus(UserStatus.Available);
        user2.setGender(Gender.Male);
        System.out.println(usersDAO.register(user2));*/

        Users user3 = new Users();
        user3.setPhoneNumber("01065001124");
        user3.setName("test");
        user3.setEmail("test@test.com");
        user3.setGender(Gender.Female);
        user3.setStatus(UserStatus.Busy);
        user3.setPassword("hello");
        System.out.println(usersDAO.updateUser(user3));
    }

}
