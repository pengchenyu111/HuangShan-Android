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
import com.example.huangshan.admin.adapter.ComplaintListAdapter;
import com.example.huangshan.admin.bean.Complaint;
import com.example.huangshan.admin.httpservice.ComplaintService;
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

public class ComplaintListFragment extends Fragment implements View.OnClickListener,PullToRefreshView.OnRefreshListener{

    @BindView(R.id.complaint_list_back_btn)
    ImageView backBtn;
    @BindView(R.id.compalint_list_refreshview)
    PullToRefreshView refreshView;
    @BindView(R.id.compalint_list_recyclerview)
    RecyclerView recyclerView;

    private static final String TAG = "ComplaintListFragment";

    private ComplaintListAdapter adapter;
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();


    public static ComplaintListFragment newInstance() {
        Bundle args = new Bundle();
        ComplaintListFragment fragment = new ComplaintListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compalint_list,container,false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        backBtn.setOnClickListener(this::onClick);
        refreshView.setOnRefreshListener(this::onRefresh);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //加载数据
        initData();
        return view;
    }


    /**
     * 获取投诉列表数据
     */
    @SuppressLint("CheckResult")
    private void initData() {
        ComplaintService complaintService = retrofit.create(ComplaintService.class);
        complaintService.getAll()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<Complaint> complaints = gson.fromJson(data,new TypeToken<List<Complaint>>(){}.getType());
                            showList(complaints);
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

    /**
     * 显示 列表
     * @param complaints
     */
    @SuppressLint("WrongConstant")
    private void showList(List<Complaint> complaints) {
        //开始设置RecyclerView
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //设置Adaptor
        adapter = new ComplaintListAdapter(getContext(),complaints);
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
                    refreshView.setEnabled(false);
                }else if (topRowVerticalPosition == 0){
                    refreshView.setEnabled(true);
                }
            }
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complaint_list_back_btn:
                getActivity().finish();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onRefresh() {
        refreshView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.removeAllViews();
                //重新加载数据
                initData();
                refreshView.setRefreshing(false);
            }
        },2000);
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.removeAllViews();
        initData();
    }
}
