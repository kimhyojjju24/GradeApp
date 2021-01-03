package com.example.pojangapp.emoney.timerecycler;

import java.util.Calendar;


public abstract class AdapterItem {
    public static final int TYPE_TIME = 1;
    public static final int TYPE_DATA = 2;

    private long time;
    private String detail, price, currentPrice;
    int isInOut;

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

    public AdapterItem(long time, String detail, String price, String currentPrice, int isInOut) {
        this.time = time;
        this.detail = detail;
        this.price = price;
        this.currentPrice = currentPrice;
        this.isInOut = isInOut;
    }

    public AdapterItem(long time) {
        this.time = time;
    }

    public AdapterItem(int year, int month, int dayOfMonth) {
        setTime(year, month, dayOfMonth);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setTime(int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month-1, dayOfMonth);
        long t = cal.getTimeInMillis();
        time = t;
    }

    public int getYear() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.YEAR);
    }

    public int getMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.MONTH) + 1;
    }

    public int getDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public long getTime() {
        return time;
    }

    public String getTimeToString() {
        return getYear() + "." + getMonth() + "." + getDayOfMonth();
    }

    public abstract int getType();

}
