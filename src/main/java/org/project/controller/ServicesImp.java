package org.project.controller;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
import com.healthmarketscience.rmiio.RemoteInputStream;
import org.project.controller.messages.Message;
import org.project.model.ChatRoom;
import org.project.model.connection.MysqlConnection;
import org.project.model.dao.users.UserStatus;
import org.project.model.dao.users.Users;
import org.project.model.dao.users.UsersDAO;
import org.project.model.dao.users.UsersDAOImpl;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicReference;

public class ServicesImp extends UnicastRemoteObject implements ServicesInterface {
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    UsersDAO DAO;
    CopyOnWriteArrayList<ClientInterface> clients;
    CopyOnWriteArrayList<ChatRoom> chatRooms;

    public ServicesImp() throws RemoteException {
        super(1260);
        try {
            DAO = new UsersDAOImpl(MysqlConnection.getInstance());
            clients = new CopyOnWriteArrayList<>();
            chatRooms = new CopyOnWriteArrayList<>();
            scheduledExecutorService.scheduleAtFixedRate(() -> {
                System.out.println(clients.size());
                if (clients != null){
                    clients.forEach(clientInterface -> {
                        if (!clientIsAlive(clientInterface)) {
                            clients.remove(clientInterface);
                            System.out.println(" user is killed : ");
                        }
                    });
                }

            },0, 1 , TimeUnit.SECONDS);
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
        System.out.println("cheking user login" + phoneNumber + password);
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
        System.out.println("check user in serviece imp notify methode " + users);
        DAO.updateUser(users);

    }
    /*@Override
    public void sendAcceptToServer(boolean check){
        ClientInterface clientInterface = null;
        if(check==true){
            clientInterface.sendAccept(check);
        }
    }*/


    @Override
    public void sendFile(String newMsg, RemoteInputStream remoteFileData, ChatRoom chatRoom, int userSendFileId) throws IOException, RemoteException {
        chatRoom.getUsers().forEach(user -> {
            if (user.getId() != userSendFileId) {
                InputStream fileData = null;
                try {
                    fileData = RemoteInputStreamClient.wrap(remoteFileData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("server 2 write");
                ReadableByteChannel from = Channels.newChannel(fileData);
                ByteBuffer buffer = null;
                try {
                    buffer = ByteBuffer.allocateDirect(fileData.available());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                WritableByteChannel to = null;
                try {
                    String home = System.getProperty("user.home");
                    to = FileChannel.open(Paths.get(home + "/Downloads/" + newMsg), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        if (!(from.read(buffer) != -1)) break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        System.out.println("server write");
                        try {
                            to.write(buffer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    buffer.clear();
                }
                try {
                    from.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    to.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileData.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });


    }


    @Override
    public void sendMessage(Message newMsg, ChatRoom chatRoom) throws RemoteException {

        chatRooms.forEach(chatRoom1 -> {
            if (chatRoom1.getChatRoomId().equals(chatRoom.getChatRoomId())) {
                chatRoom1.getChatRoomMessage().add(newMsg);
            }
        });
        chatRoom.getUsers().forEach(user -> {
            clients.forEach(clientInterface -> {
                try {
                    if (clientInterface.getUser().getId() == user.getId()) {
                        System.out.println("sending message to " + clientInterface.getUser());
                        clientInterface.recieveMsg(newMsg, chatRoom);
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
        if (chatRoomExist != null) {
            return chatRoomExist;
        }
        chatRoomExist = new ChatRoom();
        chatRoomExist.setChatRoomId(chatRoomId);
        chatRoomExist.setUsers(chatroomUsers);
        chatRooms.add(chatRoomExist);
        addChatRoomToAllClients(chatroomUsers, chatRoomExist);
        return chatRoomExist;
    }

    @Override
    public boolean changeUserStatus(Users users, UserStatus userStatus) throws RemoteException {
        return DAO.updateStatus(users, userStatus);
    }

    @Override
    public void fileNotifyUser(Message newMsg, ChatRoom chatRoom, int userSendFileId) throws RemoteException {

    }
/*
    @Override
    public void notifyUser(Message newMsg, ChatRoom chatRoom) throws RemoteException {

    }
*/

    @Override
    public boolean acceptRequest(Users currentUser, Users friend) throws RemoteException {

        return DAO.acceptRequest(currentUser, friend);
    }

    @Override
    public boolean declineRequest(Users currentUser, Users friend) throws RemoteException {
        return DAO.declineRequest(currentUser, friend);
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
            if (chatRoom.getChatRoomId().equals(chatroomUser)) {
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
    public void addUsersToFriedNotifications(List<String> contactList, Users user) throws
            RemoteException {
        DAO.addContactRequest(contactList, user);

    }

    @Override
    public List<String> getUsersList(int userId) throws RemoteException {
        return DAO.getUsersList(userId);
    }

    @Override
    public void notifyRequestedContacts(List<String> ContactList, Users user) throws RemoteException {
        System.out.println("hello from notify contacts");
        System.out.println("contacts list" + ContactList);
        ContactList.forEach(contact -> {
            clients.forEach(clientInterface -> {
                try {
                    System.out.println("contact" + contact);
                    System.out.println("client Interface" + clientInterface.getUser().getPhoneNumber());
                    if (clientInterface.getUser().getPhoneNumber().equals(contact)) {
                        System.out.println("sending notification to " + clientInterface.getUser());
                        clientInterface.recieveUpdatedNotifications(clientInterface.getUser());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    @Override
    public ArrayList<Users> getUserOnlineFriends(Users user) {
        return DAO.getUserOnlineFriends(user);
    }

    @Override
    public void notifyNewGroup(ArrayList<Users> groupUsers, ChatRoom currentChatRoom) throws RemoteException {

        for (Users user : groupUsers) {
            System.out.println("friends for: " + user.getName() + " are -->" + user.getFriends());
            ClientInterface temp = getClient(user);
            if (temp != null) {
                System.out.println("recieve new group chat for" + user);
                temp.recieveNewGroupChat(user, currentChatRoom);
            }

        }

    }

    @Override
    public boolean logout(Users user) throws RemoteException {
        updateStatus(user, UserStatus.valueOf("Offline"));
        System.out.println("user : " + user.getName() + " is now offline");
        notifyUpdatedNotifications(user.getFriends());
        System.out.println("now user Friends are supposed to be notified");
        ClientInterface clientInterface = getClient(user);
        List<ChatRoom> groupChatRooms = chatRooms.stream().filter(chatRoom -> chatRoom.getUsers().size() > 2).collect(Collectors.toList());
        groupChatRooms.stream().forEach(chatRoom -> {
            chatRoom.getUsers().remove(user);
            notifyUserLoggedOut(chatRoom, user);
        });
        System.out.println("count of the server clients " + clients.size());
        return clients.remove(clientInterface);
    }

    @Override
    public void notifyServerisDown() throws RemoteException {
        for (ClientInterface client : clients) {
            client.recieveServerDown();

        }
    }

    @Override
    public void notifyServerisup() throws RemoteException {
        for (ClientInterface client : clients) {
            client.recieveServerUp();
            if (clientIsAlive(client)){
                clients.remove(client);
            }
        }
    }

    private boolean clientIsAlive(ClientInterface client) {
       // boolean isAlive = true;
        try {
            client.isAlive();
            System.out.println("user was alive and now he is logged of");
            return true;
        } catch (RemoteException e) {
            //e.printStackTrace();
            return false;
        }
    }

    private void notifyUserLoggedOut(ChatRoom chatRoom, Users user) {
        chatRoom.getUsers().forEach(users -> {
            ClientInterface clientInterface = getClient(users);
            try {
                clientInterface.notifyUserLoggedOut(user);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void updateStatus(Users user, UserStatus newStatus) throws RemoteException {
        DAO.updateStatus(user, newStatus);
    }

    @Override
    public void sendMessageFromAdminToOnlineUsers(Message newMsg, ArrayList<Users> onlineUsersList) throws RemoteException {
        onlineUsersList.forEach(onlineUser -> {
            clients.forEach(clientInterface -> {
                try {
                    System.out.println("onlineUser" + onlineUser);
                    System.out.println("client Interface" + clientInterface.getUser().getPhoneNumber());
                    if (clientInterface.getUser().getId() == onlineUser.getId()) {
                        System.out.println("sending message to " + clientInterface.getUser());
                        clientInterface.recieveMsgFromAdmin(newMsg, clientInterface.getUser());
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
