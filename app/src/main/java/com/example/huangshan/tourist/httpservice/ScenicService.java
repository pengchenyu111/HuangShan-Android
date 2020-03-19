package com.example.huangshan.tourist.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScenicService {

    @GET("scenics/{id}")
    Observable<ResultObj> getById(@Path("id") long id);
}
