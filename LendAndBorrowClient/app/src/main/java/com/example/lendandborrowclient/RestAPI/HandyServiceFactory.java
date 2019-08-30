package com.example.lendandborrowclient.RestAPI;

import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class HandyServiceFactory
{
    private static HandyServiceAPI m_service = null;

    private HandyServiceFactory()
    {
    }

    public static HandyServiceAPI GetInstance()
    {
        if (m_service == null)
        {
            // Creating gson with custom date handling serializers
            GsonBuilder gson = new GsonBuilder();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebApiConstants.BaseUrl)
                    .client(new OkHttpClient.Builder().writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(60, TimeUnit.SECONDS).build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson.create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            m_service = retrofit.create(HandyServiceAPI.class);
        }

        return m_service;
    }
}
