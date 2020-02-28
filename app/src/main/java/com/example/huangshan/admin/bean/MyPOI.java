package com.example.huangshan.admin.bean;

import java.io.Serializable;

public class MyPOI implements Serializable {
    private int poiImg;
    private String poiText;

    public MyPOI() {
    }

    public MyPOI(int poiImg, String poiText) {
        this.poiImg = poiImg;
        this.poiText = poiText;
    }

    public int getPoiImg() {
        return poiImg;
    }

    public void setPoiImg(int poiImg) {
        this.poiImg = poiImg;
    }

    public String getPoiText() {
        return poiText;
    }

    public void setPoiText(String poiText) {
        this.poiText = poiText;
    }

    @Override
    public String toString() {
        return "MyPOI{" +
                "poiImg=" + poiImg +
                ", poiText='" + poiText + '\'' +
                '}';
    }
}
