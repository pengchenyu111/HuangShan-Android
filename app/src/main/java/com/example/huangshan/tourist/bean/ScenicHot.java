package com.example.huangshan.tourist.bean;


import java.io.Serializable;

/**
 * 景点实时热度
 * @author pcy
 */

public class ScenicHot implements Serializable {

    private long id;//即为景点Scenic的id
    private double latitude;
    private double longitude;
    private int hotNum;

    public ScenicHot() {
    }

    public ScenicHot(long id, double latitude, double longitude, int hotNum) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hotNum = hotNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getHotNum() {
        return hotNum;
    }

    public void setHotNum(int hotNum) {
        this.hotNum = hotNum;
    }

    @Override
    public String toString() {
        return "ScenicHot{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", hotNum=" + hotNum +
                '}';
    }
}
