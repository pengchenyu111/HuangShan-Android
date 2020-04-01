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

public class DeliciousActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.delicious_back_btn) ImageView backBtn;

    @BindView(R.id.delicious_fish) ImageView fishDelicious;
    @BindView(R.id.delicious_double_shi) ImageView doubleShiDelicious;
    @BindView(R.id.delicious_chicken) ImageView chickenDelicious;
    @BindView(R.id.delicious_hushi) ImageView hushiDelicious;
    @BindView(R.id.delicious_shun) ImageView shunDelicious;
    @BindView(R.id.delicious_shaobing) ImageView shaobingDelicious;
    @BindView(R.id.delicious_doufu) ImageView doufuDelicious;
    @BindView(R.id.delicious_juecai) ImageView juecaiDelicious;

    private static final String TAG = "DeliciousActivity";

    public static final String[] DELICIOUS_URL = {
            "http://101.37.173.73:8000/delicious/delicious_fish.png",
            "http://101.37.173.73:8000/delicious/delicious_double_shi.png",
            "http://101.37.173.73:8000/delicious/delicious_chicken.png",
            "http://101.37.173.73:8000/delicious/delicious_hushi.png",
            "http://101.37.173.73:8000/delicious/delicious_shun.png",
            "http://101.37.173.73:8000/delicious/delicious_shaobing.png",
            "http://101.37.173.73:8000/delicious/delicious_doufu.png",
            "http://101.37.173.73:8000/delicious/delicious_juecai.png"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delicious);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);

        backBtn.setOnClickListener(this);

        loadSongImg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.delicious_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    private void loadSongImg() {
        Glide.with(this).load(DELICIOUS_URL[0]).into(fishDelicious);
        Glide.with(this).load(DELICIOUS_URL[1]).into(doubleShiDelicious);
        Glide.with(this).load(DELICIOUS_URL[2]).into(chickenDelicious);
        Glide.with(this).load(DELICIOUS_URL[3]).into(hushiDelicious);
        Glide.with(this).load(DELICIOUS_URL[4]).into(shunDelicious);
        Glide.with(this).load(DELICIOUS_URL[5]).into(shaobingDelicious);
        Glide.with(this).load(DELICIOUS_URL[6]).into(doufuDelicious);
        Glide.with(this).load(DELICIOUS_URL[7]).into(juecaiDelicious);
    }
}
