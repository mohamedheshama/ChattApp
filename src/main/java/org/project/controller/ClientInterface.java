package org.project.controller;

import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface ClientInterface extends Remote {
    public Users getUser() throws RemoteException;

    public void recieveUpdateStatus(UserStatus status, int id) throws RemoteException;


    void recieveMsg(Message newMsg , ChatRoom chatRoom) throws RemoteException;
    void recieveFile(Message newMsg,ChatRoom chatRoom)throws RemoteException;
    void addChatRoom(ChatRoom chatRoomExist) throws RemoteException;

    void recieveUpdatedNotifications(Users user) throws RemoteException;
    // start hend


































    //end hend

    //start amr

































    //end amr
    //start iman
















    // end imaN

    //START SHIMAA


    void recieveContactRequest(Users user) throws  RemoteException;
    public void recieveMsgFromAdmin(Message newMsg, Users onlineUser) throws RemoteException;
    //END SHIMAA
}