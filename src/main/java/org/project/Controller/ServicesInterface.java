package org.project.Controller;

import org.project.model.dao.users.Users;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServicesInterface extends Remote{
    public Users login(String phoneNumber,String Password) throws RemoteException;
    public Boolean Register(Users user) throws RemoteException;

}
