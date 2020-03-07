package com.example.huangshan.admin.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.NotificationListAdapter;
import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.admin.httpservice.NotificationService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * 发布通知
 */
public class NotificationListFragment extends Fragment implements View.OnClickListener, PullToRefreshView.OnRefreshListener{

    @BindView(R.id.notifications_list_back_btn) ImageView backbtn;
    @BindView(R.id.notification_recyclerview) RecyclerView recyclerView;
    @BindView(R.id.notification_list_refresh) PullToRefreshView pullToRefreshView;

    private static final String TAG = "NotificationManageActiv";
    private NotificationListAdapter adapter;
    private Bundle bundle = new Bundle();
    //网络请求
    RetrofitManager retrofitManager = new RetrofitManager();
    Retrofit retrofit;
    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_list,container,false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        backbtn.setOnClickListener(this::onClick);
        pullToRefreshView.setOnRefreshListener(this::onRefresh);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //加载数据
        initData();

        return view;
    }

    /**
     * 显示 列表
     * @param notifications
     */
    @SuppressLint("WrongConstant")
    private void showList(List<Notification> notifications) {
        //开始设置RecyclerView
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //设置Adaptor
        adapter = new NotificationListAdapter(getContext(),notifications);
        recyclerView.setAdapter(adapter);
        //解决冲突
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                //dx dy 偏移的距离 dy上负下正，可用来判断滑动方向
                super.onScrolled(recyclerView, dx, dy);
                //拿到第一个孩子的顶部的位置，但这里还有个小bug，在到达最底部时，他也是等于0，所以应该还有一步判断是否到达底部
                int topRowVerticalPosition = recyclerView.getChildAt(0).getTop();
                if (topRowVerticalPosition < 0){
                    pullToRefreshView.setEnabled(false);
                }else if (topRowVerticalPosition == 0){
                    pullToRefreshView.setEnabled(true);
                }
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    /**
     * 获取通知列表数据
     */
    @SuppressLint("CheckResult")
    private void initData() {
        NotificationService notificationService = retrofit.create(NotificationService.class);
        notificationService.allNotifications()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<Notification> notifications = gson.fromJson(data,new TypeToken<List<Notification>>(){}.getType());
                            showList(notifications);
                        } else {
                            Toast.makeText(getContext(),"服务器繁忙，请稍候再试!",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notifications_list_back_btn:
                getActivity().finish();
                break;
            default:break;
        }
    }

    @Override
    public void onRefresh() {
        pullToRefreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //重新加载notification数据
                recyclerView.removeAllViews();
                initData();
                pullToRefreshView.setRefreshing(false);
            }
        }, 2000);
    }
}
