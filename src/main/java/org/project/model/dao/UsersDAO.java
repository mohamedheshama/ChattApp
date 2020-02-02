package org.project.model.dao;

import javafx.collections.ObservableList;

public interface UsersDAO {

    Users login(String phone_number, String password);

    boolean register(Users user);

    boolean updateUser(Users user);

    ObservableList<Friends> getUserFriends(Users user);

    ObservableList<Friends> getUserNotifications(Users user);

    boolean userExist(String phone_number);

    boolean updateStatus(Users user, UserStatus status);


}
