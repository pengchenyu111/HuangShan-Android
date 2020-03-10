package com.example.huangshan.admin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.common.ui.ChangePasswordActivity;
import com.example.huangshan.common.ui.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 这里是 设置 Activity
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.setting_back_btn) ImageView back;
    @BindView(R.id.admin_exit) Button adminExit;//退出登录按钮
    @BindView(R.id.setting_change_password_ll_root1) LinearLayout changePasswordRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //绑定控件
        ButterKnife.bind(this);

        //设置响应
        back.setOnClickListener(this::onClick);
        adminExit.setOnClickListener(this::onClick);
        changePasswordRoot.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_back_btn:
                finish();
                break;
            case R.id.admin_exit:
                //清空缓存
                SharedPreferences preferences= getSharedPreferences("loginUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                //跳转回LoginActivity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_change_password_ll_root1:
                Intent intent1 = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent1);
                break;
                default:
                    break;
        }
    }
}
