package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.tourist.bean.Scenic;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ScenicActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.scenic_name) TextView scenicNameView;
    @BindView(R.id.scenic_is_close) TextView isCloseView;
    @BindView(R.id.scenic_hot) TextView scenicHotView;
    @BindView(R.id.scenic_map_view) CircleImageView mapBtn;
    @BindView(R.id.scenic_img) ImageView scenicImgView;
    @BindView(R.id.scenic_open_time) TextView openTimeView;
    @BindView(R.id.scenic_ticket_root) LinearLayout ticketRoot;
    @BindView(R.id.scenic_ticket_price) TextView ticketPriceView;
    @BindView(R.id.scenic_brief_info) TextView briefInfoView;


    private static final String TAG = "ScenicActivity";
    private Scenic scenic;
    private int scenicHot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_scenic);
        ButterKnife.bind(this);

        mapBtn.setOnClickListener(this::onClick);
        ticketRoot.setOnClickListener(this::onClick);
        scenicImgView.setOnClickListener(this::onClick);

        getData();
        setData();


    }

    private void setData() {
        scenicNameView.setText(scenic.getName());
        if (scenic.getIsClose() == 0){
            isCloseView.setText("当前景点是否开放：开放");
        }else {
            isCloseView.setText("当前景点是否开放：关闭");
        }
        scenicHotView.setText("景点热度：" + String.valueOf(scenicHot) + "人");
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(10));
        Glide.with(this).load(scenic.getHeadIcon()).apply(options).into(scenicImgView);
        openTimeView.setText(scenic.getOpenTime());
        if (scenic.getTicketPrice() == 0){
            ticketPriceView.setText("免费游览");
        }else {
            ticketPriceView.setText("成人票参考价：" + scenic.getTicketPrice() + "元起");
        }
        briefInfoView.setText(scenic.getDescription());

    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        scenic = (Scenic) extras.getSerializable("scenic");
        scenicHot = extras.getInt("scenic_hot");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scenic_map_view:
                Intent intent = new Intent(this, ScenicLocationActivity.class);
                double[] location = {scenic.getLatitude(), scenic.getLongitude()};
                intent.putExtra("location", location);
                startActivity(intent);
                break;
            case R.id.scenic_ticket_root:
                intent = new Intent(this, ServeTicketActivity.class);
                startActivity(intent);
                break;
            case R.id.scenic_img:
                intent = new Intent(this, ScenicAlbumActivity.class);
                intent.putExtra("scenic_id", scenic.getId());
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
