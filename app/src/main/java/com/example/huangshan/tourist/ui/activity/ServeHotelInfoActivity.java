package com.example.huangshan.tourist.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.tourist.bean.Hotel;
import com.example.huangshan.utils.StatusBarUtil;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ServeHotelInfoActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.hotel_info_head) ImageView headImgView;
    @BindView(R.id.hotel_info_name) TextView hotelNameView;
    @BindView(R.id.hotel_info_location) TextView hotelLocationView;
    @BindView(R.id.hotel_info_star) MaterialRatingBar hotelStar;
    @BindView(R.id.hotel_info_checkin_time) TextView checkInTimeView;
    @BindView(R.id.hotel_info_leave_time) TextView leaveTimeView;
    @BindView(R.id.hotel_info_breakfast_type) TextView breakfastTypeView;
    @BindView(R.id.hotel_info_breakfast_price) TextView breakfastPriceView;
    @BindView(R.id.hotel_info_pet_take) TextView isTakePetView;
    @BindView(R.id.hotel_info_car_park) TextView isCarParkView;
    @BindView(R.id.hotel_info_phone) TextView phoneView;
    @BindView(R.id.hotel_info_brief) TextView briefView;

    private static final String TAG = "ServeHotelInfoActivity";
    private Hotel hotel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_hotel_info);
        ButterKnife.bind(this);
        StatusBarUtil.transparentAndDark(this);

        headImgView.setOnClickListener(this::onClick);

        getData();
        showHotelInfo();

    }

    private void showHotelInfo() {
        Glide.with(this).load(hotel.getHeadIcon()).into(headImgView);
        hotelNameView.setText(hotel.getHotelName());
        hotelLocationView.setText(hotel.getHotelLocation());
        hotelStar.setNumStars(hotel.getHotelStar());
        hotelStar.setRating(hotel.getHotelStar());
        checkInTimeView.append(hotel.getHotelCheckIn() + "后");
        leaveTimeView.append(hotel.getHotelLeave() + "前");
        breakfastTypeView.append(hotel.getBreakfastType());
        breakfastPriceView.append(hotel.getBreakfastPrice() + "元起");
        if (hotel.getIsTakePet().equals("0")){
            isTakePetView.setText("不能携带宠物");
        }else {
            isTakePetView.setText("能携带宠物");
        }
        if (hotel.getIsHavePark().equals("0")){
            isCarParkView.setText("酒店没有停车场");
        }else {
            isCarParkView.setText("酒店有停车场");
        }
        phoneView.setText(hotel.getHotelPhone());
        briefView.setText(hotel.getHotelInstruction());
    }

    private void getData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        hotel = (Hotel) extras.getSerializable("hotel");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hotel_info_head:
                Intent intent = new Intent(this, HotelAlbumActivity.class);
                intent.putExtra("hotel_id", hotel.getId());
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
