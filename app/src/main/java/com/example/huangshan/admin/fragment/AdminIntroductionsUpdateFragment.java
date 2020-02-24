package com.example.huangshan.admin.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.huangshan.R;
import com.example.huangshan.admin.service.AdminService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class AdminIntroductionsUpdateFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.admin_new_introduction_back_btn) ImageView backBtn;
    @BindView(R.id.admin_new_introduction) EditText newIntroductionText;
    @BindView(R.id.admin_new_introduction_save_btn) TextView saveBtn;


    //缓存
    private SharedPreferences sharedPreferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    public AdminService adminService;
    private Gson gson = new Gson();

    private static final String TAG = "AdminIntroductionsUpdat";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_introductions_update, container, false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        backBtn.setOnClickListener(this::onClick);
        saveBtn.setOnClickListener(this::onClick);
        //初始化缓存
        sharedPreferences = getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        adminService = retrofit.create(AdminService.class);

        return view;
    }

    /**
     * 检查输入
     * @param introduction
     */
    private boolean checkInput(String introduction) {
        if (introduction.isEmpty()){
            new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("失败！")
                    .setContentText("您还未输入任何值！")
                    .showCancelButton(false)
                    .setConfirmText("OK")
                    .setCancelClickListener(null)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    }).show();
            return false;
        }
        return true;
    }

    /**
     * 修改失败
     */
    private void changeFail() {
        new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE)
                .setTitleText("失败！")
                .setContentText("服务器繁忙，请稍候再试")
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

    /**
     * 修改成功
     * @param introduction
     */
    private void changeSuccess(String introduction) {
        //更新缓存
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("introduction",introduction);
        editor.apply();
        //显示提示框
        new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("成功！")
                .setContentText("您的个人简介已成功修改!")
                .showCancelButton(false)
                .setConfirmText("OK")
                .setCancelClickListener(null)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStack();
                    }
                }).show();
    }


    /**
     * 发送更新请求
     */
    @SuppressLint("CheckResult")
    private void updateIntroduction() {
        String account = sharedPreferences.getString("account",null);
        String newIntroduction = newIntroductionText.getText().toString();
        Map<String,String> map = new LinkedHashMap<>();
        map.put("introduction",newIntroduction);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(map));
        if (checkInput(newIntroduction)){
            adminService.changeIntroductions(account,requestBody)
                    .compose(RxSchedulers.<ResultObj>io_main())
                    .subscribe(new Consumer<ResultObj>() {
                        @Override
                        public void accept(ResultObj resultObj) throws Exception {
                            if (resultObj.getCode() == ResultCode.OK){
                                changeSuccess(newIntroduction);
                            }else {
                                changeFail();
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(TAG,throwable.getMessage());
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_new_introduction_back_btn:
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
                break;
            case R.id.admin_new_introduction_save_btn:
                updateIntroduction();
                break;
            default:
                break;

        }
    }
}
