package com.example.pojangapp.emoney.timerecycler;

public class TimeItem extends AdapterItem {

    public TimeItem(long time) {
        super(time);
    }

    public TimeItem(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    @Override
    public int getType() {
        return TYPE_TIME;
    }
}
