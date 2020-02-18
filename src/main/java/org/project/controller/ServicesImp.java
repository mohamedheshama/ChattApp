package org.project.controller;

import com.healthmarketscience.rmiio.RemoteInputStream;
import com.healthmarketscience.rmiio.RemoteInputStreamClient;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicReference;

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
    public boolean fileNotifyUser(Message newMsg, ChatRoom chatRoom) throws RemoteException{
     final boolean[] flage= {true};
    chatRoom.getUsers().forEach(user -> {
        clients.forEach(clientInterface -> {
            try {
                if (clientInterface.getUser().getId() == user.getId()) {
                    System.out.println("sending file to " + clientInterface.getUser());
                 if(clientInterface.notifyrecieveFile(newMsg , chatRoom) ){
                     flage[0]=true;
                 }
                 else {
                     flage[0]=false;
                 }



                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    });

return flage[0];

    }

    @Override
    public void sendFile( Message newMsg, RemoteInputStream remoteFileData) throws IOException,RemoteException {
        InputStream fileData= RemoteInputStreamClient.wrap(remoteFileData);
        System.out.println("server 2 write");
        ReadableByteChannel from = Channels.newChannel(fileData);
        ByteBuffer buffer = ByteBuffer.allocateDirect(fileData.available());

        WritableByteChannel to = FileChannel.open(Paths.get("D:\\iti java\\xmlAPI\\firstTask\\'"+fileData+"'"), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        while (from.read(buffer) != -1)
        {
            buffer.flip();
            while (buffer.hasRemaining()) {
                System.out.println("server write");
                to.write(buffer);
            }
            buffer.clear();
        }
        from.close();
        to.close();
        fileData.close();



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
        clients.add(clientImp);
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

    private ChatRoom checkChatRoomExist(String chatroomUser) {
        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getChatRoomId().equals(chatroomUser)){
                return chatRoom;
            }

        }
        return null;
    }


    @Override
    public boolean changeUserStatus(Users users, UserStatus userStatus) throws RemoteException {
        return  DAO.updateStatus(users, userStatus);
    }
/*
    @Override
    public void notifyUser(Message newMsg, ChatRoom chatRoom) throws RemoteException {

    }
*/
    private void addChatRoomToAllClients(ArrayList<Users> chatroomUsers, ChatRoom chatRoomExist) {
        chatroomUsers.forEach(users -> {
            try {
                getClient(users).addChatRoom(chatRoomExist);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
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
}
