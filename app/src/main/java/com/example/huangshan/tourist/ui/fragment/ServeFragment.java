package com.example.huangshan.tourist.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.NotificationUrgentActivity;
import com.example.huangshan.admin.activity.WeatherH5Activity;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.tourist.ui.activity.ServeComplaintActivity;
import com.example.huangshan.tourist.ui.activity.ServePredictActivity;
import com.example.huangshan.tourist.ui.activity.ServeTicketActivity;
import com.example.huangshan.utils.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServeFragment extends Fragment  implements View.OnClickListener{

    @BindView(R.id.serve_banner) Banner banner;
    @BindView(R.id.serve_weather) ImageButton weatherBtn;
    @BindView(R.id.serve_ticket) ImageButton ticketBtn;
    @BindView(R.id.serve_predict) ImageButton predictBtn;
    @BindView(R.id.serve_route) ImageButton routeBtn;
    @BindView(R.id.serve_hot_level) ImageButton hotLevelBtn;
    @BindView(R.id.serve_hotel) ImageButton hotelBtn;
    @BindView(R.id.serve_delicacies) ImageButton delicaciesBtn;
    @BindView(R.id.serve_complaint) ImageButton complaintBtn;
    @BindView(R.id.serve_help) ImageButton helpBtn;
    @BindView(R.id.serve_wc) ImageButton wcBtn;

    private static final String TAG = "ServeFragment";

    public static ServeFragment newInstance() {
        Bundle args = new Bundle();
        ServeFragment fragment = new ServeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_serve, container,false);
        ButterKnife.bind(this,view);

        addClick();

        bannerSettings();

        return view;
    }

    private void addClick() {
        weatherBtn.setOnClickListener(this);
        ticketBtn.setOnClickListener(this);
        predictBtn.setOnClickListener(this);
        routeBtn.setOnClickListener(this);

        hotLevelBtn.setOnClickListener(this);
        hotelBtn.setOnClickListener(this);
        delicaciesBtn.setOnClickListener(this);

        complaintBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        wcBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serve_weather:
                Intent intent = new Intent(getActivity(), WeatherH5Activity.class);
                startActivity(intent);
                break;
            case R.id.serve_ticket:
                intent = new Intent(getActivity(), ServeTicketActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_predict:
                intent = new Intent(getActivity(), ServePredictActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_route:
                Toast.makeText(getActivity(),"功能带开放，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.serve_hot_level:
                Toast.makeText(getActivity(),"功能带开放，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.serve_hotel:
                break;
            case R.id.serve_delicacies:
                Toast.makeText(getActivity(),"功能带开放，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.serve_complaint:
                intent = new Intent(getActivity(), ServeComplaintActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_help:
                intent = new Intent(getActivity(), NotificationUrgentActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_wc:
                break;
                default:
                    break;
        }
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
        banner.setImages(Arrays.asList(Constant.TOURIST_SERVE_BANNER_URL));
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
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    default:break;
                }
            }
        });
        banner.start();
    }
}
