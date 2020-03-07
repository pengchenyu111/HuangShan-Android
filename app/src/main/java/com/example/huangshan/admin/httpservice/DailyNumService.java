package com.example.huangshan.admin.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 每日数据
 */
public interface DailyNumService {

    @GET("daily_nums/{date}")
    Observable<ResultObj> getOneDayNum(@Path("date") String date);

    @GET("daily_nums/period/start/{start}/end/{end}")
    Observable<ResultObj> getPeriodDailyNum(@Path("start") String start, @Path("end") String end);
}
