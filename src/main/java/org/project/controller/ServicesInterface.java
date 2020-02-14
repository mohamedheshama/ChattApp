package org.project.controller;

import org.project.model.dao.users.Users;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface ServicesInterface extends Remote {
    public Users getUserData(String phoneNumber) throws RemoteException;

    public Boolean register(Users user) throws RemoteException, SQLException;

    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException;

    public ArrayList<Users> getFriends(String phoneNumber) throws RemoteException;

    public ArrayList<Users> getNotifications(String phoneNumber) throws RemoteException;

    public void notifyUpdate(Users users) throws RemoteException;
    // check if phone number exists, update online users
}
