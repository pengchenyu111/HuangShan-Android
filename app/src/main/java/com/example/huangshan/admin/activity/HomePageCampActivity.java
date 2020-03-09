package com.example.huangshan.admin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bin.david.form.core.SmartTable;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.table.CampPrice;
import com.example.huangshan.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageCampActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.home_page_admin_camp_back_btn) ImageView backBtn;
    @BindView(R.id.home_page_admin_camp_price) SmartTable table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_camp);

        ButterKnife.bind(this);
        backBtn.setOnClickListener(this);

        showPriceTable();

    }

    private void showPriceTable() {
        List<CampPrice> campPrices = new ArrayList<>();
        campPrices.add(new CampPrice("游客自备帐篷","所有时期", "80元/顶·天 综合服务费"));
        campPrices.add(new CampPrice("企业经营帐篷","淡季", "150元/顶•天 租赁费"));
        campPrices.add(new CampPrice("企业经营帐篷","旺季", "300元/顶•天 租赁费"));
        table.setData(campPrices);
        table.getConfig().setShowXSequence(false);
        table.getConfig().setShowYSequence(false);
        //table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_admin_camp_back_btn:
                finish();
                break;
            default:
                break;
        }
    }
}
