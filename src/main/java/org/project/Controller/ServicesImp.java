package org.project.Controller;

import org.project.model.dao.users.UsersDAO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServicesImp extends UnicastRemoteObject implements Remote {
    UsersDAO DAO;
    protected ServicesImp() throws RemoteException {

    }
}
