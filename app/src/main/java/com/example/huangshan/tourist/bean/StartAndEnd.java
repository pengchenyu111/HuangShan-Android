package com.example.huangshan.tourist.bean;

import java.io.Serializable;

public class StartAndEnd implements Serializable {

    private String currentAddressName;
    private double currentLatitude;
    private double currentLongitude;
    private String targetAddressName;
    private double targetLatitude;
    private double targetLongitude;

    public StartAndEnd() {
    }

    public StartAndEnd(String currentAddressName, double currentLatitude, double currentLongitude, String targetAddressName, double targetLatitude, double targetLongitude) {
        this.currentAddressName = currentAddressName;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.targetAddressName = targetAddressName;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
    }

    public String getCurrentAddressName() {
        return currentAddressName;
    }

    public void setCurrentAddressName(String currentAddressName) {
        this.currentAddressName = currentAddressName;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getTargetAddressName() {
        return targetAddressName;
    }

    public void setTargetAddressName(String targetAddressName) {
        this.targetAddressName = targetAddressName;
    }

    public double getTargetLatitude() {
        return targetLatitude;
    }

    public void setTargetLatitude(double targetLatitude) {
        this.targetLatitude = targetLatitude;
    }

    public double getTargetLongitude() {
        return targetLongitude;
    }

    public void setTargetLongitude(double targetLongitude) {
        this.targetLongitude = targetLongitude;
    }

    @Override
    public String toString() {
        return "StartAndEnd{" +
                "currentAddressName='" + currentAddressName + '\'' +
                ", currentLatitude=" + currentLatitude +
                ", currentLongitude=" + currentLongitude +
                ", targetAddressName='" + targetAddressName + '\'' +
                ", targetLatitude=" + targetLatitude +
                ", targetLongitude=" + targetLongitude +
                '}';
    }
}
