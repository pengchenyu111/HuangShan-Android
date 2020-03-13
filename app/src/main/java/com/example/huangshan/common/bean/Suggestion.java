package com.example.huangshan.common.bean;

import java.io.Serializable;

public class Suggestion implements Serializable {

    private long id;
    private String suggestion;
    private String propounder;
    private String feedbackTime;
    private String contactWay;

    public Suggestion() {
    }

    public Suggestion(long id, String suggestion, String propounder, String feedbackTime, String contactWay) {
        this.id = id;
        this.suggestion = suggestion;
        this.propounder = propounder;
        this.feedbackTime = feedbackTime;
        this.contactWay = contactWay;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getPropounder() {
        return propounder;
    }

    public void setPropounder(String propounder) {
        this.propounder = propounder;
    }

    public String getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(String feedbackTime) {
        this.feedbackTime = feedbackTime;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    @Override
    public String toString() {
        return "Suggestion{" +
                "id=" + id +
                ", suggestion='" + suggestion + '\'' +
                ", propounder='" + propounder + '\'' +
                ", feedbackTime='" + feedbackTime + '\'' +
                ", contactWay='" + contactWay + '\'' +
                '}';
    }
}
