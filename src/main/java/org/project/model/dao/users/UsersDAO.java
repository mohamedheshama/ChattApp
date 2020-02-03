package org.project.model.dao.users;

import javafx.collections.ObservableList;
import org.project.model.dao.friends.Friends;

public interface UsersDAO {

    Users login(String phone_number);

    boolean register(Users user);

    boolean updateUser(Users user);

    ObservableList<Friends> getUserFriends(Users user);

    ObservableList<Friends> getUserNotifications(Users user);

    boolean isUserExist(String phone_number);

    boolean updateStatus(Users user, UserStatus status);
}
