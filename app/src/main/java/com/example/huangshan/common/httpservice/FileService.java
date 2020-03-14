package com.example.huangshan.common.httpservice;

import com.example.huangshan.http.UploadFileResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FileService {

    @POST("uploadFileByAccount")
    Observable<UploadFileResponse> upload(@Body RequestBody body);

    @POST("uploadComplaintEvidence")
    Observable<UploadFileResponse> uploadComplaintEvidence(@Body RequestBody body);
}
