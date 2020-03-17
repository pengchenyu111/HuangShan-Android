package com.example.huangshan.admin.httpservice;

import com.example.huangshan.http.ResultObj;


import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 投诉
 */
public interface ComplaintService {

    //全查询
    @GET("complaints/all")
    Observable<ResultObj> getAll();

    //获得近期投诉，用于轮播
    @GET("complaints/recent")
    Observable<ResultObj> getRecent();

    //根据日期查询
    @GET("complaints/dates/{date}")
    Observable<ResultObj> getByDate(@Path("date") String date);

    //获取投诉分类排行榜
    @GET("complaints/ranks")
    Observable<ResultObj> getRanks();

    //处理一条反馈
    @PUT("complaints/handles/{id}")
    Observable<ResultObj> handleOne(@Path("id") long id, @Body RequestBody requestBody);

    //增加一条投诉
    @POST("complaints")
    Observable<ResultObj> addleOne(@Body RequestBody requestBody);

}
