package org.project.controller;

import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public Users getUser() throws RemoteException;

    public void recieveUpdateStatus(UserStatus status, int id) throws RemoteException;

    void recieveMsg(Message newMsg , ChatRoom chatRoom) throws RemoteException;

    void addChatRoom(ChatRoom chatRoomExist) throws RemoteException;
    // start hend

    public boolean notifyrecieveFile(Message newMsg, ChatRoom chatRoom) throws RemoteException;
































    //end hend

    //start amr

































    //end amr
    //start iman
















    // end imaN

    //START SHIMAA

































    //END SHIMAA
}
