package com.example.huangshan.common.service;

import com.example.huangshan.http.ResultObj;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 验证码接口
 */
public interface VerificationCodeService {

    @GET("verification_code/{phone}")
    Observable<ResultObj> getVerificationCode(@Path("phone") String phone);
}
