package com.example.huangshan.admin.bean;

import java.io.Serializable;

/**
 * 用户投诉
 * @author pcy
 */

public class Complaint implements Serializable {

    private long id;
    private String complaintTime;//投诉时间
    private String complaintObject;//投诉对象
    private String spot;//事发地
    private String complaintType;//投诉类型，写死：景区、住宿、餐饮、旅行社、导游
    private String complaintReason;//投诉原因
    private String evidencePhoto;//照片凭据
    private String complainantName;//投诉人
    private String complainantPhone;//投诉人联系电话
    private String isHandle;//是否处理
    private String handleAdminName;//处理的管理员名
    private String handleTime;//处理时间
    private String handleMessage;//反馈消息

    public Complaint() {
    }

    public Complaint(long id, String complaintTime, String complaintObject, String spot, String complaintType, String complaintReason, String evidencePhoto, String complainantName, String complainantPhone, String isHandle, String handleAdminName, String handleTime, String handleMessage) {
        this.id = id;
        this.complaintTime = complaintTime;
        this.complaintObject = complaintObject;
        this.spot = spot;
        this.complaintType = complaintType;
        this.complaintReason = complaintReason;
        this.evidencePhoto = evidencePhoto;
        this.complainantName = complainantName;
        this.complainantPhone = complainantPhone;
        this.isHandle = isHandle;
        this.handleAdminName = handleAdminName;
        this.handleTime = handleTime;
        this.handleMessage = handleMessage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(String complaintTime) {
        this.complaintTime = complaintTime;
    }

    public String getComplaintObject() {
        return complaintObject;
    }

    public void setComplaintObject(String complaintObject) {
        this.complaintObject = complaintObject;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getComplaintReason() {
        return complaintReason;
    }

    public void setComplaintReason(String complaintReason) {
        this.complaintReason = complaintReason;
    }

    public String getEvidencePhoto() {
        return evidencePhoto;
    }

    public void setEvidencePhoto(String evidencePhoto) {
        this.evidencePhoto = evidencePhoto;
    }

    public String getComplainantName() {
        return complainantName;
    }

    public void setComplainantName(String complainantName) {
        this.complainantName = complainantName;
    }

    public String getComplainantPhone() {
        return complainantPhone;
    }

    public void setComplainantPhone(String complainantPhone) {
        this.complainantPhone = complainantPhone;
    }

    public String getIsHandle() {
        return isHandle;
    }

    public void setIsHandle(String isHandle) {
        this.isHandle = isHandle;
    }

    public String getHandleAdminName() {
        return handleAdminName;
    }

    public void setHandleAdminName(String handleAdminName) {
        this.handleAdminName = handleAdminName;
    }

    public String getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleMessage() {
        return handleMessage;
    }

    public void setHandleMessage(String handleMessage) {
        this.handleMessage = handleMessage;
    }


    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", complaintTime='" + complaintTime + '\'' +
                ", complaintObject='" + complaintObject + '\'' +
                ", spot='" + spot + '\'' +
                ", complaintType='" + complaintType + '\'' +
                ", complaintReason='" + complaintReason + '\'' +
                ", evidencePhoto='" + evidencePhoto + '\'' +
                ", complainantName='" + complainantName + '\'' +
                ", complainantPhone='" + complainantPhone + '\'' +
                ", isHandle='" + isHandle + '\'' +
                ", handleAdminName='" + handleAdminName + '\'' +
                ", handleTime='" + handleTime + '\'' +
                ", handleMessage='" + handleMessage + '\'' +
                '}';
    }
}
