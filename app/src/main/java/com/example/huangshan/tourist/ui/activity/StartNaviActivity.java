package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseNaviActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartNaviActivity extends BaseNaviActivity {

    @BindView(R.id.navi_view) AMapNaviView aMapNaviView;


    private static final String TAG = "StartNaviActivity";

    private AMapNavi aMapNavi;
    //所在地和目的地
    private double currentLatitude;
    private double currentLongitude;
    private double targetLatitude;
    private double targetLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_navi);

        ButterKnife.bind(this);

        aMapNaviView.setAMapNaviViewListener(this);
        aMapNaviView.onCreate(savedInstanceState);
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(this);

        getStartAndEndData();
    }

    private void getStartAndEndData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        double[] startAndEndData = extras.getDoubleArray("navi_start_end");
        currentLatitude = startAndEndData[0];
        currentLongitude = startAndEndData[1];
        targetLatitude = startAndEndData[2];
        targetLongitude = startAndEndData[3];
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        //aMapNavi.calculateWalkRoute(new NaviLatLng(currentLatitude,currentLongitude), new NaviLatLng(targetLatitude,targetLongitude));
        // TODO: 2020/3/17 距离太长  换天兴大道制药厂
        aMapNavi.calculateWalkRoute(new NaviLatLng(currentLatitude,currentLongitude), new NaviLatLng(30.474187, 103.479229));
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        super.onCalculateRouteSuccess(ints);
        aMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        aMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        aMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapNaviView.onDestroy();
    }
}
