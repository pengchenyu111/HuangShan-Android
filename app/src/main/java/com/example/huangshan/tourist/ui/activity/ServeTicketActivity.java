package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.HomePageCablewayActivity;
import com.example.huangshan.admin.activity.HomePageTicketActivity;
import com.example.huangshan.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServeTicketActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.serve_ticket_back_btn) ImageView backBtn;
    @BindView(R.id.serve_ticket_huangshan_root) LinearLayout huangshanTicketRoot;
    @BindView(R.id.serve_ticket_huangshan_price) TextView huangshanTicketPriceView;
    @BindView(R.id.serve_ticket_cableway_1) LinearLayout cableway1TicketRoot;
    @BindView(R.id.serve_ticket_cableway_1_price) TextView cableway1TicketPriceView;
    @BindView(R.id.serve_ticket_cableway_2) LinearLayout cableway2TicketRoot;
    @BindView(R.id.serve_ticket_cableway_2_price) TextView cableway2TicketPriceView;
    @BindView(R.id.serve_ticket_cableway_3) LinearLayout cableway3TicketRoot;
    @BindView(R.id.serve_ticket_cableway_3_price) TextView cableway3TicketPriceView;
    @BindView(R.id.serve_ticket_cableway_4) LinearLayout cableway4TicketRoot;
    @BindView(R.id.serve_ticket_cableway_4_price) TextView cableway4TicketPriceView;

    private static final String TAG = "ServeTicketActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_ticket);

        ButterKnife.bind(this);

        addClick();

        showTicketPrice();
    }

    private void addClick() {
        backBtn.setOnClickListener(this);
        huangshanTicketRoot.setOnClickListener(this);
        cableway1TicketRoot.setOnClickListener(this);
        cableway2TicketRoot.setOnClickListener(this);
        cableway3TicketRoot.setOnClickListener(this);
        cableway4TicketRoot.setOnClickListener(this);
    }

    private void showTicketPrice() {
        huangshanTicketPriceView.append(150 + " 元起" );
        cableway1TicketPriceView.append(65 + " 元起");
        cableway2TicketPriceView.append(65 + " 元起");
        cableway3TicketPriceView.append(70 + " 元起");
        cableway4TicketPriceView.append(70 + " 元起");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serve_ticket_back_btn:
                finish();
                break;
            case R.id.serve_ticket_huangshan_root:
                Intent intent = new Intent(this, HomePageTicketActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_ticket_cableway_1:
                intent = new Intent(this, HomePageCablewayActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_ticket_cableway_2:
                intent = new Intent(this, HomePageCablewayActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_ticket_cableway_3:
                intent = new Intent(this, HomePageCablewayActivity.class);
                startActivity(intent);
                break;
            case R.id.serve_ticket_cableway_4:
                intent = new Intent(this, HomePageCablewayActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
