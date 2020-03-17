package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

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
    //所在地和目的地
    private double currentLatitude;
    private double currentLongitude;
    private double targetLatitude;
    private double targetLongitude;
    //路径规划
    private RouteSearch routeSearch;
    private List<LatLng> routePoints = new ArrayList<>();
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
        startRouteSearch(new LatLonPoint(currentLatitude,currentLongitude), new LatLonPoint(30.474187, 103.479229));
        // todo 由于距离过长先用近一点的
        //startRouteSearch(new LatLonPoint(currentLatitude,currentLongitude), new LatLonPoint(targetLatitude,targetLongitude));


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
        markerOptions2.position(AmapUtil.convertToLatLng(new LatLonPoint(30.474187, 103.479229)));
        // TODO: 2020/3/17 先用近一点的
        //markerOptions2.position(end);
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
            int costMinute = (int) path.getDuration()/60;
            int distance = (int) path.getDistance();
            timeDistanceView.setText(costMinute + "分钟(" + distance + "米)");

            // 显示路径列表
            RouteDetailAdapter adapter = new RouteDetailAdapter(this,steps);
            routeListView.setAdapter(adapter);
        }else {
            Toast.makeText(this, ErrorInfo.getError(i), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

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
