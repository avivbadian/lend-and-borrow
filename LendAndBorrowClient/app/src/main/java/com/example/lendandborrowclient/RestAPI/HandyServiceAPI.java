package com.example.lendandborrowclient.RestAPI;

import com.example.lendandborrowclient.Models.Admin;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HandyServiceAPI {
    @GET(WebApiConstants.Items.RelativeUrl)
    Single<List<Item>> GetAllItems();

    @GET(WebApiConstants.Items.SpecificItem)
    Call<Item> GetItem(@Path(WebApiConstants.Items.ItemId) int id);

    @POST(WebApiConstants.Items.RelativeUrl)
    Single<String> AddItem(@Body Item item);

    @DELETE(WebApiConstants.Items.SpecificItem)
    Single<ResponseBody> DeleteItem(@Path(WebApiConstants.Items.ItemId) int itemId);
//
//    @GET(WebApiConstants.Images.RelativeGetImage)
//    Call<ResponseBody> GetMoviePicture(@Path(WebApiConstants.Images.ImageName) String name);

    @GET(WebApiConstants.Items.GetItemAvailabilities)
    Single<List<Availability>> GetItemAvailabilities(@Path(WebApiConstants.Items.ItemId) int id, @Query("future") boolean futureScreening);
//
//    @POST(WebApiConstants.Screenings.SaveSeats)
//    Single<String> SaveSelectedSeats(@Path(WebApiConstants.Screenings.ScreeningId) String screeningId, @Body List<Seat> seats);

//    @FormUrlEncoded
//    @PUT(WebApiConstants.Screenings.SaveSeats)
//    Single<ResponseBody> CancelSeatSelection(@Path(WebApiConstants.Screenings.ScreeningId) String screeningId, @Field("selectionId") String selectionId);

    @POST(WebApiConstants.Borrows.RelativeUrl)
    Single<String> Borrow(@Body Borrow request);

//    @Multipart
//    @POST(WebApiConstants.Images.RelativeUrl)
//    Single<ResponseBody> UploadImage(@Part MultipartBody.Part image);

    @GET(WebApiConstants.Branches.RelativeUrl)
    Single<List<Branch>> GetBranches();

    @POST(WebApiConstants.Availabilities.RelativeUrl)
    Single<String> AddAvailability(@Body Availability availability);

    @DELETE(WebApiConstants.Availabilities.SpecificAvailability)
    Single<ResponseBody> DeleteAvailability(@Path(WebApiConstants.Availabilities.AvailabilityId) int availabilityId);

    @POST(WebApiConstants.Branches.RelativeUrl)
    Single<String> AddBranch(@Body Branch branch);

    @DELETE(WebApiConstants.Branches.SpecificBranch)
    Single<ResponseBody> DeleteBranch(@Path(WebApiConstants.Branches.BranchId) int branchId);

    @POST(WebApiConstants.Users.RelativeUrl)
    Single<Admin> ValidateUser(@Body Admin admin);
}
