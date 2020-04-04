package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.HeatmapTileProvider;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.WeightedLatLng;
import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.bean.Scenic;
import com.example.huangshan.tourist.bean.ScenicHot;
import com.example.huangshan.tourist.httpservice.ScenicHotService;
import com.example.huangshan.tourist.httpservice.ScenicService;
import com.example.huangshan.utils.StatusBarUtil;
import com.example.huangshan.view.TextCircleView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class ServeScenicHotActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.hot_map_back_btn) ImageView backBtn;
    @BindView(R.id.hot_map_max_root) LinearLayout maxRoot;
    @BindView(R.id.hot_map_scenic_name) TextView scenicMaxNameView;
    @BindView(R.id.hot_map_scenic_img) ImageView scenicMaxImgView;
    @BindView(R.id.hot_map_scenic_num) TextCircleView scenicMaxNumView;
    @BindView(R.id.hot_map_rank_1) TextView rankView1;
    @BindView(R.id.hot_map_rank_2) TextView rankView2;
    @BindView(R.id.hot_map_rank_3) TextView rankView3;
    @BindView(R.id.hot_map) MapView mapView;

    private static final String TAG = "ServeScenicHotActivity";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    private ScenicHotService scenicHotService;
    private ScenicService scenicService;
    //地图
    private AMap aMap;
    private UiSettings uiSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_scenic_hot);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);
        mapView.onCreate(savedInstanceState);

        backBtn.setOnClickListener(this);
        maxRoot.setOnClickListener(this);

        initHttpService();
        initMapView();
        getData();

    }

    private void initHttpService() {
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        scenicHotService = retrofit.create(ScenicHotService.class);
        scenicService = retrofit.create(ScenicService.class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hot_map_back_btn:
                finish();
                break;
            case R.id.hot_map_max_root:
                // TODO：要跳转到一个景点界面
                Toast.makeText(this,"TODO：要跳转到一个景点界面", Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }

    /**
     * 初始化地图
     */
    private void initMapView() {
        aMap = mapView.getMap();
        LatLng latLng = new LatLng(Constant.HUANGSHAN_LATITUDE,Constant.HUANGSHAN_LONGITITUDE);
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng,13,0,0)));
        uiSettings = aMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
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
    }

    @SuppressLint("CheckResult")
    private void getData() {
        scenicHotService.getAll()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<ScenicHot> scenicHots = gson.fromJson(data, new TypeToken<List<ScenicHot>>(){}.getType());
                            showHotMap(scenicHots);
                            getRank(scenicHots);
                        }else {
                            Toast.makeText(ServeScenicHotActivity.this, "似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.getMessage());
                        Toast.makeText(ServeScenicHotActivity.this, "开始就出错服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getRank(List<ScenicHot> scenicHots) {
        Collections.sort(scenicHots, new Comparator<ScenicHot>() {
            @Override
            public int compare(ScenicHot o1, ScenicHot o2) {
                int hotNum1 = o1.getHotNum();
                int hotNum2 = o2.getHotNum();
                if (hotNum1 > hotNum2) {
                    return -1;
                } else if (hotNum1 == hotNum2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });

        ScenicHot rank1 = scenicHots.get(0);
        ScenicHot rank2 = scenicHots.get(1);
        ScenicHot rank3 = scenicHots.get(2);

        setRankMax(rank1,rankView1);
        setRankNext(rank2,rankView2);
        setRankNext(rank3,rankView3);

    }

    @SuppressLint("CheckResult")
    private void setRankMax(ScenicHot rank, TextView textView ) {
        scenicMaxNumView.setText(String.valueOf(rank.getHotNum()));
        scenicService.getById(rank.getId())
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            Scenic scenic = gson.fromJson(data, Scenic.class);
                            scenicMaxNameView.setText(scenic.getName());
                            Glide.with(ServeScenicHotActivity.this).load(scenic.getHeadIcon()).into(scenicMaxImgView);
                            textView.append(scenic.getName());
                        }else {
                            Toast.makeText(ServeScenicHotActivity.this, "似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "MAX出错===》"+throwable.getMessage());
                        Toast.makeText(ServeScenicHotActivity.this, "服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void setRankNext(ScenicHot rank, TextView textView ) {
        scenicService.getById(rank.getId())
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            Scenic scenic = gson.fromJson(data, Scenic.class);
                            textView.append(scenic.getName());
                        }else {
                            Toast.makeText(ServeScenicHotActivity.this, "似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "NEXT出错===》"+throwable.getMessage());
                        Toast.makeText(ServeScenicHotActivity.this, "服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showHotMap(List<ScenicHot> scenicHots) {
        WeightedLatLng[] latLngs = new WeightedLatLng[scenicHots.size()];
        for (int i = 0; i < scenicHots.size(); i++) {
            ScenicHot scenicHot = scenicHots.get(i);
            latLngs[i] = new WeightedLatLng(new LatLng(scenicHot.getLatitude(),scenicHot.getLongitude()),scenicHot.getHotNum());
        }
        HeatmapTileProvider.Builder builder = new HeatmapTileProvider.Builder();
        builder.weightedData(Arrays.asList(latLngs));
        HeatmapTileProvider heatmapTileProvider= builder.build();
        TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
        tileOverlayOptions.tileProvider(heatmapTileProvider);
        aMap.addTileOverlay(tileOverlayOptions);
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
