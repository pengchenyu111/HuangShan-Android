package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.bean.Hotel;
import com.example.huangshan.tourist.httpservice.HotelService;
import com.example.huangshan.tourist.ui.adapter.HotelAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class ServeHotelActivity extends BaseActivity {

    @BindView(R.id.serve_hotel_back_btn) ImageView backbtn;
    @BindView(R.id.serve_hotel_recyclerview) RecyclerView recyclerView;

    private static final String TAG = "ServeHotelActivity";

    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();

    private HotelAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_hotel);
        ButterKnife.bind(this);

        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        getHotelData();

    }

    @SuppressLint("CheckResult")
    private void getHotelData() {
        HotelService hotelService = retrofit.create(HotelService.class);
        hotelService.getAllHotels()
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            List<Hotel> hotels = gson.fromJson(data, new TypeToken<List<Hotel>>(){}.getType());

                            showData(hotels);
                        }else {
                            Toast.makeText(ServeHotelActivity.this, "似乎出了一点问题...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(ServeHotelActivity.this, "服务器繁忙，请稍候再试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @SuppressLint("WrongConstant")
    private void showData(List<Hotel> hotels) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ServeHotelActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HotelAdapter(ServeHotelActivity.this, hotels);
        recyclerView.setAdapter(adapter);
    }
}
