package com.example.huangshan.admin.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.HomePageMapActivity;
import com.example.huangshan.admin.activity.HomePageRuleActivity;
import com.example.huangshan.admin.activity.HomePageTicketActivity;
import com.example.huangshan.admin.activity.WeatherH5Activity;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.http.RetrofitManager;

import com.example.huangshan.utils.GlideImageLoader;
import com.example.huangshan.utils.StatusBarUtil;

import com.google.gson.Gson;
import com.heweather.plugin.view.HeContent;
import com.heweather.plugin.view.HeWeatherConfig;
import com.heweather.plugin.view.HorizonView;
import com.heweather.plugin.view.LeftLargeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;


import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * MainActivity中首页的fragment
 */
public class ShowDataFragment extends Fragment implements View.OnClickListener{

//    @BindView(R.id.showdata_boommenubtn) BoomMenuButton showdataBoomMenuBtn;

    @BindView(R.id.heweather_plugin_main_view) HorizonView weatherInfo;
    @BindView(R.id.home_page_admin_poetry) TextView poetryView;
    @BindView(R.id.home_page_admin_banner) Banner banner;
    @BindView(R.id.home_page_admin_map) ImageButton mapViewBtn;
    @BindView(R.id.home_page_admin_weather) ImageButton weatherBtn;
    @BindView(R.id.home_page_admin_ticket) ImageButton ticketBtn;
    @BindView(R.id.home_page_admin_rule) ImageButton ruleBtn;

    private static final String TAG = "ShowDataFragment";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();



    public ShowDataFragment() {
        // Required empty public constructor
    }

    /**
     * 加载一次，避免内存消耗
     * @return
     */
    public static ShowDataFragment newInstance() {
        Bundle args = new Bundle();
        ShowDataFragment fragment = new ShowDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //初始化和风天气
        HeWeatherConfig.init(Constant.HEFENGWEATHER_PLUGIN_kEY,Constant.HEFENGWEATHER_LOCATION_HUANGSHAN_GUANGMINGDING);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载HomeFragment布局
        View view = inflater.inflate(R.layout.fragment_main_showdata,container,false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        mapViewBtn.setOnClickListener(this);
        weatherBtn.setOnClickListener(this);
        ticketBtn.setOnClickListener(this);
        ruleBtn.setOnClickListener(this);
        //状态栏透明
        StatusBarUtil.transparentAndDark(getActivity());
        //显示天气
        showWeather();
        //显示诗句
        showPoetry();
        //轮播
        bannerSettings();

        return view;
    }

    /**
     * 图片轮播设置
     */
    private void bannerSettings() {
        //github地址：https://github.com/youth5201314/banner

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(Arrays.asList(Constant.ADMIN_BANNER_URL));
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        //banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用

        //添加监听器
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                switch (position){
                    case 0:
                        Toast.makeText(getActivity(),"点击了图片"+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"点击了图片"+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"点击了图片"+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),"点击了图片"+position,Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(getActivity(),"点击了图片"+position,Toast.LENGTH_SHORT).show();
                        break;
                    default:break;
                }
            }
        });
        banner.start();
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
     * 显示天气
     */
    private void showWeather() {
        weatherInfo.setDefaultBack(false);
        //设置布局的背景圆角角度（单位：dp），颜色，边框宽度（单位：px），边框颜色
        weatherInfo.setStroke(10, Color.TRANSPARENT, 1, Color.TRANSPARENT);
        weatherInfo.addWeatherIcon(60);
        weatherInfo.addTemp(16, Color.WHITE);
        weatherInfo.addCond(16, Color.WHITE);
        weatherInfo.setViewGravity(HeContent.GRAVITY_LEFT);
        weatherInfo.show();
        weatherInfo.setOnClickListener(this);
    }

//    private void drawFloatButton() {
//        assert showdataBoomMenuBtn != null;
//        showdataBoomMenuBtn.addBuilder(BuilderManager.getHamButtonBuilder("点击日期区间", "").normalImageRes(R.mipmap.calendar));
//        showdataBoomMenuBtn.setOnBoomListener(new OnBoomListenerAdapter() {
//            @Override
//            public void onClicked(int index, BoomButton boomButton) {
//                super.onClicked(index, boomButton);
//                showDatePicker();
//                changeBoomButton(index);
//            }
//        });
//    }
//
//    private void showDatePicker(){
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.datepicker, null);
//        final DatePicker startTimePicker = (DatePicker) view.findViewById(R.id.starttime);
//        final DatePicker endTimePicker = (DatePicker) view.findViewById(R.id.endtime);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //开始时间
//                int yearS = startTimePicker.getYear();
//                int realMonthS = startTimePicker.getMonth()+1;
//                int realDayS = startTimePicker.getDayOfMonth();
//                String formatMonthS = formatDate(realMonthS);
//                String formatDayS = formatDate(realDayS);
//                startTime = String.valueOf(yearS)+"-"+formatMonthS+"-"+formatDayS;
//                Log.d(TAG,"startTime：-------------"+startTime+"----------------结束");
//
//                //结束时间
//                int yearE = endTimePicker.getYear();
//                int realMonthE = endTimePicker.getMonth()+1;
//                int realDayE = endTimePicker.getDayOfMonth();
//                String formatMonthE = formatDate(realMonthE);
//                String formatDayE = formatDate(realDayE);
//                endTime = String.valueOf(yearE)+"-"+formatMonthE+"-"+formatDayE;
//                Log.d(TAG,"endTime：-------------"+endTime+"----------------结束");
//
//                //更新图表
//                //initData(startTime,endTime);
//                //drawCharts();
//            }
//        });
//        builder.setNegativeButton("取消", null);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        //自动弹出键盘问题解决
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//    }
//
//    private String formatDate(int date){
//        String formatedDate = String.valueOf(date);
//        if (date == 1 || date == 2 || date == 3 || date == 4 || date == 5 || date == 6 || date == 7 || date == 8 || date == 9) {
//            formatedDate = "0"+formatedDate;
//        }
//        return formatedDate;
//    }
//
//    private void changeBoomButton(int index) {
//        HamButton.Builder builder = (HamButton.Builder) showdataBoomMenuBtn.getBuilder(index);
//        if (index == 0) {
//            String tip = dailyNums.get(0).getDateName()+"----"+dailyNums.get(dailyNums.size()-1).getDateName();
//            builder.normalText(tip);
////            builder.highlightedText("Highlighted, changed!");
//            builder.subNormalText("点击选取日期区间");
//            builder.normalTextColor(Color.YELLOW);
//            builder.highlightedTextColorRes(R.color.colorPrimary);
//            builder.subNormalTextColor(Color.BLACK);
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.heweather_plugin_main_view:
                Intent intent = new Intent(getActivity(), WeatherH5Activity.class);
                startActivity(intent);
                break;
            case R.id.home_page_admin_map:
                Intent intent1 = new Intent(getActivity(), HomePageMapActivity.class);
                startActivity(intent1);
                break;
            case R.id.home_page_admin_weather:
                Intent intent2 = new Intent(getActivity(),WeatherH5Activity.class);
                startActivity(intent2);
                break;
            case R.id.home_page_admin_ticket:
                Intent intent3 = new Intent(getActivity(), HomePageTicketActivity.class);
                startActivity(intent3);
                break;
            case R.id.home_page_admin_rule:
                Intent intent4 = new Intent(getActivity(), HomePageRuleActivity.class);
                startActivity(intent4);
            default:
                break;
        }
    }

}