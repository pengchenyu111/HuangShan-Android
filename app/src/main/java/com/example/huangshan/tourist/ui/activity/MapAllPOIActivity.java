package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
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
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.POIAdapter;
import com.example.huangshan.admin.bean.MyPOI;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.tourist.bean.StartAndEnd;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapAllPOIActivity extends BaseActivity implements View.OnClickListener, PoiSearch.OnPoiSearchListener,AMap.OnMarkerClickListener, AMapLocationListener {

    @BindView(R.id.map_all_poi_back_btn) ImageView backBtn;
    @BindView(R.id.map_all_poi_name) TextView POINameView;
    @BindView(R.id.map_all_poi_spinner_btn) Spinner spinnerBtn;
    @BindView(R.id.map_all_poi_more_btn) TextView getMorePOIBtn;
    @BindView(R.id.map_all_poi_map) MapView mapView;

    private static final String TAG = "MapAllPOIActivity";
    //ui
    private PopupWindow mPopupWindow;
    //地图
    private AMap aMap;
    private UiSettings uiSettings;
    private String poiKeyword;
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private int poiPageNum = 0;
    private LatLng latLng;
    private StartAndEnd address;
    //定位
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private double currentLatitude;
    private double currentLongitude;
    private String currentAddressName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_all_poi);
        //绑定控件
        ButterKnife.bind(this);
        //设置响应
        backBtn.setOnClickListener(this::onClick);
        getMorePOIBtn.setOnClickListener(this::onClick);
        initLocation();
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
        query.setPageSize(500);
        query.setPageNum(poiPageNum);
        poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(Constant.HUANGSHAN_LATITUDE,Constant.HUANGSHAN_LONGITITUDE), 30000, false));
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    /**
     * 展示下拉选择框
     */
    private void showSpinner() {
        MyPOI scenic = new MyPOI(R.mipmap.scenic_colored,"景点");
        MyPOI sellTicket = new MyPOI(R.mipmap.ticket_colored,"售票处");
        MyPOI hotel = new MyPOI(R.mipmap.hotel_colored,"酒店宾馆");
        MyPOI parkPort = new MyPOI(R.mipmap.park_colored,"停车场");
        MyPOI exit = new MyPOI(R.mipmap.exit_colored,"出入口");
        MyPOI hospital = new MyPOI(R.mipmap.hospital_colored,"医院");
        MyPOI policeStation = new MyPOI(R.mipmap.police_station,"派出所");
        List<MyPOI> myPOIList = new ArrayList<>();
        myPOIList.add(scenic);
        myPOIList.add(hospital);
        myPOIList.add(policeStation);
        myPOIList.add(sellTicket);
        myPOIList.add(parkPort);
        myPOIList.add(hotel);
        myPOIList.add(exit);

        POIAdapter adapter = new POIAdapter(this,myPOIList);
        spinnerBtn.setAdapter(adapter);
        spinnerBtn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                poiKeyword = adapter.getItem(position).getPoiText();
                //POI检索
                aMap.clear();
                poiPageNum = 0;
                POINameView.setText(poiKeyword);
                poiSearch(poiKeyword,"","黄山区");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                poiKeyword = "景点";
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
        aMap.setOnMarkerClickListener(this::onMarkerClick);
    }

    /**
     * 画自定义的 Marker
     */
    private void drawMarkers(ArrayList<PoiItem> pois, String keyword){
        int iconId = R.mipmap.default_poi;
        if (keyword.equals("医院")){
            iconId = R.mipmap.hospital_marker;
        }else if (keyword.equals("派出所")){
            iconId = R.mipmap.police_station_marker;
        }else if (keyword.equals("景点")){
            iconId = R.mipmap.scenic_marker;
        }else if (keyword.equals("售票处")){
            iconId = R.mipmap.ticket_marker;
        }else if (keyword.equals("停车场")){
            iconId = R.mipmap.park_marker;
        }else if (keyword.equals("酒店宾馆")){
            iconId = R.mipmap.hotel_marker;
        }else if (keyword.equals("出入口")){
            iconId = R.mipmap.exit_marker;
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
            case R.id.map_all_poi_back_btn:
                if (mPopupWindow != null){
                    mPopupWindow.dismiss();
                }
                finish();
                break;
            case R.id.map_all_poi_more_btn:
                aMap.clear();
                poiPageNum++;
                poiSearch(poiKeyword,"","黄山区");
                break;
            case R.id.poi_navi_close_btn:
                mPopupWindow.dismiss();
                break;
            case R.id.poi_navi_info:
                break;
            case R.id.poi_navi_go:
                Intent intent = new Intent(this, RouteDetailAcitvity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("navi_start_end", address);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        latLng = marker.getPosition();
        address = new StartAndEnd(currentAddressName,currentLatitude,currentLongitude,marker.getTitle(),marker.getPosition().latitude,marker.getPosition().longitude);
        showPopUpWindow(marker.getTitle());
        return false;
    }

    /**
     * 绘制弹出框
     */
    private void showPopUpWindow( String title) {
        // 绘制前先清除之前的popupwindow，否则内存泄漏
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
        //初始化PopUpWindow
        View popupwindowView = LayoutInflater.from(MapAllPOIActivity.this).inflate(R.layout.popup_poi_navi,null);
        mPopupWindow = new PopupWindow(popupwindowView);
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(getWindowManager().getDefaultDisplay().getHeight() /4);
        mPopupWindow.setFocusable(false);

        //加到父布局中
        View rootView = LayoutInflater.from(MapAllPOIActivity.this).inflate(R.layout.activity_map_all_poi,null);
        mPopupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);

        //获取PopUpWindow中的控件
        //ImageView adminHeadIcon = (ImageView) popupwindowView.findViewById(R.id.admin_head_icon);//头像
        ImageView closePop = (ImageView) popupwindowView.findViewById(R.id.poi_navi_close_btn);
        TextView poiName = (TextView)popupwindowView.findViewById(R.id.poi_navi_name);
        LinearLayout poiInfo = (LinearLayout)popupwindowView.findViewById(R.id.poi_navi_info);//详情按钮
        LinearLayout naviGo = (LinearLayout)popupwindowView.findViewById(R.id.poi_navi_go);//go按钮

        //设置响应
        closePop.setOnClickListener(this::onClick);
        poiInfo.setOnClickListener(this::onClick);
        naviGo.setOnClickListener(this::onClick);

        //显示在界面上
        //Glide.with(this).load(scenicManage.getAdminHeadIcon()).into(adminHeadIcon);
        poiName.setText(title);
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this::onLocationChanged);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setInterval(2000);
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null){
            if (aMapLocation.getErrorCode() == 0){
                currentLatitude = aMapLocation.getLatitude();
                currentLongitude = aMapLocation.getLongitude();
                currentAddressName = aMapLocation.getAddress();
            }else {
                Log.d(TAG,"location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
            }
        }
    }

    /**
     * 下面是生命周期
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
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
