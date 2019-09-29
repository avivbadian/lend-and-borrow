package com.example.lendandborrowclient.RestAPI;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.lendandborrowclient.NotificationListeners.ServerNotificationsListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public final class HandyRestApiBuilder
{
    static String BaseUrl = "http://localhost:8080/";
    private static HandyRestAPI m_service = null;

    private HandyRestApiBuilder()
    { }

    public static HandyRestAPI GetInstance() {
        if (m_service == null) {
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

    public static void SetServerUrl(ServerNotificationsListener listener) {
        DatabaseReference firebaseDB = FirebaseDatabase.getInstance().getReference();
        firebaseDB.child("server_url").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                BaseUrl = dataSnapshot.getValue().toString();
                listener.HttpClientCreated();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("HandyRestApiBuilder", "Failed getting server url : " + databaseError.getMessage());
            }
        });
    }
}
