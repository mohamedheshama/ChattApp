package org.project.model.dao.users;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.project.model.dao.friends.Friends;

import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;

public class Users {
    private int id;
    private String phoneNumber;
    private String name;
    private String email;
    private String password;
    private Gender gender;
    private String country;
    private Date dateOfBirth;
    private String bio;
    private Blob picture;
    private byte[] displayPicture;
    private UserStatus status;
    private ObservableList<Friends> friends = FXCollections.observableArrayList();
    private ObservableList<Friends> request_notifications = FXCollections.observableArrayList();

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

    public ObservableList<Friends> getFriends() {
        return friends;
    }

    public void setFriends(ObservableList<Friends> friends) {
        this.friends = friends;
    }

    public void setPicture(Blob picture) {
        this.picture = picture;
    }

    public byte[] getDisplayPicture() throws SQLException {
        byte displayPicture[] = picture.getBytes(1, (int) picture.length());
        return displayPicture;
    }

    public ObservableList<Friends> getRequest_notifications() {
        return request_notifications;
    }

    public void setRequest_notifications(ObservableList<Friends> request_notifications) {
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
