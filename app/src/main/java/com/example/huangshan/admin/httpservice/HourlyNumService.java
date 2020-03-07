package com.example.huangshan.admin.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HourlyNumService {

    @GET("hourly_nums/period/start/{start}/end/{end}")
    Observable<ResultObj> getPeriodHourlyNum(@Path("start") String start, @Path("end") String end);
}
