package com.example.huangshan.tourist.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DownloadActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.download_back_btn)
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        ButterKnife.bind(this);

        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.download_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }
}
