package com.example.huangshan.admin.activity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.huangshan.admin.fragment.NotificationListFragment;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.NotificationListAdapter;
import com.example.huangshan.admin.bean.Admin;
import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.utils.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通知管理 NotificationManageActivity  主要显示通知列表
 */
public class NotificationManageActivity extends BaseActivity {

    private static final String TAG = "NotificationManageActiv";


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_manage);
        //绑定控件
        ButterKnife.bind(this);
        //设置初始fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NotificationListFragment fragment = new NotificationListFragment();
        transaction.add(R.id.notification_history_container,fragment);
        transaction.commit();
    }
}
