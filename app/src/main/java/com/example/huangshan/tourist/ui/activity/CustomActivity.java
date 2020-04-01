package com.example.huangshan.tourist.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.custom_back_btn) ImageView backBtn;
    @BindView(R.id.custom_naba) ImageView customNaba;
    @BindView(R.id.custom_lvyoujie) ImageView customLvyoujie;
    @BindView(R.id.custom_tea) ImageView customTea;
    @BindView(R.id.custom_taige) ImageView customTaige;

    private static final String TAG = "CustomActivity";

    public static final String[] CUSTOM_URL = {
            "http://101.37.173.73:8000/custom/custom_naba.png",
            "http://101.37.173.73:8000/custom/custom_lvyoujie.png",
            "http://101.37.173.73:8000/custom/custom_tea.png",
            "http://101.37.173.73:8000/custom/custom_taige.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);

        backBtn.setOnClickListener(this);

        loadSongImg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.custom_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    private void loadSongImg() {
        Glide.with(this).load(CUSTOM_URL[0]).into(customNaba);
        Glide.with(this).load(CUSTOM_URL[1]).into(customLvyoujie);
        Glide.with(this).load(CUSTOM_URL[2]).into(customTea);
        Glide.with(this).load(CUSTOM_URL[3]).into(customTaige);
    }
}
