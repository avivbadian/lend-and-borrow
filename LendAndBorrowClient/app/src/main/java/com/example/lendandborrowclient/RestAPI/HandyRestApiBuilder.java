package com.example.lendandborrowclient.RestAPI;

import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class HandyRestApiBuilder
{
    final static String BaseUrl = "http://10.0.0.15:8080/";

    private static HandyRestAPI m_service = null;
    private HandyRestApiBuilder()
    { }

    public static HandyRestAPI GetInstance()
    {
        if (m_service == null)
        {
            // Creating gson with custom date handling serializers
            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .client(new OkHttpClient.Builder().writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS).build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            m_service = retrofit.create(HandyRestAPI.class);
        }

        return m_service;
    }
}
