package com.example.pojangapp.main;

public class Store {

    static String storeName;
    static String storeNum;
    static double lat;
    static double lng;

    public static String getStoreName() {
        return storeName;
    }

    public static void setStoreName(String storeName) {
        Store.storeName = storeName;
    }

    public static String getStoreNum() {
        return storeNum;
    }

    public static void setStoreNum(String storeNum) {
        Store.storeNum = storeNum;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLat(double lat) {
        Store.lat = lat;
    }

    public static double getLng() {
        return lng;
    }

    public static void setLng(double lng) {
        Store.lng = lng;
    }
}
