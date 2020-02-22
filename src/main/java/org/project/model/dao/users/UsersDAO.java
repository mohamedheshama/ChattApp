package org.project.model.dao.users;

import javafx.collections.ObservableList;
import org.project.exceptions.UserAlreadyExistException;
//import org.project.model.dao.friends.Friends;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//import org.project.model.dao.friends.Friends;

public interface UsersDAO {

    Users login(String phone_number);

    boolean register(Users user) throws UserAlreadyExistException;

    boolean updateUser(Users user);
    boolean deleteUSer(Users user);

    ArrayList<Users> getUserFriends(Users user);

    ObservableList<Users>getUsers();

    ArrayList<Users>getUserNotifications(Users user);

    boolean isUserExist(String phone_number);

    boolean matchUserNameAndPassword(String phoneNumber, String Password);

    boolean updateStatus(Users user, UserStatus status);

    Map<String, Integer> getUsersNumByCountry();

    Map<String, Integer> getUsersByGender();

    Map<String, Integer> getUsersByStatus();

    boolean acceptRequest(Users currentUser, Users friend);

    boolean declineRequest(Users currentUser, Users friend);

    public ArrayList<Users> getUserOnlineFriends(Users user);


    int getUserIDByPhoneNo(String phoneNo);

    boolean addContactRequest(List<String> contactList, Users user);

    List<String> getUsersList(int userId);
}
