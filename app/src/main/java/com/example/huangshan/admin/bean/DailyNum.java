package com.example.huangshan.admin.bean;


import java.io.Serializable;

public class DailyNum implements Serializable {

    private String dateName;
    private String dateWeek;
    private int predictNum;
    private int todayEightNum;
    private int todayNineNum;
    private int todayTotalNum;
    private double deviationRate;
    private int orderNum;
    private String weatherName;
    private String moduleName;
    private String description;

    public DailyNum() {
    }

    public DailyNum(String dateName, String dateWeek, int predictNum, int todayEightNum, int todayNineNum, int todayTotalNum, double deviationRate, int orderNum, String weatherName, String moduleName, String description) {
        this.dateName = dateName;
        this.dateWeek = dateWeek;
        this.predictNum = predictNum;
        this.todayEightNum = todayEightNum;
        this.todayNineNum = todayNineNum;
        this.todayTotalNum = todayTotalNum;
        this.deviationRate = deviationRate;
        this.orderNum = orderNum;
        this.weatherName = weatherName;
        this.moduleName = moduleName;
        this.description = description;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

    public String getDateWeek() {
        return dateWeek;
    }

    public void setDateWeek(String dateWeek) {
        this.dateWeek = dateWeek;
    }

    public int getPredictNum() {
        return predictNum;
    }

    public void setPredictNum(int predictNum) {
        this.predictNum = predictNum;
    }

    public int getTodayEightNum() {
        return todayEightNum;
    }

    public void setTodayEightNum(int todayEightNum) {
        this.todayEightNum = todayEightNum;
    }

    public int getTodayNineNum() {
        return todayNineNum;
    }

    public void setTodayNineNum(int todayNineNum) {
        this.todayNineNum = todayNineNum;
    }

    public int getTodayTotalNum() {
        return todayTotalNum;
    }

    public void setTodayTotalNum(int todayTotalNum) {
        this.todayTotalNum = todayTotalNum;
    }

    public double getDeviationRate() {
        return deviationRate;
    }

    public void setDeviationRate(double deviationRate) {
        this.deviationRate = deviationRate;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getWeatherName() {
        return weatherName;
    }

    public void setWeatherName(String weatherName) {
        this.weatherName = weatherName;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "DailyNum{" +
                "dateName='" + dateName + '\'' +
                ", dateWeek='" + dateWeek + '\'' +
                ", predictNum=" + predictNum +
                ", todayEightNum=" + todayEightNum +
                ", todayNineNum=" + todayNineNum +
                ", todayTotalNum=" + todayTotalNum +
                ", deviationRate=" + deviationRate +
                ", orderNum=" + orderNum +
                ", weatherName='" + weatherName + '\'' +
                ", moduleName='" + moduleName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
