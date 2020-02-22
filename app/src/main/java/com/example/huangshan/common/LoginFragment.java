package com.example.huangshan.common;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.huangshan.admin.activity.AdminMainActivity;
import com.example.huangshan.admin.bean.Tourist;
import com.example.huangshan.admin.bean.Admin;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.R;
import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;


/**
 * LoginActivity中admin用于登录的fragment
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.login_account) EditText accountText;
    @BindView(R.id.login_pwd) EditText passwordText;
    @BindView(R.id.login_btn) Button loginBtn;
    @BindView(R.id.login_back) ImageView loginBack;
    @BindView(R.id.login_pwd_visible) ImageView pwdVisible;

    private static final String TAG = "LoginFragment";
    private View view;
    public RetrofitManager retrofitManager = new RetrofitManager();
    public Gson gson = new Gson();

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        //绑定控件
        ButterKnife.bind(this,view);
        //初始化网络请求
        retrofitManager.init();
        //设置响应
        loginBtn.setOnClickListener(this::onClick);
        pwdVisible.setOnClickListener(this::onClick);
        loginBack.setOnClickListener(this::onClick);

        return view;
    }

    /**
     * 登录操作开始
     */
    private void loginBtnClick() {
        try {
            //获取输入的账号和密码
            final String account = accountText.getText().toString();
            final String password = passwordText.getText().toString();

            if (account.isEmpty() || password.isEmpty()){
                Toast.makeText(getActivity(),"账号或密码不能为空！",Toast.LENGTH_SHORT).show();
            }else {
                //往服务器发送登录请求
                loginRequest(account, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 往服务器发送登陆请求
     * @param account 账号
     * @param password 密码
     */
    @SuppressLint("CheckResult")
    private void loginRequest(String account, String password) {
        //拿到Retrofit
        Retrofit retrofit = retrofitManager.getRetrofit();
        LoginService testService = retrofit.create(LoginService.class);
        //构造请求参数
        Map<String,String> map = new LinkedHashMap<>();
        map.put("account",account);
        map.put("password",password);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(map));
        //发送请求并接收结果
        testService.login(requestBody)
                .compose(RxSchedulers.<ResultObj>io_main())
                .subscribe(new Consumer<ResultObj>() {
                               @Override
                               public void accept(ResultObj resultObj) throws Exception {
                                   if (resultObj.getCode() == ResultCode.LOGIN_DATA_WRONG){
                                       //账号或密码错误
                                       Toast.makeText(getActivity(),resultObj.getMessage(),Toast.LENGTH_SHORT).show();
                                   }else {
                                       Gson gson = new Gson();
                                       String data = gson.toJson(resultObj.getData());
                                       if (checkLoginRole(resultObj)){
                                           Admin admin = gson.fromJson(data,Admin.class);
                                           //写入缓存
                                           adminSharePreferences(admin);
                                           //登录
                                           Intent intent = new Intent(getActivity(), AdminMainActivity.class);
                                           startActivity(intent);
                                           Toast.makeText(getActivity(),"管理员您好，欢迎登录！",Toast.LENGTH_SHORT).show();
                                       }else {
                                           Tourist tourist = gson.fromJson(data,Tourist.class);
                                           //写入缓存
                                           touristSharePreferences(tourist);
                                           //登录
                                           Toast.makeText(getActivity(),"游客您好，欢迎登录！",Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Log.d(TAG,throwable.getMessage());
                               }
                           }
                );
    }

    /**
     * 检查当前登录人是管理员还是游客
     * @param resultObj
     * @return
     */
    private boolean checkLoginRole(ResultObj resultObj) {
        boolean isAdminLogin = false;
        if (resultObj.getCode() == ResultCode.ADMIN_LOGIN){
            isAdminLogin = true;
        }
        return  isAdminLogin;
    }

    /**
     * 将管理员信息写入缓存
     * @param admin
     */
    private void adminSharePreferences(Admin admin) {
        SharedPreferences preferences= getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("role", "admin");
        editor.putInt("id",admin.getId());
        editor.putString("account",admin.getAccount());
        editor.putString("name",admin.getName());
        editor.putString("sex",admin.getSex());
        editor.putInt("age",admin.getAge());
        editor.putString("phone",admin.getPhone());
        editor.putString("headIcon",admin.getHeadIcon());
        editor.putString("birth",admin.getBirth());
        editor.putInt("workYear",admin.getWorkYear());
        editor.putString("introduction",admin.getIntroduction());
        editor.putString("roleName",admin.getRoleName());
        editor.apply();
    }

    /**
     * 将游客的个人信息写入缓存
     * @param tourist
     */
    private void touristSharePreferences(Tourist tourist) {
        SharedPreferences preferences= getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("role", "tourist");
        editor.putInt("id",tourist.getId());
        editor.putString("account",tourist.getAccount());
        editor.putString("name",tourist.getName());
        editor.putString("sex",tourist.getSex());
        editor.putInt("age",tourist.getAge());
        editor.putString("phone",tourist.getPhone());
        editor.putString("headIcon",tourist.getHeadIcon());
        editor.putString("birth",tourist.getBirth());
        editor.apply();
    }

    /**
     * 改变密码显隐
     */
    private void changPwdVisivle() {
        //InputType.TYPE_TEXT_VARIATION_PASSWORD
        //TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        if (passwordText.getInputType() == 128){
            //当前为显示密文
            pwdVisible.setImageResource(R.mipmap.pwd_visible);
            passwordText.setInputType(129);
        } else if (passwordText.getInputType() == 129){
            //当前为显示明文
            pwdVisible.setImageResource(R.mipmap.pwd_invisible);
            passwordText.setInputType(128);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                loginBtnClick();
                break;
            case R.id.login_pwd_visible:
                changPwdVisivle();
                break;
            case R.id.login_back:
                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //fragmentManager.popBackStack();
                ActivityCollector.finishAll();
                break;
                default:break;
        }
    }

}