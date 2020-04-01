package com.example.huangshan.tourist.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.utils.StatusBarUtil;

import butterknife.ButterKnife;

public class LegendActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LegendActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
