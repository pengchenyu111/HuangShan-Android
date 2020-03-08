package com.example.huangshan.admin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageRuleActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.home_page_admin_rule_back_btn)
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_rule);
        ButterKnife.bind(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_admin_rule_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }
}
