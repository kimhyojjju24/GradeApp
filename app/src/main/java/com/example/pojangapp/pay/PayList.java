package com.example.pojangapp.pay;

import java.util.ArrayList;

import kr.co.bootpay.model.Item;

public class PayList {
    public static String mainTitle;
    public static String subTitle;
    public static double totalPrice;
    public static double payPrice;
    public static ArrayList<Item> menuList;

    public static ArrayList<Item> getMenuList() {
        return menuList;
    }

    public static double getPayPrice() {
        return payPrice;
    }

    public static void setPayPrice(double payPrice) {
        PayList.payPrice = payPrice;
    }

    public static void setMenuList(ArrayList<Item> menuList) {
        PayList.menuList = menuList;
    }

    public static String getMainTitle() {
        return mainTitle;
    }

    public static void setMainTitle(String mainTitle) {
        PayList.mainTitle = mainTitle;
    }

    public static String getSubTitle() {
        return subTitle;
    }

    public static void setSubTitle(String subTitle) {
        PayList.subTitle = subTitle;
    }

    public static double getTotalPrice() {
        return totalPrice;
    }

    public static void setTotalPrice(double totalPrice) {
        PayList.totalPrice = totalPrice;
    }

    public static void remove(int position){
        menuList.remove(position);
    }
}
