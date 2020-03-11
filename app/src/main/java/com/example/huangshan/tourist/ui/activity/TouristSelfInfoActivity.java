package com.example.huangshan.tourist.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.tourist.ui.fragment.TouristSelfInfoFragment;

import butterknife.ButterKnife;

public class TouristSelfInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_self_info);

        ButterKnife.bind(this);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        TouristSelfInfoFragment fragment = new TouristSelfInfoFragment();
        transaction.add(R.id.tourist_self_info_container, fragment);
        transaction.commit();
    }

}
