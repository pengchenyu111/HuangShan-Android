package com.example.huangshan.admin.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.TicketDiscount;
import com.example.huangshan.common.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageTicketActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.home_page_admin_ticket_back_btn) ImageView backBtn;
    @BindView(R.id.home_page_admin_ticket_discount) SmartTable table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_ticket);

        ButterKnife.bind(this);
        backBtn.setOnClickListener(this);
        //显示优惠政策表
        showDiscountTable();
    }

    /**
     * 优惠政策表
     */
    private void showDiscountTable() {
        List<TicketDiscount> ticketDiscounts = new ArrayList<>();
        ticketDiscounts.add(new TicketDiscount("残疾人",this.getString(R.string.ticket_disabled_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("导游",this.getString(R.string.ticket_tourist_guide_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("儿童",this.getString(R.string.ticket_children1_explanation) ,"半价"));
        ticketDiscounts.add(new TicketDiscount("儿童",this.getString(R.string.ticket_children2_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("记者",this.getString(R.string.ticket_reporter_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("军人",this.getString(R.string.ticket_soldier_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("老人",this.getString(R.string.ticket_older1_explanation) ,"半价"));
        ticketDiscounts.add(new TicketDiscount("老人",this.getString(R.string.ticket_older2_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("旅行社总经理",this.getString(R.string.ticket_travel_agency_manager_explanation) ,"免费"));
        ticketDiscounts.add(new TicketDiscount("学生",this.getString(R.string.ticket_student_explanation) ,"半价"));

        table.setData(ticketDiscounts);
        //设置是否显示顶部序号列，默认显示ABCD...
        table.getConfig().setShowXSequence(false);
        //设置是否显示左侧序号列，就是行号
        table.getConfig().setShowYSequence(false);
        //table.getConfig().setContentStyle(new FontStyle(50, Color.BLUE));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_admin_ticket_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }
}
