package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.common.httpservice.VerificationCodeService;
import com.example.huangshan.common.ui.LoginActivity;
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

public class TouristRegisterActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tourist_register_back_btn) ImageView backBtn;
    @BindView(R.id.tourist_register_phone) EditText phoneEdit;
    @BindView(R.id.tourist_register_verification) EditText verificationEdit;
    @BindView(R.id.tourist_register_get_code) Button getVerificationBtn;
    @BindView(R.id.tourist_register_password_edit_1) EditText passwordEdit;
    @BindView(R.id.tourist_register_password_edit_2) EditText passwordConfirmEdit;
    @BindView(R.id.tourist_register_btn) Button registerBtn;

    private static final String TAG = "TouristRegisterActivity";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //ui
    private SweetAlertDialog pDialog;
    private CountDownTimer timer;
    private String phone;
    private String verificationCode;
    private String password;
    private String confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_register);
        ButterKnife.bind(this);

        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();

        backBtn.setOnClickListener(this);
        getVerificationBtn.setOnClickListener(this);
        registerBtn.setOnClickListener(this);

        timer = new CountDownTimer(1000*60, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getVerificationBtn.setEnabled(false);
                getVerificationBtn.setText(millisUntilFinished/1000 + "s");
            }

            @Override
            public void onFinish() {
                getVerificationBtn.setEnabled(true);
                getVerificationBtn.setText("重新发送");
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tourist_register_back_btn:
                finish();
                break;
            case R.id.tourist_register_get_code:
                handleGetVerification();
                break;
            case R.id.tourist_register_btn:
                handleRegister();
                break;
                default:
                    break;
        }
    }

    @SuppressLint("CheckResult")
    private void handleRegister() {
        phone = phoneEdit.getText().toString();
        verificationCode = verificationEdit.getText().toString();
        password = passwordEdit.getText().toString();
        confirmPassword = passwordConfirmEdit.getText().toString();
        boolean isOK = checkRegisterInput(phone, verificationCode, password, confirmPassword);
        if (isOK){
            //先显示注册中，避免过长等待
            pDialog = new SweetAlertDialog(TouristRegisterActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("注册中");
            pDialog.setCancelable(false);
            pDialog.show();
            //开始注册
            TouristService touristService = retrofit.create(TouristService.class);
            Map<String, String> map = new LinkedHashMap<>();
            map.put("phone",phone);
            map.put("password",confirmPassword);
            map.put("verificationCode",verificationCode);
            RequestBody requestBody = RequestBody.Companion.create(gson.toJson(map), MediaType.Companion.parse("application/json; charset=utf-8"));
            touristService.register(requestBody)
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<ResultObj>() {
                        @Override
                        public void accept(ResultObj resultObj) throws Exception {
                            if (resultObj.getCode() == ResultCode.OK){
                                pDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                pDialog.setTitleText("成功！")
                                        .setContentText("您已成功注册!")
                                        .showCancelButton(false)
                                        .setConfirmText("返回登录")
                                        .setCancelClickListener(null)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Intent intent = new Intent(TouristRegisterActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        }).show();
                            }else {
                                pDialog.dismissWithAnimation();
                                Toast.makeText(TouristRegisterActivity.this,resultObj.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            pDialog.dismissWithAnimation();
                            Log.d(TAG,throwable.getMessage());
                            Toast.makeText(TouristRegisterActivity.this,"服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private boolean checkRegisterInput(String phone,String verificationCode,String password,String confirmPassword) {
        if (phone.isEmpty() || verificationCode.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this,"不能输入空值！", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!phone.isEmpty() && !verificationCode.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !password.equals(confirmPassword)){
            Toast.makeText(this,"两次密码输入不一致！", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    /**
     * 处理发送验证码
     */
    @SuppressLint("CheckResult")
    private void handleGetVerification() {
        phone = phoneEdit.getText().toString();
        boolean isEmpty = checkPhoneInput(phone);
        if (!isEmpty){
            timer.start();
            VerificationCodeService verificationCodeService = retrofit.create(VerificationCodeService.class);
            verificationCodeService.getVerificationCode(phone)
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<ResultObj>() {
                        @Override
                        public void accept(ResultObj resultObj) throws Exception {
                            if (resultObj.getCode() == ResultCode.OK){
                                Toast.makeText(TouristRegisterActivity.this,"验证码获取成功，请稍等片刻！", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(TouristRegisterActivity.this,"验证码获取失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(TAG,throwable.getMessage());
                            Toast.makeText(TouristRegisterActivity.this,"服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this,"请输入正确的手机号", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 检查有没有输入电话
     * @return
     */
    private boolean checkPhoneInput(String phone) {
        if (phone.isEmpty()){
            return true;
        }
        return false;
    }
}
