package org.project.controller;

import com.healthmarketscience.rmiio.RemoteInputStream;
import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;

import java.io.IOException;
import java.rmi.NotBoundException;
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

    public boolean changeUserStatus(Users user, UserStatus userStatus) throws RemoteException;
    public void fileNotifyUser(Message newMsg, ChatRoom chatRoom,int userSendFileId) throws RemoteException;
    // check if phone number exists, update online users
    // start hend



    public void sendFile( String newMsg, RemoteInputStream remoteFileData,ChatRoom chatRoom,int userSendFileId)throws RemoteException , IOException, NotBoundException;

//public  void sendAcceptToServer(boolean check);





























    //end hend

    //start amr

































    //end amr
    //start iman

    boolean acceptRequest(Users currentUser, Users friend) throws RemoteException;

    boolean declineRequest(Users currentUser, Users friend) throws RemoteException;

    public void notifyUpdatedNotifications(ArrayList<Users> users) throws RemoteException;














    // end imaN

    //START SHIMAA
    void addUsersToFriedNotifications(List<String> contactList, Users user) throws RemoteException;

    List<String> getUsersList(int userId) throws RemoteException;
    void notifyRequestedContacts(List<String> ContactList, Users user) throws RemoteException;

    public ArrayList<Users> getUserOnlineFriends(Users user) throws RemoteException;
    void updateStatus(Users user, UserStatus newStatus) throws RemoteException;
    public void sendMessageFromAdminToOnlineUsers(Message newMsg, ArrayList<Users> onlineUsersList) throws RemoteException;

    void notifyNewGroup(ArrayList<Users> groupUsers, ChatRoom currentChatRoom) throws RemoteException;

    boolean logout(Users user) throws RemoteException;

    void notifyServerisDown() throws RemoteException;

    void notifyServerisup() throws RemoteException;

    public void fileSendAccepted(Users chatRoom) throws RemoteException;

    void setverIsAlive() throws RemoteException;

    void updateCurrentUserIcon(Users currentUser) throws RemoteException;

    //END SHIMAA
}
