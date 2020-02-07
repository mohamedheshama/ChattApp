package org.project.Controller;

import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAO;
import org.project.model.dao.users.UsersDAOImpl;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

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
    public Users login(String phoneNumber, String password) throws RemoteException {
        return DAO.login(phoneNumber,password);
    }

    @Override
    public Boolean Register(Users user) throws RemoteException {
        return DAO.register(user);
    }
}
