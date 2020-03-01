package com.example.huangshan.admin.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.huangshan.R;
import com.example.huangshan.admin.fragment.ComplaintListFragment;

import butterknife.ButterKnife;

public class HandleComplaintActivity extends AppCompatActivity {

    private static final String TAG = "HandleComplaintActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_complaint);
        //绑定控件
        ButterKnife.bind(this);
        //设置初始fragment
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        ComplaintListFragment fragment = new ComplaintListFragment();
        transaction.add(R.id.compalint_root_container,fragment);
        transaction.commit();
    }
}
