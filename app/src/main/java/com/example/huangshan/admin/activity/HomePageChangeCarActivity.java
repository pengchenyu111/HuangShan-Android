package com.example.huangshan.admin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bin.david.form.core.SmartTable;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.table.ChangeCarPrice;
import com.example.huangshan.admin.bean.table.PortPrice;
import com.example.huangshan.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageChangeCarActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.home_page_admin_change_car_back_btn) ImageView backBtn;
    @BindView(R.id.home_page_admin_change_car_price) SmartTable changeCarPriceTable;
    @BindView(R.id.home_page_admin_change_port_price) SmartTable portPriceTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_change_car);

        ButterKnife.bind(this);
        backBtn.setOnClickListener(this);

        showChangeCarPrice();
        showPortPrice();
    }

    private void showPortPrice() {
        List<PortPrice> portPrices = new ArrayList<>();
        portPrices.add(new PortPrice("20分钟内","不限","免费"));
        portPrices.add(new PortPrice("20分钟以上至1个小时","不限","10元/车"));
        portPrices.add(new PortPrice("1个小时至6个小时","大型车（黄牌）","40元/车"));
        portPrices.add(new PortPrice("1个小时至6个小时","小型车（蓝牌、黑牌","30元/车"));
        portPrices.add(new PortPrice("6个小时以上","不限","按以上再每小时加收1元"));
        portPriceTable.setData(portPrices);
        portPriceTable.getConfig().setShowXSequence(false);
        portPriceTable.getConfig().setShowYSequence(false);
    }

    private void showChangeCarPrice() {
        List<ChangeCarPrice> changeCarPrices = new ArrayList<>();
        changeCarPrices.add(new ChangeCarPrice("寨西、东岭、南门——温泉", "往返票均为11元"));
        changeCarPrices.add(new ChangeCarPrice("温泉——云谷寺、慈光阁", "往返票均为8元"));
        changeCarPrices.add(new ChangeCarPrice("寨西、东岭、南门——慈光阁、云谷寺", "往返票均为19元"));
        changeCarPriceTable.setData(changeCarPrices);
        changeCarPriceTable.getConfig().setShowXSequence(false);
        changeCarPriceTable.getConfig().setShowYSequence(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_admin_change_car_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }
}
