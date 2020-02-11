package org.project.controller;

import org.project.exceptions.UserAlreadyExistException;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAO;
import org.project.model.dao.users.UsersDAOImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServicesImp extends UnicastRemoteObject implements ServicesInterface {
    UsersDAO DAO=null;
    public ServicesImp() throws RemoteException {
       try {
            DAO= new UsersDAOImpl(MysqlConnection.getInstance());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Users getUserData(String phoneNumber) throws RemoteException {
        return DAO.login(phoneNumber);
    }

    @Override
    public Boolean Register(Users user) throws RemoteException, UserAlreadyExistException {
        return DAO.register(user);
    }

    @Override
    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<Users> getFriends(String phoneNumber) throws RemoteException {
        return null;
    }

    @Override
    public ArrayList<Users> getNotifications(String phoneNumber) throws RemoteException {
        return null;
    }

    @Override
    public void notifyUpdate(Users users) throws RemoteException {

    }
}
