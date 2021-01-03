package com.example.pojangapp.pay;

public class BuyList {

    public String buyName;
    public String buyCnt;
    public String buyNum;
    public String buyPrice;
    public String storeCate;
    public String storeName;
    public String storeSubCate;

    public BuyList(String buyName, String buyCnt, String buyNum, String buyPrice, String storeCate, String storeName, String storeSubCate) {
        this.buyName = buyName;
        this.buyCnt = buyCnt;
        this.buyNum = buyNum;
        this.buyPrice = buyPrice;
        this.storeCate = storeCate;
        this.storeName = storeName;
        this.storeSubCate = storeSubCate;
    }

    public String getBuyName() {
        return buyName;
    }

    public void setBuyName(String buyName) {
        this.buyName = buyName;
    }

    public String getBuyCnt() {
        return buyCnt;
    }

    public void setBuyCnt(String buyCnt) {
        this.buyCnt = buyCnt;
    }

    public String getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(String buyNum) {
        this.buyNum = buyNum;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getStoreCate() {
        return storeCate;
    }

    public void setStoreCate(String storeCate) {
        this.storeCate = storeCate;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreSubCate() {
        return storeSubCate;
    }

    public void setStoreSubCate(String storeSubCate) {
        this.storeSubCate = storeSubCate;
    }
}
