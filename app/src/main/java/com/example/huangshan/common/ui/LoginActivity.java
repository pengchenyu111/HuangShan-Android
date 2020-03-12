package com.example.huangshan.common.ui;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.huangshan.admin.activity.AdminMainActivity;
import com.example.huangshan.common.base.ActivityCollector;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.tourist.ui.activity.TouristMainActivity;
import com.example.huangshan.view.CustomVideoView;
import com.example.huangshan.R;

/**
 * 登录  Activity
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    //权限获取
    private static final int M_PERMISSION_CODE = 1001;
    private String[] mPermissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    //背景视频
    private CustomVideoView videoView;

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //征询权限
        askPermissions();

        SharedPreferences preferences= getSharedPreferences("loginUser", MODE_PRIVATE);
        String role = (String) preferences.getString("role",null);
        //若是缓存中有之前用户登录的信息，则不用加载Fragment去网络请求，直接拿出缓存中的数据就行了
        //这时候就不会经过Login界面，而是直接进入Main界面
        if (role != null && role.equals("admin")){
            Intent intent = new Intent(this, AdminMainActivity.class);
            startActivity(intent);
        } else if (role != null && role.equals("tourist")){
            Intent intent = new Intent(this, TouristMainActivity.class);
            startActivity(intent);
        } else{
            //缓存中没有用户信息，那就要执行登录界面了
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //UserLoginFragment fragment = new UserLoginFragment();//添加第三方的fragment
            LoginFragment fragment = new LoginFragment();
            fragmentTransaction.add(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
            //初始化背景视频
            initVideoView();
        }
    }

    private void initVideoView(){
        videoView = (CustomVideoView)findViewById(R.id.login_video_bg);
        videoView.setVideoURI(Uri.parse("android.resource://com.example.huangshan/"+R.raw.login_background));
//        播放
        videoView.start();
//        循环播放
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoView.start();
            }
        });
    }


    //返回直接退出
    @Override
    protected void onRestart() {
        super.onRestart();
        initVideoView();
//        this.finish();
    }

    //防止锁屏或者切出的时候，音乐在播放
//    @Override
//    protected void onStop() {
//        super.onStop();
//        videoView.stopPlayback();
//    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        finishAndRemoveTask();
//    }

    /**
     * 双击退出应用
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                ActivityCollector.finishAll();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 以下函数为检查本 app所需要的运行时权限
     */
    private void askPermissions(){
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                && checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                && checkPermission(Manifest.permission.READ_PHONE_STATE)){
            return;
        }else{
            requestPermissions(mPermissions, M_PERMISSION_CODE);
        }
    }
    private boolean checkPermission(String permission) {

        if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case M_PERMISSION_CODE:
                if (grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED){
                    return;
                }else{
                    Toast.makeText(this,"请通过全部权限，否则将无法正常使用！",Toast.LENGTH_SHORT).show();
                    ActivityCollector.finishAll();
                }
        }
    }
}