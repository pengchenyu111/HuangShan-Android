package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
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
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseNaviActivity;
import com.example.huangshan.constans.ErrorInfo;
import com.example.huangshan.tourist.bean.StartAndEnd;
import com.example.huangshan.tourist.ui.adapter.RouteDetailAdapter;
import com.example.huangshan.utils.AmapUtil;
import com.example.huangshan.view.ScrollListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteDetailAcitvity extends BaseNaviActivity implements View.OnClickListener, RouteSearch.OnRouteSearchListener, AMap.OnMarkerClickListener{

    @BindView(R.id.route_back_btn) ImageView backBtn;
    @BindView(R.id.route_start_navi_btn) TextView startNaviBtn;
    @BindView(R.id.route_map) MapView mapView;
    @BindView(R.id.route_time_distance) TextView timeDistanceView;
    @BindView(R.id.route_detail_list) ScrollListView routeListView;

    private static final String TAG = "RouteDetailAcitvity";
    //ui
    private PopupWindow mPopupWindow;
    //所在地和目的地
    private double currentLatitude;
    private double currentLongitude;
    private double targetLatitude;
    private double targetLongitude;
    private String targetAddressName;
    //路径规划
    private RouteSearch routeSearch;
    private List<LatLng> routePoints = new ArrayList<>();
    private int distance;
    private int costMinute;
    //地图
    private AMap aMap;
    private UiSettings uiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        ButterKnife.bind(this);
        mapView.onCreate(savedInstanceState);

        backBtn.setOnClickListener(this);
        startNaviBtn.setOnClickListener(this);

        getStartAndEndData();
        initMapView();
        initRouteSearch();
        //startRouteSearch(new LatLonPoint(currentLatitude,currentLongitude), new LatLonPoint(30.474187, 103.479229));
        startRouteSearch(new LatLonPoint(currentLatitude,currentLongitude), new LatLonPoint(targetLatitude,targetLongitude));


    }

    private void getStartAndEndData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        StartAndEnd startAndEnd = (StartAndEnd) extras.getSerializable("navi_start_end");
        currentLatitude = startAndEnd.getCurrentLatitude();
        currentLongitude = startAndEnd.getCurrentLongitude();
        targetLatitude = startAndEnd.getTargetLatitude();
        targetLongitude = startAndEnd.getTargetLongitude();
        targetAddressName = startAndEnd.getTargetAddressName();
        Log.d(TAG, "=============>" + startAndEnd.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.route_back_btn:
                finish();
                break;
            case R.id.route_start_navi_btn:
                Intent intent = new Intent(this, StartNaviActivity.class);
                Bundle bundle = new Bundle();
                double[] data = {currentLatitude,currentLongitude,targetLatitude,targetLongitude};
                bundle.putDoubleArray("navi_start_end",data);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.gaode_map_btn:
                if (AmapUtil.isGdMapInstalled()) {
                    AmapUtil.openGaoDeNavi(RouteDetailAcitvity.this, 0, 0, null, targetLatitude, targetLongitude, targetAddressName);
                } else {
                    //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                    Toast.makeText(RouteDetailAcitvity.this, "尚未安装高德地图", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.baidu_map_btn:
                if (AmapUtil.isBaiduMapInstalled()) {
                    AmapUtil.openBaiDuNavi(RouteDetailAcitvity.this, 0, 0, null, targetLatitude, targetLongitude, targetAddressName);
                } else {
                    //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                    Toast.makeText(RouteDetailAcitvity.this, "尚未安装百度地图", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tengxun_map_btn:
                if (AmapUtil.isTencentMapInstalled()) {
                    AmapUtil.openTencentMap(RouteDetailAcitvity.this, 0, 0, null, targetLatitude, targetLongitude, targetAddressName);
                } else {
                    //这里必须要写逻辑，不然如果手机没安装该应用，程序会闪退，这里可以实现下载安装该地图应用
                    Toast.makeText(RouteDetailAcitvity.this, "尚未安装腾讯地图", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancle_btn:
                this.finish();
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
        LatLng latLng = new LatLng(currentLatitude,currentLongitude);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,13,0,0)));
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        uiSettings.setMyLocationButtonEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.strokeColor(Color.argb(0,0,0,0));//精度圈 圈框隐藏
        myLocationStyle.radiusFillColor(Color.argb(0,0,0,0));//精度圈 圈范围隐藏
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        //添加起点终点marker
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng start = new LatLng(currentLatitude, currentLongitude);
        LatLng end = new LatLng(targetLatitude, targetLongitude);
        markerOptions.position(start);
        markerOptions.title("您的位置");
        markerOptions.draggable(false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.navi_marker_start)));
        markerOptions.setFlat(true);
        aMap.addMarker(markerOptions);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(end);
        markerOptions2.title("目的地");
        markerOptions2.draggable(false);
        markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.navi_marker_end)));
        markerOptions2.setFlat(true);
        aMap.addMarker(markerOptions2);

        aMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    /**
     * 下面是路径规划
     */
    private void initRouteSearch() {
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
    }

    private void startRouteSearch(LatLonPoint start, LatLonPoint end){
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(start,end);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo,RouteSearch.WalkDefault);
        routeSearch.calculateWalkRouteAsyn(query);
    }

    /**
     * 下面是路径规划的方法
     */
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        Log.d(TAG, "cuowm:"+i);
        if (i == 1000){
            List<WalkPath> paths = walkRouteResult.getPaths();
            WalkPath path = paths.get(0);
            List<WalkStep> steps = path.getSteps();
            for (WalkStep step : steps) {
                ArrayList<LatLng> latLngs = AmapUtil.convertArrList(step.getPolyline());
                routePoints.addAll(latLngs);
            }
            aMap.addPolyline(new PolylineOptions().addAll(routePoints).width(10).color(Color.argb(255, 1, 1, 1)));
            //显示花费时间和距离
            costMinute = (int) path.getDuration()/60;
            distance = (int) path.getDistance();
            timeDistanceView.setText(costMinute + "分钟(" + distance + "米)");

            // 显示路径列表
            RouteDetailAdapter adapter = new RouteDetailAdapter(this,steps);
            routeListView.setAdapter(adapter);
        }else {
            Toast.makeText(this, "距离过长，建议您使用其他地图应用获取更专业的体验！", Toast.LENGTH_SHORT).show();
            showPopUpWindow();

        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    /**
     * 绘制弹出框
     */
    private void showPopUpWindow() {
        // 绘制前先清除之前的popupwindow，否则内存泄漏
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
        //初始化PopUpWindow
        View popupwindowView = LayoutInflater.from(RouteDetailAcitvity.this).inflate(R.layout.popup_map_app,null);
        mPopupWindow = new PopupWindow(popupwindowView);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(getWindowManager().getDefaultDisplay().getHeight() /4);
        mPopupWindow.setFocusable(false);

        //加到父布局中
        View rootView = LayoutInflater.from(RouteDetailAcitvity.this).inflate(R.layout.activity_route_detail,null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        //获取PopUpWindow中的控件
        LinearLayout gaodeMapBtn = (LinearLayout) popupwindowView.findViewById(R.id.gaode_map_btn);
        LinearLayout baiduMapBtn = (LinearLayout)popupwindowView.findViewById(R.id.baidu_map_btn);
        LinearLayout tengxunMapBtn = (LinearLayout)popupwindowView.findViewById(R.id.tengxun_map_btn);
        LinearLayout cancleBtn = (LinearLayout)popupwindowView.findViewById(R.id.cancle_btn);

        //设置响应
        gaodeMapBtn.setOnClickListener(this::onClick);
        baiduMapBtn.setOnClickListener(this::onClick);
        tengxunMapBtn.setOnClickListener(this::onClick);
        cancleBtn.setOnClickListener(this::onClick);

    }

    /**
     * 下面是marker点击
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    /**
     * 下面是生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
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
