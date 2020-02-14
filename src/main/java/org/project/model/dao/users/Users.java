package org.project.model.dao.users;

import org.project.model.ChatRoom;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public class Users implements Serializable {
    private int id;
    private String phoneNumber;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String country;
    private Date dateOfBirth;
    private String bio;
    transient private Blob picture;
    transient private byte[] displayPicture;
    private UserStatus status;
    private ArrayList<Users> friends = new ArrayList<>();
    private ArrayList<Users> request_notifications = new ArrayList<>();
    private ArrayList<ChatRoom> chatRooms = new ArrayList<>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }


    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public ArrayList<Users> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<Users> friends) {
        this.friends = friends;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public byte[] getDisplayPicture() throws SQLException {
        byte displayPicture[] = picture.getBytes(1, (int) picture.length());
        return displayPicture;
    }

    public ArrayList<Users> getRequest_notifications() {
        return request_notifications;
    }

    public void setRequest_notifications(ArrayList<Users> request_notifications) {
        this.request_notifications = request_notifications;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
