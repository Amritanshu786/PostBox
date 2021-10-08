package com.amritanshu.whatsappclone.Models;

public class Users {
    String profilePic, userName, mail, password, userId, lastMessage, phoneNumber, status;
    boolean isVerified = false;

    public Users(String profilePic, String userName, String mail, String password, String userId, String lastMessage, String phoneNumber, String status, boolean isVerified) {
        this.profilePic = profilePic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.isVerified = isVerified;
    }

    public Users()
    { }

    // SignUp Constructor
    public Users(String userName, String mail, String password, boolean isVerified) {
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.isVerified = isVerified;
    }

    public boolean getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //Phone Constructor
    public Users(String userName, String phoneNumber)
    {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }


}
