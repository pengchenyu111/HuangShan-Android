package com.example.huangshan.tourist.bean;

import java.io.Serializable;

/**
 * 景点
 * @author pcy
 */
public class Scenic implements Serializable {

    private long id;
    private String code;
    private String name;
    private String description;
    private String openTime;
    private double ticketPrice;
    private int isClose;
    private double longitude;
    private double latitude;
    private String headIcon;
    private String photoUrl;
    private String videoUrl;
    private String audioUrl;

    public Scenic() {
    }

    public Scenic(long id, String code, String name, String description, String openTime, double ticketPrice, int isClose, double longitude, double latitude, String headIcon, String photoUrl, String videoUrl, String audioUrl) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.openTime = openTime;
        this.ticketPrice = ticketPrice;
        this.isClose = isClose;
        this.longitude = longitude;
        this.latitude = latitude;
        this.headIcon = headIcon;
        this.photoUrl = photoUrl;
        this.videoUrl = videoUrl;
        this.audioUrl = audioUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public int getIsClose() {
        return isClose;
    }

    public void setIsClose(int isClose) {
        this.isClose = isClose;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getHeadIcon() {
        return headIcon;
    }

    public void setHeadIcon(String headIcon) {
        this.headIcon = headIcon;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        return "Scenic{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", openTime='" + openTime + '\'' +
                ", ticketPrice=" + ticketPrice +
                ", isClose=" + isClose +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", headIcon='" + headIcon + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }
}
