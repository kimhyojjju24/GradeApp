package com.example.pojangapp.storedetail.bus;

public class BusItem extends AdapterItem {

    private String busName;

    public BusItem(String busStopName, String busStopNum, String busName) {
        super(busStopName, busStopNum);
        this.busName = busName;
    }

    @Override
    public int getType() {
        return TYPE_BUS;
    }

    @Override
    public String getBusName() {
        return busName;
    }

    @Override
    public void setBusName(String busName) {
        this.busName = busName;
    }

}
