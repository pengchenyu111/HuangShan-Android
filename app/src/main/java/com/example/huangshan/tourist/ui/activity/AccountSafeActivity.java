package com.example.huangshan.tourist.ui.activity;

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

public class AccountSafeActivity extends BaseActivity implements View.OnClickListener{

    @BindView( R.id.account_safe_back_btn)
    ImageView backBtn;
    @BindView(R.id.account_safe_change_password_ll_root1)
    LinearLayout changePasswordRoot;
    @BindView(R.id.account_safe_tourist_exit)
    Button exitBtn;

    private static final String TAG = "AccountSafeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);

        ButterKnife.bind(this);

        backBtn.setOnClickListener(this);
        changePasswordRoot.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.account_safe_back_btn:
                finish();
                break;
            case R.id.account_safe_change_password_ll_root1:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.account_safe_tourist_exit:
                //清空缓存
                SharedPreferences preferences= getSharedPreferences("loginUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                //跳转回LoginActivity
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
