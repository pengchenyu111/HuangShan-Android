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

public class LegendActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.legend_back_btn) ImageView backBtn;
    @BindView(R.id.legend_penglai) ImageView penglaiLegend;
    @BindView(R.id.legend_houziguanhai) ImageView houziguanhaiLegend;
    @BindView(R.id.legend_mengbishenghua) ImageView mengbishenghuaLegend;

    private static final String TAG = "LegendActivity";

    public static final String[] LEGEND_URL = {
            "http://101.37.173.73:8000/legend/legend_penglai.png",
            "http://101.37.173.73:8000/legend/legend_houziguanhai.png",
            "http://101.37.173.73:8000/legend/legend_mengbishenghua.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legend);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);

        backBtn.setOnClickListener(this);

        loadSongImg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.legend_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }


    private void loadSongImg() {
        Glide.with(this).load(LEGEND_URL[0]).into(penglaiLegend);
        Glide.with(this).load(LEGEND_URL[1]).into(houziguanhaiLegend);
        Glide.with(this).load(LEGEND_URL[2]).into(mengbishenghuaLegend);
    }
}
