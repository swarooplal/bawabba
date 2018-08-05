package com.bawaaba.rninja4.rookie.retrofit;

import com.bawaaba.rninja4.rookie.models.VideoUploadinResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("api/user/portfoliomedia")
    Call<VideoUploadinResponse> uploadVideoFile(
            @Header("Client-Service") String client_service,
            @Header("Auth-Key") String authkey,
            @Header("User-Id") String userId,
            @Header("Token") String Token,
            @Part("user_id") RequestBody user_id,
            @Part("upload_type") RequestBody upload_type,
            @Part("url") RequestBody url,
            @Part("table_name") RequestBody table_name,
            @Part MultipartBody.Part files);

}
