package com.example.huangshan.tourist.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ScenicHotService {

    @GET("scenic_hot")
    Observable<ResultObj> getAll();
}
