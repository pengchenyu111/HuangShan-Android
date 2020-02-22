package com.example.huangshan.admin.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.OnesScenicManageAdapter;
import com.example.huangshan.admin.bean.Admin;
import com.example.huangshan.admin.bean.AdminScenicManage;
import com.example.huangshan.admin.service.ScenicManageService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 */
public class AdminSelfInfoFragment extends Fragment implements View.OnClickListener{


    @BindView(R.id.admin_self_info_headicon) ImageView headIconView;
    @BindView(R.id.admin_self_info_acount) TextView accountView;
    @BindView(R.id.admin_self_info_name) TextView nameView;
    @BindView(R.id.admin_self_info_sex) TextView sexView;
    @BindView(R.id.admin_self_info_age) TextView ageView;
    @BindView(R.id.admin_self_info_workyear) TextView workYearView;
    @BindView(R.id.admin_self_info_role) TextView roleNameView;
    @BindView(R.id.admin_self_info_phone) TextView phoneView;
    @BindView(R.id.admin_self_info_introduction) TextView introductionView;
    @BindView(R.id.admin_self_info_null_spot) TextView nullSpot;

    //返回按钮
    @BindView(R.id.admin_slef_info_back_btn) ImageView backBtn;

    //工作地点
    @BindView(R.id.admin_manage_spots_recycler_view) RecyclerView recyclerView;

    private static final String TAG = "AdminSelfInfoFragment";
    private View view;
    private SharedPreferences preferences;
    private RetrofitManager retrofitManager = new RetrofitManager();
    private OnesScenicManageAdapter adapter;

    public AdminSelfInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_self_info, container, false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        backBtn.setOnClickListener(this);
        //初始化缓存
        preferences= getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        //初始化网络请求
        retrofitManager.init();
        //设置基础数据
        setAdminBaseData();
        //获取工作地点
        getManageSpots();

        return view;
    }

    /**
     * 获取工作地点
     */
    @SuppressLint("CheckResult")
    private void getManageSpots() {
        int adminId = (int)preferences.getInt("id",1);
        Retrofit retrofit = retrofitManager.getRetrofit();
        ScenicManageService scenicManageService = retrofit.create(ScenicManageService.class);
        scenicManageService.onesManages(adminId)
                .compose(RxSchedulers.<ResultObj>io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.QUERY_FAIL){
                            nullSpot.setText("暂未设置");
                        }else if (resultObj.getCode() == ResultCode.OK){
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            Gson gson = new Gson();
                            String data = gson.toJson(resultObj.getData());
                            List<AdminScenicManage> adminScenicManages = gson.fromJson(data, new TypeToken<List<AdminScenicManage>>(){}.getType());
                            adapter = new OnesScenicManageAdapter(getContext(),adminScenicManages);
                            recyclerView.setAdapter(adapter);
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
     * 设置管理员的基础管理数据
     */
    private void setAdminBaseData() {
        //从缓存中拿到数据
        String headIcon = (String) preferences.getString("headIcon",null);
        String account = (String)preferences.getString("account",null);
        String name = (String) preferences.getString("name",null);
        String sex = (String)preferences.getString("sex",null);
        int age = (int) preferences.getInt("age",0);
        int workYear = (int)preferences.getInt("workYear",0);
        String roleName = (String) preferences.getString("roleName",null);
        String phone = (String)preferences.getString("phone",null);
        String introduction = (String) preferences.getString("introduction",null);

        //设置数据
        Glide.with(getActivity()).load(headIcon).into(headIconView);
        accountView.setText(account);
        nameView.setText(name);
        sexView.setText(sex);
        ageView.setText(String.valueOf(age));
        workYearView.setText(workYear + "年");
        roleNameView.setText(roleName);
        phoneView.setText(phone);
        introductionView.setText(introduction);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_slef_info_back_btn:
                getActivity().finish();
                break;
            default:break;
        }
    }


}
