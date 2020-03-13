package com.example.huangshan.common.httpservice;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SuggestionService {

    @POST("suggestions")
    Observable<ResultObj> sendSuggestion(@Body RequestBody requestBody);
}
