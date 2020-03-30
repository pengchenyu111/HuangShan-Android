package com.example.huangshan.tourist.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.WeatherH5Activity;
import com.example.huangshan.admin.bean.DailyNum;
import com.example.huangshan.admin.httpservice.DailyNumService;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.ui.activity.MapAllPOIActivity;
import com.example.huangshan.tourist.ui.activity.ServeScenicHotActivity;
import com.example.huangshan.tourist.ui.activity.ServeTicketActivity;
import com.example.huangshan.utils.StatusBarUtil;
import com.example.huangshan.view.TextCircleView;
import com.google.gson.Gson;
import com.heweather.plugin.view.HeContent;
import com.heweather.plugin.view.HeWeatherConfig;
import com.heweather.plugin.view.HorizonView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class HomePageTouristFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.home_page_tourist_poetry) TextView poetryView;

    @BindView(R.id.home_page_tourist_map_all) ImageButton mapViewBtn;
    @BindView(R.id.home_page_tourist_weather) ImageButton weatherBtn;
    @BindView(R.id.home_page_tourist_ticket) ImageButton ticketBtn;
    @BindView(R.id.home_page_tourist_hot) ImageButton hotBtn;

    @BindView(R.id.heweather_plugin_tourist) HorizonView weatherInfo;
    @BindView(R.id.home_page_tourist_predict_num) TextCircleView predictNumView;
    @BindView(R.id.home_page_tourist_predict_level) TextCircleView predictLevelView;

    private static final String TAG = "HomePageTouristFragment";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    private DailyNumService dailyNumService;

    public static HomePageTouristFragment newInstance() {
        Bundle args = new Bundle();
        HomePageTouristFragment fragment = new HomePageTouristFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fargment_home_page_tourist, container, false);
        ButterKnife.bind(this,view);
        initClick();
        //状态栏透明
        StatusBarUtil.transparentAndDark(getActivity());
        //初始化网络请求
        initService();

        showPoetry();
        showWeather();
        getTodayPredict();

        return view;
    }

    private void initClick() {
        mapViewBtn.setOnClickListener(this::onClick);
        weatherBtn.setOnClickListener(this::onClick);
        ticketBtn.setOnClickListener(this::onClick);
        hotBtn.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_tourist_map_all:
                Intent intent = new Intent(getActivity(), MapAllPOIActivity.class);
                startActivity(intent);
                break;
            case R.id.home_page_tourist_weather:
                intent = new Intent(getActivity(), WeatherH5Activity.class);
                startActivity(intent);
                break;
            case R.id.home_page_tourist_ticket:
                intent = new Intent(getActivity(), ServeTicketActivity.class);
                startActivity(intent);
                break;
            case R.id.home_page_tourist_hot:
                intent = new Intent(getActivity(), ServeScenicHotActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }

    }

    private void initService() {
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        dailyNumService = retrofit.create(DailyNumService.class);
        //初始化和风天气
        HeWeatherConfig.init(Constant.HEFENGWEATHER_PLUGIN_kEY,Constant.HEFENGWEATHER_LOCATION_HUANGSHAN_GUANGMINGDING);
    }
    /**
     * 显示天气
     */
    private void showWeather() {
        weatherInfo.setDefaultBack(false);
        //设置布局的背景圆角角度（单位：dp），颜色，边框宽度（单位：px），边框颜色
        weatherInfo.setStroke(10, Color.TRANSPARENT, 1, Color.TRANSPARENT);
        weatherInfo.addWeatherIcon(80);
        weatherInfo.addTemp(16, Color.BLACK);
        weatherInfo.addCond(16, Color.BLACK);
        weatherInfo.setViewGravity(HeContent.GRAVITY_LEFT);
        weatherInfo.show();
        weatherInfo.setOnClickListener(this);
    }

    /**
     * 显示诗句
     */
    private void showPoetry() {
        int index = (int)(Math.random()*10);
        String poetry = Constant.POETRY[index];
        poetryView.setText(poetry);
    }


    /**
     * 获取今日预测
     */
    @SuppressLint("CheckResult")
    private void getTodayPredict() {
        //todo 由于没有数据  暂时写死
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //String date = format.format(new Date());
        dailyNumService.getOneDayNum("2019-9-8")
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            DailyNum dailyNum = gson.fromJson(data,DailyNum.class);
                            setPredictData(dailyNum);
                        }else {
                            Toast.makeText(getActivity(),"似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getActivity(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置今日预测的数据
     * @param dailyNum
     */
    private void setPredictData(DailyNum dailyNum) {
        String[] levels = {"通畅","一般","拥挤"};
        int num = dailyNum.getPredictNum();
        predictNumView.setText(String.valueOf(num));
        if (num <= 8000){
            predictLevelView.setText(levels[0]);
            predictLevelView.setTextColor(ContextCompat.getColor(getActivity(),R.color.free_count));
            predictNumView.setTextColor(ContextCompat.getColor(getActivity(),R.color.free_count));
        }else if (num > 8000 && num <= 15000){
            predictLevelView.setText(levels[1]);
            predictLevelView.setTextColor(ContextCompat.getColor(getActivity(),R.color.tips));
            predictNumView.setTextColor(ContextCompat.getColor(getActivity(),R.color.tips));
        }else {
            predictLevelView.setText(levels[2]);
            predictLevelView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
            predictNumView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
        }
    }
}
