package com.example.huangshan.admin.bean;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ScenicManage implements Serializable {
    private int id;
    private int adminId;
    private String adminHeadIcon;
    private String adminName;
    private String phone;
    private String sex;
    private int age;
    private int workYear;
    private String introduction;
    private String adminWorkDay;
    private String adminWorkTime;
    private int scenicId;
    private String scenicName;
    private String scenicHeadIcon;
    private int isClose;
    private double longitude;
    private double latitude;

    public ScenicManage() {
    }

    public ScenicManage(int id, int adminId, String adminHeadIcon, String adminName, String phone, String sex, int age, int workYear, String introduction, String adminWorkDay, String adminWorkTime, int scenicId, String scenicName, String scenicHeadIcon, int isClose, double longitude, double latitude) {
        this.id = id;
        this.adminId = adminId;
        this.adminHeadIcon = adminHeadIcon;
        this.adminName = adminName;
        this.phone = phone;
        this.sex = sex;
        this.age = age;
        this.workYear = workYear;
        this.introduction = introduction;
        this.adminWorkDay = adminWorkDay;
        this.adminWorkTime = adminWorkTime;
        this.scenicId = scenicId;
        this.scenicName = scenicName;
        this.scenicHeadIcon = scenicHeadIcon;
        this.isClose = isClose;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminHeadIcon() {
        return adminHeadIcon;
    }

    public void setAdminHeadIcon(String adminHeadIcon) {
        this.adminHeadIcon = adminHeadIcon;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWorkYear() {
        return workYear;
    }

    public void setWorkYear(int workYear) {
        this.workYear = workYear;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getAdminWorkDay() {
        return adminWorkDay;
    }

    public void setAdminWorkDay(String adminWorkDay) {
        this.adminWorkDay = adminWorkDay;
    }

    public String getAdminWorkTime() {
        return adminWorkTime;
    }

    public void setAdminWorkTime(String adminWorkTime) {
        this.adminWorkTime = adminWorkTime;
    }

    public int getScenicId() {
        return scenicId;
    }

    public void setScenicId(int scenicId) {
        this.scenicId = scenicId;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
    }

    public String getScenicHeadIcon() {
        return scenicHeadIcon;
    }

    public void setScenicHeadIcon(String scenicHeadIcon) {
        this.scenicHeadIcon = scenicHeadIcon;
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

    @Override
    public String toString() {
        return "ScenicManage{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", adminHeadIcon='" + adminHeadIcon + '\'' +
                ", adminName='" + adminName + '\'' +
                ", phone='" + phone + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", workYear=" + workYear +
                ", introduction='" + introduction + '\'' +
                ", adminWorkDay='" + adminWorkDay + '\'' +
                ", adminWorkTime='" + adminWorkTime + '\'' +
                ", scenicId=" + scenicId +
                ", scenicName='" + scenicName + '\'' +
                ", scenicHeadIcon='" + scenicHeadIcon + '\'' +
                ", isClose=" + isClose +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
