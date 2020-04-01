package com.example.huangshan.tourist.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.MyInfoWindowAdapter;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeographyHuangShanActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.geography_huangshan_back_btn) ImageView backBtn;
    @BindView(R.id.geography_huangshan_map) MapView mapView;

    private static final String TAG = "GeographyHuangShanActivity";
    //地图
    private AMap aMap;
    private UiSettings uiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geography_huangshan);
        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);

        backBtn.setOnClickListener(this::onClick);
        initMapView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.geography_huangshan_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    /**
     * 初始化地图
     */
    private void initMapView() {

        //获得地图
        aMap = mapView.getMap();
        //转移镜头到黄山风景区
        LatLng latLng = new LatLng(Constant.HUANGSHAN_LATITUDE,Constant.HUANGSHAN_LONGITITUDE);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,11,0,0)));
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        uiSettings.setMyLocationButtonEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);
    }

    /**
     * 以下重写的方法是控制地图的生命周期，因为地图占的内存相当大
     * 官方文档要求重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }
}
