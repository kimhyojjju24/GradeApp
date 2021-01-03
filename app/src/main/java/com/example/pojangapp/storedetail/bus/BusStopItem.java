package com.example.pojangapp.storedetail.bus;

public class BusStopItem extends AdapterItem {

    public BusStopItem(String busStopName, String busStopNum) {
        super(busStopName, busStopNum);
    }

    @Override
    public int getType() {
        return TYPE_BUS_STOP;
    }
}
