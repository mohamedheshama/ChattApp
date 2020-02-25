package org.project.controller.messages;


import org.project.model.dao.users.Users;

import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {

    private String name;
    private MessageType type;
    private String msg;
    private String fontFamily;

    public String getFontPosture() {
        return fontPosture;
    }

    public void setFontPosture(String fontPosture) {
        this.fontPosture = fontPosture;
    }

    private String textFill;
    private int fontSize;
    private String fontWeight;
    private String fontPosture;
    private Users user;
    private String chatId;
    PublicKey publicKey;
    String encryptedAESKeyString;
    private byte[] voiceMsg;

    public byte[] getVoiceMsg() {
        return voiceMsg;
    }

    public void setVoiceMsg(byte[] voiceMsg) {
        this.voiceMsg = voiceMsg;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedAESKeyString() {
        return encryptedAESKeyString;
    }

    public void setEncryptedAESKeyString(String encryptedAESKeyString) {
        this.encryptedAESKeyString = encryptedAESKeyString;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
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

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    // private ArrayList<Users> listUsers;
    // private ArrayList<Users> users;
    //private UserStatus status;
    private String picture;

    public Message() {
    }

    public String getPicture() {
        return picture;
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
