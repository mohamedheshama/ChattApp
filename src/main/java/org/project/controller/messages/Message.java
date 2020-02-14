package org.project.controller.messages;


import org.project.model.dao.users.Users;

import java.io.Serializable;

public class Message implements Serializable {

    private String name;
    private MessageType type;
    private String msg;
    private String fontFamily;
    private String textFill;
    private int fontSize;
    private String fontWeight;
    private Users user;
    // private ArrayList<Users> listUsers;
    // private ArrayList<Users> users;
    //private UserStatus status;
    private String picture;

    public Message() {
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getTextFill() {
        return textFill;
    }

    public void setTextFill(String textFill) {
        this.textFill = textFill;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontWeight() {
        return fontWeight;
    }

    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }
   /* public ArrayList<Users> getUserlist() {
        return listUsers;
    }
    public void setUserlist(HashMap<String, Users> userList) {
        this.listUsers = new ArrayList<>(userList.values());
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public ArrayList<Users> getUsers() {
        return users;
    }
    public void setUsers(ArrayList<Users> users) {
        this.users = users;
    }
    public void setStatus(UserStatus status) {
        this.status = status;
    }
    public UserStatus getStatus() {
        return status;
    }*/
}