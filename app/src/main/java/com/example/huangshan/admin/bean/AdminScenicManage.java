package com.example.huangshan.admin.bean;

import java.io.Serializable;

/**
 * 简易的管理员管理景点
 * @author pcy
 */

public class AdminScenicManage implements Serializable {

    private long id;
    private String scenicName;
    private String adminWorkDay;
    private String adminWorkTime;

    public AdminScenicManage() {
    }

    public AdminScenicManage(long id, String scenicName, String adminWorkDay, String adminWorkTime) {
        this.id = id;
        this.scenicName = scenicName;
        this.adminWorkDay = adminWorkDay;
        this.adminWorkTime = adminWorkTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getScenicName() {
        return scenicName;
    }

    public void setScenicName(String scenicName) {
        this.scenicName = scenicName;
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

    @Override
    public String toString() {
        return "AdminScenicManage{" +
                "id=" + id +
                ", scenicName='" + scenicName + '\'' +
                ", adminWorkDay='" + adminWorkDay + '\'' +
                ", adminWorkTime='" + adminWorkTime + '\'' +
                '}';
    }
}
