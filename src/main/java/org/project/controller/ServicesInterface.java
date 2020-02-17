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
import java.util.concurrent.atomic.AtomicReference;

public interface ServicesInterface extends Remote {
    public Users getUserData(String phoneNumber) throws RemoteException;

    public Boolean register(Users user) throws RemoteException, SQLException;

    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException;

    public ArrayList<Users> getFriends(String phoneNumber) throws RemoteException;

    public ArrayList<Users> getNotifications(String phoneNumber) throws RemoteException;

    public void notifyUpdate(Users users) throws RemoteException;

    public void sendMessage(Message newMsg, ChatRoom chatRoom) throws RemoteException;

    public void registerClient(ClientInterface clientImp) throws RemoteException;

    public ChatRoom requestChatRoom(ArrayList<Users> chatroomUsers) throws RemoteException;

    public boolean changeUserStatus(Users user, UserStatus userStatus) throws RemoteException;
    public void notifyUser(Message newMsg, ChatRoom chatRoom) throws RemoteException;
    public boolean fileNotifyUser(Message newMsg, ChatRoom chatRoom) throws RemoteException;
    public void sendFile( Message newMsg, RemoteInputStream remoteFileData) throws IOException ,RemoteException;
    // check if phone number exists, update online users
    // start hend


































    //end hend

    //start amr

































    //end amr
    //start iman
















    // end imaN

    //START SHIMAA

































    //END SHIMAA
}
