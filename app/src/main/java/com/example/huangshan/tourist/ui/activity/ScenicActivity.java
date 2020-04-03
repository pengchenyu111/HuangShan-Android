package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.tourist.bean.Scenic;

import butterknife.ButterKnife;

public class ScenicActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "ScenicActivity";
    private Scenic scenic;
    private int scenicHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_scenic);
        ButterKnife.bind(this);

        getData();


    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        scenic = (Scenic) extras.getSerializable("scenic");
        scenicHot = extras.getInt("scenic_hot");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
