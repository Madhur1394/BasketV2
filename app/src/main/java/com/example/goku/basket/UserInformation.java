package com.example.goku.basket;

/**
 * Created by goku on 8/5/17.
 */

class UserInformation {
    String userId;
    String name;
    String email;
    String password;
    String phoneNo;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    String photoUrl;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public UserInformation() {

    }

    public UserInformation(String userId, String name, String email, String password, String phoneNo,String photoUrl) {

        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.photoUrl = photoUrl;
    }
}
