package com.example.huangshan.tourist.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TouristService {

    //修改密码
    @PUT("tourists/passwords/{id}")
    Observable<ResultObj> changePassword(@Path("id") long id, @Body RequestBody body);

    //注册
    @POST("tourists/registers")
    Observable<ResultObj> register(@Body RequestBody body);

    //修改头像
    @PUT("tourists/headicons/{id}")
    Observable<ResultObj> changeHeadIcon(@Path("id") long id, @Body RequestBody body);

    //更新个人资料
    @PUT("tourists/{id}")
    Observable<ResultObj> updateInfo(@Path("id") long id, @Body RequestBody body);
}
