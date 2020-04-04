package com.example.huangshan.tourist.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface HotelService {

    @GET("hotels")
    Observable<ResultObj> getAllHotels();


}
