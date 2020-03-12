package com.example.huangshan.common.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.suke.widget.SwitchButton;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class SystemSettingsActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.system_settings_back_btn)
    ImageView backBtn;
    @BindView(R.id.system_settings_notification_close_btn)
    SwitchButton notificationCloseBtn;
    @BindView(R.id.system_settings_clean_cache_root)
    LinearLayout cleanCacheRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_settings);
        ButterKnife.bind(this);

        backBtn.setOnClickListener(this);
        cleanCacheRoot.setOnClickListener(this);

        initNotificationCloseBtn();
    }

    private void initNotificationCloseBtn() {
        notificationCloseBtn.setChecked(true);
        notificationCloseBtn.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked){
                Toast.makeText(this,"开启推送",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"关闭推送",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.system_settings_back_btn:
                finish();
                break;
            case R.id.system_settings_clean_cache_root:
                cleanCache();
                break;
                default:
                    break;
        }
    }

    private void cleanCache() {
        SweetAlertDialog dialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("清除中");
        dialog.setCancelable(false);
        dialog.show();
        new CountDownTimer(1000*2,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog  .setTitleText("清除成功！")
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
        }.start();
    }
}
