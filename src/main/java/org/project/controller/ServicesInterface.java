package org.project.controller;

import com.healthmarketscience.rmiio.RemoteInputStream;
import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public interface ServicesInterface extends Remote {
    public Users getUserData(String phoneNumber) throws RemoteException;

    public Boolean register(Users user) throws RemoteException, SQLException;

    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException;

    public ArrayList<Users> getFriends(Users user) throws RemoteException;

    public ArrayList<Users> getNotifications(Users user) throws RemoteException ;

    public void notifyUpdate(Users users) throws RemoteException;

    public void sendMessage(Message newMsg, ChatRoom chatRoom) throws RemoteException;

    public void registerClient(ClientInterface clientImp) throws RemoteException;

    public ChatRoom requestChatRoom(ArrayList<Users> chatroomUsers) throws RemoteException;
    public boolean changeUserStatus(Users user, UserStatus userStatus) throws RemoteException;
    public boolean fileNotifyUser(Message newMsg, ChatRoom chatRoom) throws RemoteException;
    // check if phone number exists, update online users
    // start hend



    public void sendFile( Message newMsg, RemoteInputStream remoteFileData)throws RemoteException;


    public boolean changeUserStatus(Users user, UserStatus userStatus) throws RemoteException;

    public void fileNotifyUser(Message newMsg, ChatRoom chatRoom,int userSendFileId) throws RemoteException;
    public void sendFile( String newMsg, RemoteInputStream remoteFileData,ChatRoom chatRoom,int userSendFileId) throws IOException ,RemoteException;
    // check if phone number exists, update online users
    // start hend


































    //end hend

    //start amr

































    //end amr
    //start iman
















    // end imaN

    //START SHIMAA
    void addUsersToFriedNotifications(List<String> contactList, Users user) throws RemoteException;

    List<String> getUsersList(int userId) throws RemoteException;
    void notifyRequestedContacts(List<String> ContactList, Users user) throws RemoteException;

    boolean acceptRequest(Users currentUser, Users friend) throws RemoteException;

    boolean declineRequest(Users currentUser, Users friend) throws RemoteException;

    public void notifyUpdatedNotifications(ArrayList<Users> users) throws RemoteException;

    void addUsersToFriedNotifications(List<String> contactList, Users user) throws RemoteException;

    List<String> getUsersList(int userId) throws RemoteException;

    void notifyRequestedContacts(List<String> ContactList, Users user) throws RemoteException;
   //START SHIMAA
    void updateStatus(Users user, UserStatus newStatus) throws RemoteException;




    public ArrayList<Users> getUserOnlineFriends(Users user) throws RemoteException;
}
