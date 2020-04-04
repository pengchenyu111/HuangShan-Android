package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.common.httpservice.FileService;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.ui.adapter.AlbumAdapter;
import com.example.huangshan.utils.StatusBarUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HotelAlbumActivity extends BaseActivity {

    @BindView(R.id.hotel_album_back_btn)
    ImageView backBtn;
    @BindView(R.id.hotel_album_recyclerview)
    RecyclerView recyclerView;

    private static final String TAG = "HotelAlbumActivity";
    private long hotelId;
    private AlbumAdapter albumAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_album);
        ButterKnife.bind(this);

        StatusBarUtil.transparentAndDark(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        hotelId = intent.getLongExtra("hotel_id", 1);
        getAlbum();
    }

    @SuppressLint("CheckResult")
    private void getAlbum() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(30000, TimeUnit.MILLISECONDS);
        OkHttpClient okHttpClient = builder.build();

        Retrofit mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl("http://101.37.173.73:9000/")
                .build();
        FileService fileService = mRetrofit.create(FileService.class);
        fileService.getHotelAlbum(hotelId)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> list) throws Exception {
                        albumAdapter = new AlbumAdapter(HotelAlbumActivity.this, list);
                        GridLayoutManager layoutManager = new GridLayoutManager(HotelAlbumActivity.this, 2);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(albumAdapter);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(HotelAlbumActivity.this, "服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
