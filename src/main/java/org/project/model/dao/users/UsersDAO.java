package org.project.model.dao.users;

import javafx.collections.ObservableList;
//import org.project.model.dao.friends.Friends;

import java.util.ArrayList;

public interface UsersDAO {

    Users login(String phone_number,String password);

    boolean register(Users user);

    boolean updateUser(Users user);

    ArrayList<Users>getUserFriends(Users user);

    ArrayList<Users>getUserNotifications(Users user);

    boolean isUserExist(String phone_number);

    boolean updateStatus(Users user, UserStatus status);
}
