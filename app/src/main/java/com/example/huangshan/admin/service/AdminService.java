package com.example.huangshan.admin.service;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AdminService {

    //查询所有管理员
    @GET("admins/all")
    Observable<ResultObj> allAdmins();
}
