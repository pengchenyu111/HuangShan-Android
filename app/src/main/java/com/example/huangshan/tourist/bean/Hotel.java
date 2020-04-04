package com.example.huangshan.tourist.bean;


import java.io.Serializable;

/**
 * 酒店
 * @author pcy
 */

public class Hotel implements Serializable {
    private long id;
    private String hotelName;
    private int hotelStar;
    private String hotelInstruction;
    private String hotelLocation;
    private String hotelCheckIn;
    private String hotelLeave;
    private String hotelPhone;
    private String breakfastType;
    private double breakfastPrice;
    private String isTakePet;
    private String isHavePark;
    private String headIcon;
    private String albumUrl;

    public Hotel() {
    }

    public Hotel(long id, String hotelName, int hotelStar, String hotelInstruction, String hotelLocation, String hotelCheckIn, String hotelLeave, String hotelPhone, String breakfastType, double breakfastPrice, String isTakePet, String isHavePark, String headIcon, String albumUrl) {
        this.id = id;
        this.hotelName = hotelName;
        this.hotelStar = hotelStar;
        this.hotelInstruction = hotelInstruction;
        this.hotelLocation = hotelLocation;
        this.hotelCheckIn = hotelCheckIn;
        this.hotelLeave = hotelLeave;
        this.hotelPhone = hotelPhone;
        this.breakfastType = breakfastType;
        this.breakfastPrice = breakfastPrice;
        this.isTakePet = isTakePet;
        this.isHavePark = isHavePark;
        this.headIcon = headIcon;
        this.albumUrl = albumUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getHotelStar() {
        return hotelStar;
    }

    public void setHotelStar(int hotelStar) {
        this.hotelStar = hotelStar;
    }

    public String getHotelInstruction() {
        return hotelInstruction;
    }

    public void setHotelInstruction(String hotelInstruction) {
        this.hotelInstruction = hotelInstruction;
    }

    public String getHotelLocation() {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation) {
        this.hotelLocation = hotelLocation;
    }

    public String getHotelCheckIn() {
        return hotelCheckIn;
    }

    public void setHotelCheckIn(String hotelCheckIn) {
        this.hotelCheckIn = hotelCheckIn;
    }

    public String getHotelLeave() {
        return hotelLeave;
    }

    public void setHotelLeave(String hotelLeave) {
        this.hotelLeave = hotelLeave;
    }

    public String getHotelPhone() {
        return hotelPhone;
    }

    public void setHotelPhone(String hotelPhone) {
        this.hotelPhone = hotelPhone;
    }

    public String getBreakfastType() {
        return breakfastType;
    }

    public void setBreakfastType(String breakfastType) {
        this.breakfastType = breakfastType;
    }

    public double getBreakfastPrice() {
        return breakfastPrice;
    }

    public void setBreakfastPrice(double breakfastPrice) {
        this.breakfastPrice = breakfastPrice;
    }

    public String getIsTakePet() {
        return isTakePet;
    }

    public void setIsTakePet(String isTakePet) {
        this.isTakePet = isTakePet;
    }

    public String getIsHavePark() {
        return isHavePark;
    }

    public void setIsHavePark(String isHavePark) {
        this.isHavePark = isHavePark;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", hotelStar=" + hotelStar +
                ", hotelInstruction='" + hotelInstruction + '\'' +
                ", hotelLocation='" + hotelLocation + '\'' +
                ", hotelCheckIn='" + hotelCheckIn + '\'' +
                ", hotelLeave='" + hotelLeave + '\'' +
                ", hotelPhone='" + hotelPhone + '\'' +
                ", breakfastType='" + breakfastType + '\'' +
                ", breakfastPrice=" + breakfastPrice +
                ", isTakePet='" + isTakePet + '\'' +
                ", isHavePark='" + isHavePark + '\'' +
                ", headIcon='" + headIcon + '\'' +
                ", albumUrl='" + albumUrl + '\'' +
                '}';
    }
}
