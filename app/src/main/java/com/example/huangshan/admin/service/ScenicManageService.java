package com.example.huangshan.admin.service;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ScenicManageService {

    //全查询
    @GET("scenic_manages/all")
    Observable<ResultObj> allScenicManages();
}
