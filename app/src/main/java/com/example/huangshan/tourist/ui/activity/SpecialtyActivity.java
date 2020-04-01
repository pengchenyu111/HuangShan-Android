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

public class SpecialtyActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.specialty_back_btn) ImageView backBtn;
    @BindView(R.id.specialty_tea) ImageView specialtyTea;
    @BindView(R.id.specialty_juecai) ImageView specialtyJuecai;
    @BindView(R.id.specialty_shier) ImageView specialtyShier;
    @BindView(R.id.specialty_gongju) ImageView specialtyGongju;
    @BindView(R.id.specialty_xiangfei) ImageView specialtyXiangfei;

    private static final String TAG = "SpecialtyActivity";

    public static final String[] SPECIALTY_URL = {
            "http://101.37.173.73:8000/specialty/specialty_tea.png",
            "http://101.37.173.73:8000/specialty/specialty_juecai.png",
            "http://101.37.173.73:8000/specialty/specialty_shier.png",
            "http://101.37.173.73:8000/specialty/specialty_gongju.png",
            "http://101.37.173.73:8000/specialty/specialty_xiangfei.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);

        backBtn.setOnClickListener(this::onClick);

        loadSongImg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.specialty_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    private void loadSongImg() {
        Glide.with(this).load(SPECIALTY_URL[0]).into(specialtyTea);
        Glide.with(this).load(SPECIALTY_URL[1]).into(specialtyJuecai);
        Glide.with(this).load(SPECIALTY_URL[2]).into(specialtyShier);
        Glide.with(this).load(SPECIALTY_URL[3]).into(specialtyGongju);
        Glide.with(this).load(SPECIALTY_URL[4]).into(specialtyXiangfei);
    }
}
