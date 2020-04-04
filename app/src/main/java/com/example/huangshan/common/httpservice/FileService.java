package com.example.huangshan.common.httpservice;

import com.example.huangshan.admin.activity.ListAdminsActivity;
import com.example.huangshan.http.UploadFileResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FileService {

    @POST("uploadFileByAccount")
    Observable<UploadFileResponse> upload(@Body RequestBody body);

    @POST("uploadComplaintEvidence")
    Observable<UploadFileResponse> uploadComplaintEvidence(@Body RequestBody body);

    @GET("scenic/album/{id}")
    Observable<List<String>> getScenicAlbum(@Path("id") long id);

    @GET("hotel/album/{id}")
    Observable<List<String>> getHotelAlbum(@Path("id") long id);
}
