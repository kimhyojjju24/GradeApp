package com.example.pojangapp.storedetail.bus;

public abstract class AdapterItem {

    public static final int TYPE_BUS_STOP = 1;
    public static final int TYPE_BUS = 2;

    private String busStopName, busName, busStopNum;

    public AdapterItem(String busStopName, String busName, String busStopNum) {
        this.busStopName = busStopName;
        this.busName = busName;
        this.busStopNum = busStopNum;
    }

    public AdapterItem(String busStopName, String busStopNum) {
        this.busStopName = busStopName;
        this.busStopNum = busStopNum;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusStopNum() {
        return busStopNum;
    }

    public void setBusStopNum(String busStopNum) {
        this.busStopNum = busStopNum;
    }

    public abstract int getType();

}
