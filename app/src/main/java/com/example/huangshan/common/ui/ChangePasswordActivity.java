package com.example.huangshan.common.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.example.huangshan.R;
import com.example.huangshan.admin.httpservice.AdminService;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.httpservice.TouristService;
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

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.change_password_back_btn) ImageView backBtn;
    @BindView(R.id.change_password_edit_1) EditText inputEditText;
    @BindView(R.id.change_password_edit_2) EditText confirmEditText;
    @BindView(R.id.change_password_btn) Button changeBtn;

    private static final String TAG = "ChangePasswordActivity";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //缓存
    private SharedPreferences preferences;
    //ui
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        backBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        //网络初始化
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //初始化缓存
        preferences= this.getSharedPreferences("loginUser", MODE_PRIVATE);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_password_back_btn:
                finish();
                break;
            case R.id.change_password_btn:
                handleChangePassword();
                break;
                default:
                    break;
        }
    }

    private void handleChangePassword() {
        password = inputEditText.getText().toString();
        confirmPassword = confirmEditText.getText().toString();
        String checkResult = checkInput();
        if (checkResult.equals("0")){
            String role = preferences.getString("role",null);
            long id = preferences.getLong("id",0);
            if (role.equals("admin")){
                changeAdminPassword(id);
            }else {
                changeTouristPassword(id);
            }
        }else if (checkResult.equals("1")){
            Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show();
        }else if (checkResult.equals("2")){
            Toast.makeText(this,"两次密码不一致！",Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("CheckResult")
    private void changeAdminPassword(long id) {
        AdminService adminService = retrofit.create(AdminService.class);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("password",confirmPassword);
        RequestBody requestBody = RequestBody.Companion.create(gson.toJson(map), MediaType.Companion.parse("application/json; charset=utf-8"));
        adminService.changePassword(id,requestBody)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            new SweetAlertDialog(ChangePasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("成功！")
                                    .setContentText("您的密码已成功修改!")
                                    .showCancelButton(false)
                                    .setConfirmText("返回登录")
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }else {
                            Toast.makeText(ChangePasswordActivity.this,"似乎出了点小问题...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(ChangePasswordActivity.this,"服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void changeTouristPassword(long id) {
        TouristService touristService = retrofit.create(TouristService.class);
        Map<String,String> map = new LinkedHashMap<>();
        map.put("password",confirmPassword);
        RequestBody requestBody = RequestBody.Companion.create(gson.toJson(map), MediaType.Companion.parse("application/json; charset=utf-8"));
        touristService.changePassword(id,requestBody)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            new SweetAlertDialog(ChangePasswordActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("成功！")
                                    .setContentText("您的密码已成功修改!")
                                    .showCancelButton(false)
                                    .setConfirmText("返回登录")
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.clear();
                                            editor.apply();
                                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }).show();
                        }else {
                            Toast.makeText(ChangePasswordActivity.this,"似乎出了点小问题...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(ChangePasswordActivity.this,"服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String checkInput() {
        if (password.isEmpty() || confirmPassword.isEmpty()){
            return "1";
        }else if (!password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword)){
            return "2";
        }
        return "0";
    }
}
