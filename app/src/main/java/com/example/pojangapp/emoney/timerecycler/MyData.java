package com.example.pojangapp.emoney.timerecycler;

public class MyData extends AdapterItem {
    private String detail, price, currentPrice;
    int isInOut;

    public MyData(String storeName, long time) {
        super(time);
        this.detail = storeName;
    }

    public MyData(int year, int month, int dayOfMonth, String price, String currentPrice, int isInOut, String detail) {
        super(year, month, dayOfMonth);
        this.detail = detail;
        this.price = price;
        this.currentPrice = currentPrice;
        this.isInOut = isInOut;
    }

    @Override
    public int getType() {
        return TYPE_DATA;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getIsInOut() {
        return isInOut;
    }

    public void setIsInOut(int isInOut) {
        this.isInOut = isInOut;
    }
}
