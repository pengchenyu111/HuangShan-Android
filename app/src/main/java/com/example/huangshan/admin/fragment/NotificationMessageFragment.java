package com.example.huangshan.admin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.NotificationManageActivity;
import com.example.huangshan.admin.activity.NotificationUrgentActivity;
import com.example.huangshan.admin.activity.SendNotificationActivity;
import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.view.AdminNotificationBanner;
import com.example.huangshan.admin.httpservice.NotificationService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.utils.StatusBarUtil;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * NotificationFragment中管理员看内部通知的地方
 */
public class NotificationMessageFragment extends Fragment implements View.OnClickListener,PullToRefreshView.OnRefreshListener{



    @BindView(R.id.notification_refresh_view) PullToRefreshView refreshView;
    @BindView(R.id.notification_admin_banner_marqueeView) MarqueeView notificationBannerView;
    @BindView(R.id.notification_message_history_btn) ImageButton historyNotificationBtn;
    @BindView(R.id.notification_message_urgent_btn) ImageButton urgentBtn;
    @BindView(R.id.notification_message_sign_btn) ImageButton signBtn;
    @BindView(R.id.notification_message_equipment_repair_btn) ImageButton equipmentRepairBtn;
    @BindView(R.id.notification_message_add_btn) Button sendNotificationBtn;

    private static final String TAG = "NotificationRootFragment";
    private View view;
    private Bundle bundle = new Bundle();

    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    public Gson gson = new Gson();

    public NotificationMessageFragment() {
        // Required empty public constructor
    }

    public static NotificationMessageFragment newInstance() {
        Bundle args = new Bundle();
        NotificationMessageFragment fragment = new NotificationMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        view = inflater.inflate(R.layout.fragment_notification_message,container,false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        refreshView.setOnRefreshListener(this::onRefresh);
        historyNotificationBtn.setOnClickListener(this::onClick);
        urgentBtn.setOnClickListener(this::onClick);
        signBtn.setOnClickListener(this::onClick);
        equipmentRepairBtn.setOnClickListener(this::onClick);
        sendNotificationBtn.setOnClickListener(this::onClick);
        //设置状态栏透明化
        StatusBarUtil.transparentAndDark(getActivity());
        //初始网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //加载通知数据
        initNotificationData();


        return view;
    }

    /**
     * 加载轮播的通知
     */
    @SuppressLint("CheckResult")
    private void initNotificationData() {
        NotificationService notificationService = retrofit.create(NotificationService.class);
        notificationService.recentNotifications()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<Notification> notifications = gson.fromJson(data, new TypeToken<List<Notification>>(){}.getType());
                            showNotificationBanner(notifications);
                        }else {
                            Toast.makeText(getContext(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                    }
                });
    }

    /**
     * 显示banner
     * @param notifications
     */
    private void showNotificationBanner(List<Notification> notifications) {
        MarqueeFactory<LinearLayout,Notification> marqueeFactory = new AdminNotificationBanner(getContext());
        //设置数据
        marqueeFactory.setData(notifications);
        //设置点击
        notificationBannerView.setOnItemClickListener(new OnItemClickListener<LinearLayout,Notification>() {
            @Override
            public void onItemClickListener(LinearLayout mView, Notification mData, int mPosition) {
                Toast.makeText(getContext(),mData.getContent(), Toast.LENGTH_LONG).show();
            }
        });
        //设置动画
        notificationBannerView.setInAndOutAnim(R.anim.in_top, R.anim.out_bottom);
        //设置工厂
        notificationBannerView.setMarqueeFactory(marqueeFactory);
        //开始滚动
        notificationBannerView.startFlipping();
    }


    /**
     * 成功提示框
     */
    private void showSuccessTips(String content) {
        new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("成功！")
                .setContentText(content)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setCancelClickListener(null)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notification_message_history_btn:
                Intent intent = new Intent(getActivity(), NotificationManageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.notification_message_urgent_btn:
                Intent urgent = new Intent(getActivity(), NotificationUrgentActivity.class);
                startActivity(urgent);
                break;
            case R.id.notification_message_sign_btn:
                showSuccessTips("今日您已签到成功!");
                break;
            case R.id.notification_message_equipment_repair_btn:
                Toast.makeText(getContext(),"功能待开放，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.notification_message_add_btn:
                Intent intent2 = new Intent(getActivity(), SendNotificationActivity.class);
                startActivityForResult(intent2,0);
                break;
            default:break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0){
            switch (resultCode){
                case 0:
                    initNotificationData();
                    break;
                    default:
                        break;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        notificationBannerView.startFlipping();
        //banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        notificationBannerView.stopFlipping();
        //banner.stopAutoPlay();
    }

    @Override
    public void onRefresh() {
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshView.setRefreshing(false);
                //重新加载notification数据
                initNotificationData();
            }
        }, 2000);
    }
}
