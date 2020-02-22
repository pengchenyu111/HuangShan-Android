package com.example.huangshan.admin.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Admin;
import com.example.huangshan.admin.fragment.AdminSelfInfoFragment;
import com.example.huangshan.common.BaseActivity;

import butterknife.ButterKnife;

/**
 * 管理员个人信息
 */
public class AdminSelfInfoActivity extends BaseActivity {

    private static final String TAG = "AdminSelfInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_self_info);

        ButterKnife.bind(this);

        //加入第一个fragment: 展示个人信息的fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AdminSelfInfoFragment fragment = new AdminSelfInfoFragment();
        fragmentTransaction.add(R.id.admin_self_info_container,fragment);
        fragmentTransaction.commit();
    }
}
