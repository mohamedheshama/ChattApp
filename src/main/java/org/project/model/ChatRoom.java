package org.project.model;

import org.project.controller.messages.Message;
import org.project.model.dao.users.Users;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatRoom implements Serializable {
    private String chatRoomId;
    private ArrayList<Users> users = new ArrayList<>();
    private ArrayList<Message> chatRoomMessage = new ArrayList<>();
    public ChatRoom() {
    }
    public ChatRoom(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
    public ArrayList<Message> getChatRoomMessage() {
        return chatRoomMessage;
    }
    public void setChatRoomMessage(ArrayList<Message> chatRoomMessage) {
        this.chatRoomMessage = chatRoomMessage;
    }
    public String getChatRoomId() {
        return chatRoomId;
    }
    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
    public ArrayList<Users> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }
}
