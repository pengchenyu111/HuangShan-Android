package com.example.huangshan.admin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.HandleComplaintActivity;
import com.example.huangshan.admin.bean.Complaint;
import com.example.huangshan.admin.httpservice.ComplaintService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.utils.StatusBarUtil;
import com.example.huangshan.view.AdminFeedbackBanner;
import com.gongwen.marqueen.MarqueeFactory;
import com.gongwen.marqueen.MarqueeView;
import com.gongwen.marqueen.util.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.phoenix.PullToRefreshView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * NotificationFragment中管理员处理用户反馈的地方
 */
public class HandleFeedBackFragment extends Fragment implements View.OnClickListener,PullToRefreshView.OnRefreshListener {

    @BindView(R.id.notification_feedback_refreshview) PullToRefreshView refreshView;
    @BindView(R.id.notification_feedback_scrollview) NestedScrollView scrollView;
    @BindView(R.id.notification_feedback_marqueeView) MarqueeView marqueeView;
    @BindView(R.id.notification_feedback_rank_1) TextView rankView1;
    @BindView(R.id.notification_feedback_rank_2) TextView rankView2;
    @BindView(R.id.notification_feedback_rank_3) TextView rankView3;
    @BindView(R.id.notification_feedback_rank_4) TextView rankView4;
    @BindView(R.id.notification_feedback_rank_5) TextView rankView5;
    @BindView(R.id.notification_feedback_today_count) TextView todayViewCount;
    @BindView(R.id.notification_feedback_handle_btn) Button handleBtn;


    private static final String TAG = "HandleFeedBackFragment";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();

    public static HandleFeedBackFragment newInstance() {
        Bundle args = new Bundle();
        HandleFeedBackFragment fragment = new HandleFeedBackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handle_fedback,container,false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        handleBtn.setOnClickListener(this::onClick);
        refreshView.setOnRefreshListener(this::onRefresh);
        //设置状态栏透明化
        StatusBarUtil.transparentAndDark(getActivity());
        //初始网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //加载最近投诉数据
        initComplaintData();
        //加载今日投诉
        initTodayComplaint();
        //加载排行榜
        initRank();

        return view;
    }


    /**
     * 加载轮播的投诉
     */
    @SuppressLint("CheckResult")
    private void initComplaintData() {
        ComplaintService complaintService = retrofit.create(ComplaintService.class);
        complaintService.getRecent()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<Complaint> complaints = gson.fromJson(data, new TypeToken<List<Complaint>>(){}.getType());
                            showComplaintBanner(complaints);
                        }else {
                            Toast.makeText(getContext(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getContext(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 显示banner
     * @param complaints
     */
    private void showComplaintBanner(List<Complaint> complaints) {
        MarqueeFactory<LinearLayout,Complaint> marqueeFactory = new AdminFeedbackBanner(getContext());
        //设置数据
        marqueeFactory.setData(complaints);
        //设置点击
        marqueeView.setOnItemClickListener(new OnItemClickListener<LinearLayout,Complaint>() {
            @Override
            public void onItemClickListener(LinearLayout mView, Complaint mData, int mPosition) {
                Toast.makeText(getContext(),mData.getComplaintReason(), Toast.LENGTH_LONG).show();
            }
        });
        //设置动画
        marqueeView.setInAndOutAnim(R.anim.in_top, R.anim.out_bottom);
        //设置工厂
        marqueeView.setMarqueeFactory(marqueeFactory);
        //开始滚动
        marqueeView.startFlipping();
    }


    /**
     * 加载今日投诉
     */
    @SuppressLint("CheckResult")
    private void initTodayComplaint() {
        ComplaintService complaintService = retrofit.create(ComplaintService.class);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        complaintService.getByDate(date)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<Complaint> complaints = gson.fromJson(data, new TypeToken<List<Complaint>>(){}.getType());
                            //设置数据
                            todayViewCount.setText(String.valueOf(complaints.size()));
                        }else if (resultObj.getCode() == ResultCode.QUERY_FAIL){
                            todayViewCount.setText("0");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getContext(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 加载排行榜
     */
    @SuppressLint("CheckResult")
    private void initRank() {
        ComplaintService complaintService = retrofit.create(ComplaintService.class);
        complaintService.getRanks()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<String> complaints = gson.fromJson(data, new TypeToken<List<String>>(){}.getType());
                            //设置数据
                            rankView1.setText("1."+complaints.get(0));
                            rankView2.setText("2."+complaints.get(1));
                            rankView3.setText("3."+complaints.get(2));
                            rankView4.setText("4."+complaints.get(3));
                            rankView5.setText("5."+complaints.get(4));
                        }else {
                            Toast.makeText(getContext(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getContext(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notification_feedback_handle_btn:
                Intent intent = new Intent(getActivity(), HandleComplaintActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        marqueeView.startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        marqueeView.stopFlipping();
    }

    @Override
    public void onRefresh() {
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshView.setRefreshing(false);
                //重新加载notification数据
                initComplaintData();
                initTodayComplaint();
            }
        }, 2000);
    }
}
