package com.bawaaba.rninja4.rookie.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Apiclient {

    public static final String BASE_URL = "https://test378.bawabba.com/";

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(480, TimeUnit.SECONDS)
            .connectTimeout(480, TimeUnit.SECONDS)
            .writeTimeout(482,TimeUnit.SECONDS)
            .build();
}
