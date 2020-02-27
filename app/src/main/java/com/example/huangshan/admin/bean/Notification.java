package com.example.huangshan.admin.bean;

import java.io.Serializable;

public class Notification  implements Serializable {

    private long id;
    private String sendTime;
    private String sendAdminName;
    private String title;
    private String content;
    private String type;//type==0 紧急事件；1 客流预警；2==>天气预警；3==>优惠通知
    private String isClose;

    public Notification() {
    }

    public Notification(long id, String sendTime, String sendAdminName, String title, String content, String type, String isClose) {
        this.id = id;
        this.sendTime = sendTime;
        this.sendAdminName = sendAdminName;
        this.title = title;
        this.content = content;
        this.type = type;
        this.isClose = isClose;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendAdminName() {
        return sendAdminName;
    }

    public void setSendAdminName(String sendAdminName) {
        this.sendAdminName = sendAdminName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsClose() {
        return isClose;
    }

    public void setIsClose(String isClose) {
        this.isClose = isClose;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", sendTime='" + sendTime + '\'' +
                ", sendAdminName='" + sendAdminName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", isClose='" + isClose + '\'' +
                '}';
    }
}
