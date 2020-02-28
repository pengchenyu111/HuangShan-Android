package com.example.huangshan.admin.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.MyInfoWindowAdapter;
import com.example.huangshan.admin.adapter.POIAdapter;
import com.example.huangshan.admin.bean.MyPOI;
import com.example.huangshan.admin.bean.ScenicManage;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.Constant;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationUrgentActivity extends BaseActivity implements View.OnClickListener, PoiSearch.OnPoiSearchListener {

    @BindView(R.id.notifications_urgent_back_btn) ImageView backBtn;
    @BindView(R.id.notification_urgent_spinner_btn) Spinner spinnerBtn;
    @BindView(R.id.notification_urgent_map) MapView mapView;
    @BindView(R.id.notification_urgent_110_call) ImageButton call110Btn;
    @BindView(R.id.notification_urgent_120_call) ImageButton call120Btn;

    private static final String TAG = "NotificationUrgentActiv";
    //地图
    private AMap aMap;
    private UiSettings uiSettings;
    private String poiKeyword;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_urgent);
        //绑定控件
        ButterKnife.bind(this);
        //设置响应
        backBtn.setOnClickListener(this::onClick);
        call110Btn.setOnClickListener(this::onClick);
        call120Btn.setOnClickListener(this::onClick);
        //设置下拉选择框
        showSpinner();
        //地图绑定初始化地图
        mapView.onCreate(savedInstanceState);
        //初始化地图
        initMapView();

    }

    /**
     * POI检索
     * @param keyword 关键字
     * @param poiCode POI的分类检索码
     * @param cityCodeOrName 城市的编码或名字，不写默认为全国范围内
     */
    private void poiSearch(String keyword,String poiCode,String cityCodeOrName) {
        query = new PoiSearch.Query(keyword,poiCode,cityCodeOrName);
        query.setPageSize(20);
        query.setPageNum(0);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 展示下拉选择框
     */
    private void showSpinner() {
        MyPOI hospital = new MyPOI(R.mipmap.hospital_colored,"医院");
        MyPOI policeStation = new MyPOI(R.mipmap.police_station,"派出所");
        List<MyPOI> myPOIList = new ArrayList<>();
        myPOIList.add(hospital);
        myPOIList.add(policeStation);

        POIAdapter adapter = new POIAdapter(this,myPOIList);
        spinnerBtn.setAdapter(adapter);
        spinnerBtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                poiKeyword = adapter.getItem(position).getPoiText();
                //POI检索
                aMap.clear();
                poiSearch(poiKeyword,"","黄山");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                poiKeyword = "医院";
            }

        });
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
        //实例化 UiSettings 对象
        uiSettings = aMap.getUiSettings();
        //不显示缩放按钮
        uiSettings.setZoomControlsEnabled(false);
        //显示指南针
        uiSettings.setCompassEnabled(true);
        //显示比例尺
        uiSettings.setScaleControlsEnabled(true);
        //高德地图logo放于左下角
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        //显示默认的定位按钮
        uiSettings.setMyLocationButtonEnabled(true);
        //设置显示自身定位的小图标
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));//精度圈 圈框隐藏
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));//精度圈 圈范围隐藏

        aMap.setMyLocationStyle(myLocationStyle);
        //设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        aMap.setMyLocationEnabled(true);
        //设置自定义 infowindow
//        adapter = new MyInfoWindowAdapter();
//        aMap.setInfoWindowAdapter(adapter);
//        aMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    /**
     * 画自定义的 Marker
     */
    private void drawMarkers(ArrayList<PoiItem> pois,String keyword){
        int iconId = R.mipmap.default_poi;
        if (keyword.equals("医院")){
            iconId = R.mipmap.hospital_marker;
        }else if (keyword.equals("派出所")){
            iconId = R.mipmap.police_station_marker;
        }
        for (PoiItem poiItem : pois) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(),poiItem.getLatLonPoint().getLongitude());
            markerOptions.position(latLng);// Marker的位置
            markerOptions.title(poiItem.getTitle());// Marker的标题
            markerOptions.draggable(false);//设置可拖动
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),iconId)));//加上图标
            markerOptions.setFlat(true);//设置marker平贴地图效果
            Marker marker = aMap.addMarker(markerOptions);
        }
    }

    /**
     * 一下两个是地图的POI检索回调接口
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000){
            ArrayList<PoiItem> pois = poiResult.getPois();
            drawMarkers(pois,poiResult.getQuery().getQueryString());
        }else {
            Toast.makeText(this,"服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    /**
     * 点击响应接口
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notifications_urgent_back_btn:
                finish();
                break;
            case R.id.notification_urgent_110_call:
                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_DIAL);
                intent1.setData(Uri.parse("tel:110"));
                startActivity(intent1);
                break;
            case R.id.notification_urgent_120_call:
                Intent intent2 = new Intent();
                intent2.setAction(Intent.ACTION_DIAL);
                intent2.setData(Uri.parse("tel:120"));
                startActivity(intent2);
                break;
                default:
                    break;
        }

    }

    /**
     * 下面是生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
