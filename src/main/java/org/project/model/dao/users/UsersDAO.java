package org.project.model.dao.users;

import org.project.exceptions.UserAlreadyExistException;

import java.util.ArrayList;
import java.util.Map;

//import org.project.model.dao.friends.Friends;

public interface UsersDAO {

    Users login(String phone_number);

    boolean register(Users user) throws UserAlreadyExistException;

    boolean updateUser(Users user);

    ArrayList<Users> getUserFriends(Users user);

    ArrayList<Users> getUserNotifications(Users user);

    boolean isUserExist(String phone_number);

    boolean matchUserNameAndPassword(String phoneNumber, String Password);

    boolean updateStatus(Users user, UserStatus status);

    Map<String, Integer> getUsersNumByCountry();

    Map<String, Integer> getUsersByGender();

    Map<String, Integer> getUsersByStatus();
}
