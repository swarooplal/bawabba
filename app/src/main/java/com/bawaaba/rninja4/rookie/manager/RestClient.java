package com.bawaaba.rninja4.rookie.manager;

import android.content.Context;

import com.bawaaba.rninja4.rookie.utils.Constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by rninja4 on 11/1/17.
 */

public class RestClient {

    private Context context;


    public RestClient(Context context) {
        this.context = context.getApplicationContext();
    }

    public void updateContext(Context context) {
        if (context != null)
            this.context = context.getApplicationContext();
    }

    public Retrofit getAdapter() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.writeTimeout(200,TimeUnit.SECONDS);
        httpClient.readTimeout(120, TimeUnit.SECONDS);
        httpClient  .connectTimeout(120, TimeUnit.SECONDS)
                .build();//
        OkHttpClient mOkHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit;
    }

    public WebServiceApi getApiService() {
        WebServiceApi api = getAdapter().create(WebServiceApi.class);
        return api;
    }
    //
    public interface WebServiceApi {
        @GET("api/user/get_all_skills?")
        Call<ResponseBody> getCategorySkillsinInside(
                @Query("category") String category);
        @GET
        Call<ResponseBody> getCategorySkills(
                @Url String url);
        @FormUrlEncoded
        @POST("api/user/register")
        Call<ResponseBody> signUp(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("email") String email,
                @Field("fullname") String fullname,
                @Field("location") String location,
                @Field("skills") String skills,
                @Field("description") String description,
                @Field("category") String category,
                @Field("gender") String gender,
                @Field("password") String password,
                @Field("dob") String dateofbirth,
                @Field("phone") String phone,
                @Field("role") String role,
                @Field("profile_img") String profile_image,
                @Field("device") String device);

        @FormUrlEncoded
        @POST("api/user/logout")
        Call<ResponseBody> logout(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @Field("fcm_token") String fcm_token
        );

        @FormUrlEncoded
        @POST("api/user/register")
        Call<ResponseBody> signUp_hire(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("email") String email,
                @Field("fullname") String fullname,
                @Field("location") String location,
                @Field("phone") String phone,
                @Field("dob") String dob,
                @Field("gender") String gender,
                @Field("password") String password,
                @Field("role") String userRole,
                @Field("profile_img") String profile_image
        );

        @FormUrlEncoded
        @POST("api/user/quickblox")
        Call<ResponseBody> sendChatStatus(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("sender_id") String sender_id,
                @Field("sender_name") String sender_name,
                @Field("receiver_id") String receiver_id,
                @Field("receiver_name") String recerver_name,
                @Field("receiver_email") String receiver_email,
                @Field("read") String read,
                @Field("count") String count
        );

        /*email:arjun555@gmail.com
fullname:arjun
location:test
skills :legal
description   :test
category:1
gender:male
password:123456*/

        @FormUrlEncoded
        @POST("api/user/update_skill")
        Call<ResponseBody> updateSkill(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("skills") String skills,
                @Field("category") String category
        );

        // http://demo.rookieninja.com/api/user/update_language/
        @FormUrlEncoded
        @POST("api/user/update_language")
        Call<ResponseBody> updateLangauge(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("lang_id") String lang_id,
                @Field("delete_all") String delete_all
        );

        // http://demo.rookieninja.com/api/user/hire_email/
        @FormUrlEncoded
        @POST("api/user/hire_email")
        Call<ResponseBody> getInbox(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id

        );

        @POST("api/user/currency")
        Call<ResponseBody> getCurrency(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey

        );

        @FormUrlEncoded
        @POST("api/user/add_service")
        Call<ResponseBody> addServices(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @FieldMap Map<String, String> hashFields);

        @Multipart
        @POST("api/user/portfolioimg")
        Call<ResponseBody> uploadImage(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Part("user_id") RequestBody user_id,
                @Part List<MultipartBody.Part> files);

        @Multipart
        @POST("api/user/portfoliomedia")
        Call<ResponseBody> uploadVideoFile(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Part("user_id") RequestBody user_id,
                @Part("upload_type") RequestBody upload_type,
                @Part("url") RequestBody url,
                @Part("table_name") RequestBody table_name,
                @Part MultipartBody.Part files);

        @FormUrlEncoded
        @POST("api/user/portfoliomedia")
        Call<ResponseBody> uploadUrlVideo(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @Field("upload_type") String upload_type,
                @FieldMap Map<String, String> hashFields,
                @Field("table_name") String table_name
        );
        @FormUrlEncoded
        @POST("api/user/delete_dp")
        Call<ResponseBody> delete_profimg(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id

        );


        @Multipart
        @POST("api/user/portfoliomedia")
        Call<ResponseBody> uploadAudioFile(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Part("user_id") RequestBody user_id,
                @Part("title") RequestBody title,
                @Part("upload_type") RequestBody upload_type,
                @Part("url") RequestBody url,
                @Part("table_name") RequestBody table_name,
                @Part MultipartBody.Part files);


        @FormUrlEncoded
        @POST("api/user/portfoliomedia")
        Call<ResponseBody> uploadAudioUrls(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @Field("upload_type") String upload_type,
                @FieldMap Map<String, String> hashFields,
                @FieldMap Map<String, String> title,
                @Field("table_name") String table_name
        );

//        @FormUrlEncoded
//        @POST("api/user/portfoliodoc")
//        Call<ResponseBody> uploadFile(
//                @Header("Client-Service") String client_service,
//                @Header("Auth-Key") String authkey,
//                @Header("User-Id") String userId,
//                @Header("Token") String Token,
//                @Field("user_id") String user_id,
//                @Field("document_file[0]") String document_file,
//                @Field("title[0]") String title
//        );

        @FormUrlEncoded
        @POST("api/user/portfoliodoc")
        Call<ResponseBody> uploadFile(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @FieldMap Map<String, String> hashFields);


        @FormUrlEncoded
        @POST("api/user/edit_portfolio")
        Call<ResponseBody> deletePortfolio(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("table_name") String table_name,
                @Field("row_id") String row_id,
                @Field("action") String action
        );

        @FormUrlEncoded
        @POST("api/user/edit_portfolio")
        Call<ResponseBody> updatePortfolio(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("table_name") String table_name,
                @Field("row_id") String row_id,
                @Field("action") String action,
                @Field("title") String title
        );

        @FormUrlEncoded
        @POST("api/user/portfoliolink")
        Call<ResponseBody> addotherUrl(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @FieldMap Map<String, String> hashFields);

        @FormUrlEncoded
        @POST("api/user/profile")
        Call<ResponseBody> getProfile(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("user_id") String user_id);

        @FormUrlEncoded
        @POST("api/user/add_review")
        Call<ResponseBody> setRating(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @Field("profile_id") String profile_id,
                @Field("review") String review,
                @Field("rating") String rating,
                @Field("review_image") String review_image,
                @Field("reviewer_name") String reviewer_name);


        @FormUrlEncoded
        @POST("api/user/change_email")
        Call<ResponseBody> changeEmail(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("email") String email,
                @Field("new_email") String new_email);


        @FormUrlEncoded
        @POST("api/user/change_password")
        Call<ResponseBody> changePassword(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("password") String password,
                @Field("oldpassword") String oldpassword,
                @Field("email") String email);


        @FormUrlEncoded
        @POST("api/user/verify_profile")
        Call<ResponseBody> verifyProfile(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("id_document") String id_document,
                @Field("keycode_img") String keycode_img);


        @FormUrlEncoded
        @POST("api/user/edit_review")
        Call<ResponseBody> deleteReview(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("row_id") String table_name,
                @Field("rating") String row_id,
                @Field("review") String review,
                @Field("review_image") String review_image,
                @Field("action") String action,
                @Field("image") String image
        );

        @FormUrlEncoded
        @POST("api/user/email_validator")
        Call<ResponseBody> emailDescriptionValidation(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("email") String email,
                @Field("description") String description
        );

        @FormUrlEncoded
        @POST("api/user/general_setting")
        Call<ResponseBody> changeType(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id,
                @Field("role") String role,
                @Field("description") String description,
                @Field("category_id") String category_id,
                @Field("skills") String skills
        );


        @FormUrlEncoded
        @POST("api/user/deactivate_account")
        Call<ResponseBody> deactivateaccount(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("reason") String email

        );

        @FormUrlEncoded
        @POST("api/user/forget_password")
        Call<ResponseBody> forgetpassword(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,

                @Field("email") String reason


        );

        @FormUrlEncoded
        @POST("api/user/hire")
        Call<ResponseBody> contactform(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("profile_id") String profile_id,
                @Field("name") String name,
                @Field("email") String email,
                @Field("phone") String phone,
                @Field("message") String message

        );

        @FormUrlEncoded
        @POST("api/user/notification")
        Call<ResponseBody> notification(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,
                @Field("user_id") String user_id

        );

        @FormUrlEncoded
        @POST("api/user/hiremail_status")
        Call<ResponseBody> email_status(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("User-Id") String userId,
                @Header("Token") String Token,

                @Field("user_id") String user_id,
                @Field("row_id") String row_id,
                @Field("action") String action
        );

        @FormUrlEncoded
        @POST("api/user/login")
        Call<ResponseBody> login(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("email") String email,
                @Field("password") String password,
                @Field("fcm_token") String fcm_token,
                @Field("phone") String phone
        );

        @FormUrlEncoded
        @POST("api/user/search/")
        Call<ResponseBody> serachUser(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Field("keyword") String keyword,
                @Field("skills") String skills,
                @Field("location") String fcm_token
        );

        @FormUrlEncoded
        @POST("api/user/update_profile/")
        Call<ResponseBody> editUserDeatils(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("Token") String Token,
                @Header("User-Id") String User_id,
                @Field("user_id") String user_id,
                @Field("profile_img") String profile_img,
                @Field("fullname") String fullname,
                @Field("dob") String dob,
                @Field("phone") String phone,
                @Field("profile_url") String profile_url,
                @Field("location") String location,
                @Field("gender") String gender

        );
        @FormUrlEncoded
        @POST("api/user/update_aboutme")
        Call<ResponseBody> editAbout(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("Token") String Token,
                @Header("User-Id") String User_id,
                @Field("user_id") String user_id,
                @Field("aboutme") String aboutme
        );

        @FormUrlEncoded
        @POST("api/user/update_description")
        Call<ResponseBody> editDescription(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("Token") String Token,
                @Header("User-Id") String User_id,
                @Field("user_id") String user_id,
                @Field("description") String description
        );

        @FormUrlEncoded
        @POST("api/user/verify_profile/")
        Call<ResponseBody> verifyProfileDeatils(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("Token") String Token,
                @Header("User-Id") String User_id,
                @Field("user_id") String user_id,
                @Field("id_document") String id_document,
                @Field("keycode_img") String keycode_img
        );

        @FormUrlEncoded
        @POST("api/user/update_socialmedia/")
        Call<ResponseBody> addSocialMedia(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("Token") String Token,
                @Header("User-Id") String User_id,
                @FieldMap Map<String, String> hashFields
        );
        @FormUrlEncoded
        @POST("api/user/report/")
        Call<ResponseBody> reportAbuse(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @Header("Token") String Token,
                @Header("User-Id") String User_id,
                @Field("user_id") String user_id,
                @Field("profile_id") String profile_id,
                @Field("message") String message
        );

        @FormUrlEncoded
        @POST("api/user/profile")
        Call<ResponseBody> profileDetails(
                @Header("Client-Service") String client_service,
                @Header("Auth-Key") String authkey,
                @FieldMap Map<String, String> hashFields
        );
    }

}
