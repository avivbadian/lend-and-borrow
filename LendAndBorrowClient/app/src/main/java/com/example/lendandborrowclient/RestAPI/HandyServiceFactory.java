package com.example.lendandborrowclient.RestAPI;

import com.example.lendandborrowclient.Models.DateSerializers.LocalDateSerializer;
import com.example.lendandborrowclient.Models.DateSerializers.LocalDateTimeSerializer;
import com.example.lendandborrowclient.Models.DateSerializers.LocalTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.lang.reflect.Type;
import java.util.Date;
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
    { }

    public static HandyServiceAPI GetInstance()
    {
        if (m_service == null)
        {
            // Creating gson with custom date handling serializers
            GsonBuilder gsonBuilder = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateSerializer());

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(WebApiConstants.BaseUrl)
                    .client(new OkHttpClient.Builder().writeTimeout(10, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS).build())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            m_service = retrofit.create(HandyServiceAPI.class);
        }

        return m_service;
    }
}
