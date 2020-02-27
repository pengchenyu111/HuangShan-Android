package com.example.huangshan.admin.service;

import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    // 全查询
    @GET("notifications/all")
    Observable<ResultObj> allNotifications();

    // 全查询
    @GET("notifications/recent")
    Observable<ResultObj> recentNotifications();

    //关闭一条通知
    @PUT("notifications/closes/{id}")
    Observable<ResultObj> closeOne(@Path("id") long id);

    //添加新通知
    @POST("notifications")
    Observable<ResultObj> addOne(@Body RequestBody body);
}
