package org.project.controller;

import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.dao.users.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ServicesInterface extends Remote {
    public Users getUserData(String phoneNumber) throws RemoteException;

    public Boolean register(Users user) throws RemoteException, SQLException;

    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException;

    public ArrayList<Users> getFriends(Users users) throws RemoteException;

    public ArrayList<Users> getNotifications(Users users) throws RemoteException;

    public void notifyUpdate(Users users) throws RemoteException;

    public void sendMessage(Message newMsg, ChatRoom chatRoom) throws RemoteException;

    public void registerClient(ClientInterface clientImp) throws RemoteException;

    public ChatRoom requestChatRoom(ArrayList<Users> chatroomUsers) throws RemoteException;

    boolean acceptRequest(Users currentUser, Users friend) throws RemoteException;

    boolean declineRequest(Users currentUser, Users friend) throws RemoteException;

    public void notifyUpdatedNotifications(ArrayList<Users> users) throws RemoteException;

    void addUsersToFriedNotifications(List<String> contactList, Users user) throws RemoteException;

    List<String> getUsersList(int userId) throws RemoteException;

    void notifyRequestedContacts(List<String> ContactList, Users user) throws RemoteException;
    // check if phone number exists, update online users
}
