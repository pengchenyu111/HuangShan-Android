package com.example.huangshan.admin.service;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScenicManageService {

    //全查询
    @GET("scenic_manages/all")
    Observable<ResultObj> allScenicManages();

    //查询某个管理员管理的景点
    @GET("scenic_manages/ones/{adminId}")
    Observable<ResultObj> onesManages(@Path("adminId") long adminId);
}
