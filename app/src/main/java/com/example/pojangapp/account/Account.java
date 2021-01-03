package com.example.pojangapp.account;


public class Account {

    public static String userId;
    public static String userImage;
    public static String userNickname;
    public static String userPhone;
    public static String userEmoney;

    public static String getUserPhone() {
        return userPhone;
    }

    public static void setUserPhone(String userPhone) {
        Account.userPhone = userPhone;
    }

    public static String getUserEmoney() {
        return userEmoney;
    }

    public static void setUserEmoney(String userEmoney) {
        Account.userEmoney = userEmoney;
    }

    public static String getUser_id() {
        return userId;
    }

    public static void setUser_id(String user_id) {
        Account.userId = user_id;
    }

    public static String getUserImage() {
        return userImage;
    }

    public static void setUserImage(String userImage) {
        Account.userImage = userImage;
    }

    public static String getUserNickname() {
        return userNickname;
    }

    public static void setUserNickname(String userNickname) {
        Account.userNickname = userNickname;
    }
}
