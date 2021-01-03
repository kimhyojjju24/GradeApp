package com.example.pojangapp.mypage;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyItem {

    String buyNum;
    String menuName;
    String menuCnt;
    String menuPrice;

    public BuyItem() {
    }

    public BuyItem(String buyNum, String menuName, String menuCnt, String menuPrice) {
        this.buyNum = buyNum;
        this.menuName = menuName;
        this.menuCnt = menuCnt;
        this.menuPrice = menuPrice;
    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCnt() {
        return menuCnt;
    }

    public void setMenuCnt(String menuCnt) {
        this.menuCnt = menuCnt;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }
}
