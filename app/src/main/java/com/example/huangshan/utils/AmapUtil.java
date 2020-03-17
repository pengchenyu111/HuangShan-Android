package com.example.huangshan.utils;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.example.huangshan.R;

import java.util.ArrayList;
import java.util.List;

public class AmapUtil {

    /**
     * 把LatLonPoint对象转化为LatLon对象
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    /**
     * 把集合体的LatLonPoint转化为集合体的LatLng
     */
    public static ArrayList<LatLng> convertArrList(List<LatLonPoint> shapes) {
        ArrayList<LatLng> lineShapes = new ArrayList<LatLng>();
        for (LatLonPoint point : shapes) {
            LatLng latLngTemp = AmapUtil.convertToLatLng(point);
            lineShapes.add(latLngTemp);
        }
        return lineShapes;
    }

    public static int getWalkActionID(String actionName) {
        if (actionName == null || actionName.equals("")) {
            return R.mipmap.navi_dir13;
        }
        if ("左转".equals(actionName)) {
            return  R.mipmap.navi_dir2;
        }
        if ("右转".equals(actionName)) {
            return  R.mipmap.navi_dir1;
        }
        if ("向左前方".equals(actionName) || "靠左".equals(actionName) || actionName.contains("向左前方")) {
            return  R.mipmap.navi_dir6;
        }
        if ("向右前方".equals(actionName) || "靠右".equals(actionName) || actionName.contains("向右前方")) {
            return  R.mipmap.navi_dir5;
        }
        if ("向左后方".equals(actionName)|| actionName.contains("向左后方")) {
            return  R.mipmap.navi_dir7;
        }
        if ("向右后方".equals(actionName)|| actionName.contains("向右后方")) {
            return  R.mipmap.navi_dir8;
        }
        if ("直行".equals(actionName)) {
            return  R.mipmap.navi_dir3;
        }
        if ("通过人行横道".equals(actionName)) {
            return  R.mipmap.navi_dir9;
        }
        if ("通过过街天桥".equals(actionName)) {
            return  R.mipmap.navi_dir11;
        }
        if ("通过地下通道".equals(actionName)) {
            return  R.mipmap.navi_dir10;
        }
        return  R.mipmap.navi_dir13;
    }
}
