package org.project.controller;

import org.project.controller.messages.Message;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    public Users getUser() throws RemoteException;

    public void recieveUpdateStatus(UserStatus status, int id) throws RemoteException;

    void recieveMsg(Message newMsg) throws RemoteException;
}
