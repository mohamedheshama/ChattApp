package org.project.controller;

import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAO;
import org.project.model.dao.users.UsersDAOImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class ServicesImp extends UnicastRemoteObject implements ServicesInterface {
    UsersDAO DAO;
    CopyOnWriteArrayList<ClientInterface> clients;
    public ServicesImp() throws RemoteException {
       try {
            DAO= new UsersDAOImpl(MysqlConnection.getInstance());
            clients = new CopyOnWriteArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Users getUserData(String phoneNumber) throws RemoteException {
        return DAO.login(phoneNumber);
    }

    @Override
    public Boolean register(Users user) throws RemoteException, SQLException {
        return DAO.register(user);
    }

    @Override
    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException {
        return DAO.matchUserNameAndPassword(phoneNumber, password);
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

    @Override
    public void sendMessage(Message newMsg, ChatRoom chatRoom)throws RemoteException {
        chatRoom.getUsers().forEach(user -> {
            clients.forEach(clientInterface -> {
                try {
                    if (clientInterface.getUser().getId() == user.getId()){
                        System.out.println("sending message to " + clientInterface.getUser());
                        clientInterface.recieveMsg(newMsg);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public void registerClient(ClientInterface clientImp) throws RemoteException {
        System.out.println("in server register client");
        clients.add(clientImp);
        System.out.println("new Client is assigned" + clientImp.getUser());
    }
}
