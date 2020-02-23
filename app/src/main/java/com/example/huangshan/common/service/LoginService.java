package com.example.huangshan.common.service;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("login")
    Observable<ResultObj> login(@Body RequestBody body);
}
