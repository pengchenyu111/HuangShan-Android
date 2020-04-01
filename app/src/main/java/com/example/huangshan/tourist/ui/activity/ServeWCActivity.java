package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DistanceResult;
import com.amap.api.services.route.DistanceSearch;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.common.ui.LoginActivity;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.tourist.bean.StartAndEnd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServeWCActivity extends BaseActivity implements View.OnClickListener, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener,AMapLocationListener, DistanceSearch.OnDistanceSearchListener {

    @BindView(R.id.serve_wc_back_btn) ImageView backBtn;
    @BindView(R.id.serve_wc_map) MapView mapView;
    @BindView(R.id.serve_wc_spot) TextView spotView;
    @BindView(R.id.serve_wc_distance) TextView distanceView;
    @BindView(R.id.serve_wc_go) Button goBtn;

    private static final String TAG = "ServeWCActivity";
    //定位
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private double currentLatitude;
    private double currentLongitude;
    private String currentAddressName;
    private StartAndEnd startAndEnd;
    //地图
    private AMap aMap;
    private UiSettings uiSettings;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private double targetWCLatitude;
    private double targetWCLongitude;
    private String targetAdressName;
    //距离
    private DistanceSearch distanceSearch = null;
    private DistanceSearch.DistanceQuery distanceQuery = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_wc);

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);

        backBtn.setOnClickListener(this);
        goBtn.setOnClickListener(this::onClick);

        initLocation();
        //initMapView();
        initDistance();

        //poiSearch("厕所","","");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serve_wc_back_btn:
                finish();
                break;
            case R.id.serve_wc_go:
                Intent intent = new Intent(this, RouteDetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("navi_start_end", startAndEnd);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this::onLocationChanged);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //单次定位
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        //连续定位
        //mLocationOption.setInterval(2000);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setMockEnable(true);
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(true);
        if (null != mLocationClient){
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    /**
     * 初始化地图
     */
    private void initMapView() {

        //获得地图
        aMap = mapView.getMap();
        //转移镜头到黄山风景区
        LatLng latLng = new LatLng(currentLatitude,currentLongitude);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,13,0,0)));
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
        aMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    private void initDistance() {
        distanceSearch = new DistanceSearch(this);
        distanceSearch.setDistanceSearchListener(this::onDistanceSearched);
    }

    /**
     * POI检索
     * @param keyword 关键字
     * @param poiCode POI的分类检索码
     * @param cityCodeOrName 城市的编码或名字，不写默认为全国范围内
     */
    private void poiSearch(String keyword,String poiCode,String cityCodeOrName) {
        query = new PoiSearch.Query(keyword,poiCode,cityCodeOrName);
        query.setPageSize(50);
        query.setPageNum(0);
        poiSearch = new PoiSearch(this, query);
        Log.d(TAG, "======>"+currentLatitude+currentLongitude);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(currentLatitude, currentLongitude),15000,true));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 画自定义的 Marker
     */
    private void drawMarkers(ArrayList<PoiItem> pois){
        for (PoiItem poiItem : pois) {
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng latLng = new LatLng(poiItem.getLatLonPoint().getLatitude(),poiItem.getLatLonPoint().getLongitude());
            markerOptions.position(latLng);// Marker的位置
            markerOptions.title(poiItem.getTitle());// Marker的标题
            markerOptions.draggable(false);//设置可拖动
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.wc_marker)));//加上图标
            markerOptions.setFlat(true);//设置marker平贴地图效果
            Marker marker = aMap.addMarker(markerOptions);
        }
        //设置初始的厕所
        spotView.setText(pois.get(0).getTitle());
        LatLonPoint end = pois.get(0).getLatLonPoint();
        getDistance(new LatLonPoint(currentLatitude,currentLongitude),end);
        targetWCLatitude = pois.get(0).getLatLonPoint().getLatitude();
        targetWCLongitude = pois.get(0).getLatLonPoint().getLongitude();
        targetAdressName = pois.get(0).getTitle();
        startAndEnd = new StartAndEnd(currentAddressName,currentLatitude,currentLongitude,targetAdressName,targetWCLatitude,targetWCLongitude);
    }

    /**
     * 下面是marker点击
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        targetWCLatitude = marker.getPosition().latitude;
        targetWCLongitude = marker.getPosition().longitude;
        targetAdressName = marker.getTitle();
        spotView.setText(marker.getTitle());

        LatLonPoint start = new LatLonPoint(currentLatitude, currentLongitude);
        LatLonPoint end = new LatLonPoint(targetWCLatitude,targetWCLongitude);
        startAndEnd = new StartAndEnd(currentAddressName, currentLatitude, currentLongitude,targetAdressName, targetWCLatitude, targetWCLongitude);
        //获取距离
        getDistance(start,end);
        return false;
    }

    private void getDistance(LatLonPoint start, LatLonPoint end) {
        List<LatLonPoint> latLonPoints = new ArrayList<>();
        latLonPoints.add(start);
        distanceQuery = new DistanceSearch.DistanceQuery();
        distanceQuery.setOrigins(latLonPoints);
        distanceQuery.setDestination(end);
        distanceQuery.setType(DistanceSearch.TYPE_DISTANCE);
        distanceSearch.calculateRouteDistanceAsyn(distanceQuery);
    }


    /**
     * 一下两个是地图的POI检索回调接口
     */
    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000){
            ArrayList<PoiItem> pois = poiResult.getPois();
            drawMarkers(pois);
        }else {
            Toast.makeText(this,"服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }



    /**
     * 下面是定位
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null){
            if (aMapLocation.getErrorCode() == 0){
                currentLatitude = aMapLocation.getLatitude();
                currentLongitude = aMapLocation.getLongitude();
                currentAddressName = aMapLocation.getAddress();
                initMapView();
                poiSearch("厕所","","");
            }else {
                Log.d(TAG,"location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 距离测量
     * @param distanceResult
     * @param i
     */
    @Override
    public void onDistanceSearched(DistanceResult distanceResult, int i) {
        if (i == 1000) {
            int contents = distanceResult.describeContents();
            distanceView.setText("距离：" + distanceResult.getDistanceResults().get(0).getDistance() + " 米");
        }
    }

    /**
     * 下面是生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mLocationClient.stopLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        mLocationClient.startLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        mLocationClient.startLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
