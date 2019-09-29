package com.example.lendandborrowclient.RestAPI;

import com.example.lendandborrowclient.Models.Admin;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.Models.Status;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HandyRestAPI {

    //region items

    @GET("items")
    Single<List<Item>> GetAllItems();

    @POST("items")
    Single<Integer> AddItem(@Body Item item);

    @GET("items/{id}")
    Single<Item> GetItem(@Path("id") int id);

    @DELETE("items/{id}")
    Single<List<Borrow>> DeleteItem(@Path("id") int itemId);

    @GET("items/{id}/availabilities")
    Single<List<Availability>> GetItemAvailabilities(@Path("id") int id);

    @GET("items/{id}/availabilities/all")
    Single<List<Availability>> GetItemFullAvailabilities(@Path("id") int id);

    //endregion

    //region borrows

    @POST("borrows")
    Single<ResponseBody> Borrow(@Body Borrow request);

    @GET("borrows/pending")
    Single<List<Borrow>> GetAllPendingRequests();

    @PUT("borrows/{id}/{status}")
    Single<List<Borrow>> UpdateBorrowStatus(@Path("id") int borrowId,
                                            @Path("status") Status newStatus);

    //endregion

    //region availabilities

    @POST("availabilities")
    Single<ResponseBody> AddAvailability(@Body Availability availability);

    @GET("availabilities/{id}")
    Single<Availability> GetAvailabilityById(@Path("id") int availabilityId);

    @DELETE("availabilities/{id}")
    Single<List<Borrow>> DeleteAvailability(@Path("id") int availabilityId);

    //endregion

    //region branches

    @GET("branches")
    Single<List<Branch>> GetBranches();

    @POST("branches")
    Single<String> AddBranch(@Body Branch branch);

    @DELETE("branches/{branchId}")
    Single<ResponseBody> DeleteBranch(@Path("branchId") int branchId);

    //endregion

    //region users

    @POST("users")
    Single<ResponseBody> ValidateUser(@Body Admin admin);

    //endregion
}
