package com.example.huangshan.admin.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.OneAdminManage;
import com.example.huangshan.admin.fragment.ShowAdminInfoFragment;

import butterknife.ButterKnife;

/**
 * 这个activity用来展示一个管理员的详细信息
 */
public class  AdminInformationActivity extends BaseActivity {

    private static final String TAG = "AdminInformationActivit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_information);

        ButterKnife.bind(this);

        //拿到传过来的数据
        Intent intent = getIntent();
        OneAdminManage currentAdminManage = (OneAdminManage) intent.getSerializableExtra("currentAdminManage");

        //设置第一个fragment,并向其传送数据
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ShowAdminInfoFragment showAdminInfoFragment = new ShowAdminInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("adminInfo",currentAdminManage);
        showAdminInfoFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.admin_info_container,showAdminInfoFragment);
        fragmentTransaction.commit();

    }
}
