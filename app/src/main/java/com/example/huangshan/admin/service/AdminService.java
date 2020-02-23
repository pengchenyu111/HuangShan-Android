package com.example.huangshan.admin.service;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AdminService {

    //查询所有管理员
    @GET("admins/all")
    Observable<ResultObj> allAdmins();

    //更换头像
    @PUT("admins/head_icons/{account}")
    Observable<ResultObj> changeHeadIcon(@Path("account") String account, @Body RequestBody body);
}
