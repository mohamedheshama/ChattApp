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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServicesImp extends UnicastRemoteObject implements ServicesInterface {
    UsersDAO DAO;
    CopyOnWriteArrayList<ClientInterface> clients;
    CopyOnWriteArrayList<ChatRoom> chatRooms;

    public ServicesImp() throws RemoteException {
        super(1260);
        try {
            DAO = new UsersDAOImpl(MysqlConnection.getInstance());
            clients = new CopyOnWriteArrayList<>();
            chatRooms = new CopyOnWriteArrayList<>();
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
        System.out.println(user + " is registered");
        return DAO.register(user);
    }

    @Override
    public Boolean checkUserLogin(String phoneNumber, String password) throws RemoteException {
        return DAO.matchUserNameAndPassword(phoneNumber, password);
    }

    @Override
    public ArrayList<Users> getFriends(Users user) throws RemoteException {
        return DAO.getUserFriends(user);
    }

    @Override
    public ArrayList<Users> getNotifications(Users user) throws RemoteException {
        return DAO.getUserNotifications(user);
    }

    @Override
    public void notifyUpdate(Users users) throws RemoteException {

    }

    @Override
    public void sendMessage(Message newMsg, ChatRoom chatRoom) throws RemoteException {

        chatRooms.forEach(chatRoom1 -> {
            if (chatRoom1.getChatRoomId().equals(chatRoom.getChatRoomId())){
                chatRoom1.getChatRoomMessage().add(newMsg);
            }
        });
        chatRoom.getUsers().forEach(user -> {
            clients.forEach(clientInterface -> {
                try {
                    if (clientInterface.getUser().getId() == user.getId()) {
                        System.out.println("sending message to " + clientInterface.getUser());
                        clientInterface.recieveMsg(newMsg , chatRoom);
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

    @Override
    public ChatRoom requestChatRoom(ArrayList<Users> chatroomUsers) throws RemoteException {
        List<Integer> collect = chatroomUsers.stream().map(Users::getId).collect(Collectors.toList());
        String chatRoomId = collect.stream().sorted().collect(Collectors.toList()).toString();
        ChatRoom chatRoomExist = checkChatRoomExist(chatRoomId);
        if (chatRoomExist != null){
            return chatRoomExist;
        }
        chatRoomExist = new ChatRoom();
        chatRoomExist.setChatRoomId(chatRoomId);
        chatRoomExist.setUsers(chatroomUsers);
        chatRooms.add(chatRoomExist);
        addChatRoomToAllClients(chatroomUsers , chatRoomExist);
        return chatRoomExist;
    }

    @Override
    public boolean acceptRequest(Users currentUser, Users friend) throws RemoteException {

        return DAO.acceptRequest(currentUser, friend);
    }

    @Override
    public boolean declineRequest(Users currentUser, Users friend) throws RemoteException {
        return DAO.declineRequest(currentUser,friend);
    }


    private void addChatRoomToAllClients(ArrayList<Users> chatroomUsers, ChatRoom chatRoomExist) {
        chatroomUsers.forEach(users -> {
            try {
                getClient(users).addChatRoom(chatRoomExist);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private ChatRoom checkChatRoomExist(String chatroomUser) {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getChatRoomId().equals(chatroomUser)){
                return chatRoom;
            }
        }
        return null;
    }

    public ClientInterface getClient(Users user) {
        for (ClientInterface clientInterface : clients) {
            try {
                if (clientInterface.getUser().getId() == user.getId()) {
                    return clientInterface;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        System.out.println("this user has no life " + user);
        return null;
    }

    public void notifyUpdatedNotifications(ArrayList<Users> users) {
        for (Users user : users) {
            ClientInterface temp = getClient(user);
            if (temp != null) {
                try {
                    temp.recieveUpdatedNotifications(user);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }


    }
    @Override
    public void addUsersToFriedNotifications (List< String > contactList, Users user) throws
            RemoteException {
        DAO.addContactRequest(contactList, user);

    }

    @Override
    public List<String> getUsersList (int userId) throws RemoteException {
        return DAO.getUsersList(userId);
    }

    @Override
    public void notifyRequestedContacts (List < String > ContactList, Users user) throws RemoteException
    {
        ContactList.forEach(contact -> {
            clients.forEach(clientInterface -> {
                try {
                    if (clientInterface.getUser().getId() == user.getId()) {
                        System.out.println("sending notification to " + clientInterface.getUser());
                        clientInterface.recieveContactRequest(user);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });

    }
}







