package com.example.huangshan.tourist.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.huangshan.R;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.bean.Scenic;
import com.example.huangshan.tourist.bean.ScenicHot;
import com.example.huangshan.tourist.httpservice.ScenicService;
import com.example.huangshan.tourist.ui.adapter.ScenicViewAdapter;
import com.example.huangshan.utils.StatusBarUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class ScenicFragment extends Fragment implements View.OnClickListener,SearchView.OnQueryTextListener,SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.scenic_view_search) SearchView searchView;
    @BindView(R.id.scenic_view_swipe) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.scenic_view_recyclerView) RecyclerView recyclerView;

    private static final String TAG = "ScenicFragment";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    private ScenicService scenicService;
    //
    private List<Scenic> scenics = new ArrayList<>();
    private List<ScenicHot> scenicHots = new ArrayList<>();
    private ScenicViewAdapter adapter;

    public static ScenicFragment newInstance() {
        Bundle args = new Bundle();
        ScenicFragment fragment = new ScenicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_scenic, container ,false);
        ButterKnife.bind(this,view);
        StatusBarUtil.transparentAndDark(getActivity());

        searchView.setSubmitButtonEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this::onRefresh);

        initHttpService();

        showData();


        return view;
    }

    private void showData() {
        getScenicData();
    }

    private void setData() {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ScenicViewAdapter(getActivity(), scenics, scenicHots);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("CheckResult")
    private void getScenicData() {
        scenicService.getAll()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            scenics = gson.fromJson(data, new TypeToken<List<Scenic>>(){}.getType());
                            getScenicHotData();
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

    @SuppressLint("CheckResult")
    private void getScenicHotData() {
        scenicService.getAllHots()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            scenicHots = gson.fromJson(data, new TypeToken<List<ScenicHot>>(){}.getType());
                            setData();
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

    private void initHttpService() {
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        scenicService = retrofit.create(ScenicService.class);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.d(TAG, query);
        if (scenics.size() == 0){
            Toast.makeText(getActivity(), "请稍等片刻...", Toast.LENGTH_SHORT).show();
        }else {
            // TODO: 2020/4/3 动态查询
            //adapter.notifyDataSetChanged();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onRefresh() {
        showData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
