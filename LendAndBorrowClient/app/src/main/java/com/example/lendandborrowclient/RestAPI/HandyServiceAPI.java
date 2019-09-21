package com.example.lendandborrowclient.RestAPI;

import com.example.lendandborrowclient.Models.Admin;
import com.example.lendandborrowclient.Models.Availability;
import com.example.lendandborrowclient.Models.Borrow;
import com.example.lendandborrowclient.Models.Branch;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.Models.Status;

import java.util.List;

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

public interface HandyServiceAPI {

    //region items

    @GET(WebApiConstants.Items.RelativeUrl)
    Single<List<Item>> GetAllItems();

    @GET(WebApiConstants.Items.SpecificItem)
    Single<Item> GetItem(@Path(WebApiConstants.Items.ItemId) int id);

    @POST(WebApiConstants.Items.RelativeUrl)
    Single<Integer> AddItem(@Body Item item);

    @DELETE(WebApiConstants.Items.SpecificItem)
    Single<List<Borrow>> DeleteItem(@Path(WebApiConstants.Items.ItemId) int itemId);

    @GET(WebApiConstants.Items.GetItemAvailabilities)
    Single<List<Availability>> GetItemAvailabilities(@Path(WebApiConstants.Items.ItemId) int id);

    //endregion

    @POST(WebApiConstants.Borrows.RelativeUrl)
    Single<ResponseBody> Borrow(@Body Borrow request);

    @GET(WebApiConstants.Borrows.PendingBorrows)
    Single<List<Borrow>> GetAllPendingRequests();

    @PUT(WebApiConstants.Borrows.UpdateBorrowStatus)
    Single<List<Borrow>> UpdateBorrowStatus(@Path(WebApiConstants.Borrows.BorrowId) int borrowId,
                                            @Path(WebApiConstants.Borrows.BorrowStatus) Status newStatus);

    //region Availabilities

    @GET(WebApiConstants.Availabilities.SpecificAvailability)
    Single<Availability> GetAvailabilityById(@Path(WebApiConstants.Availabilities.AvailabilityId) int availabilityId);

    @POST(WebApiConstants.Availabilities.RelativeUrl)
    Single<String> AddAvailability(@Body Availability availability);

    @DELETE(WebApiConstants.Availabilities.SpecificAvailability)
    Single<List<Borrow>> DeleteAvailability(@Path(WebApiConstants.Availabilities.AvailabilityId) int availabilityId);

    //endregion

    //region branches

    @GET(WebApiConstants.Branches.RelativeUrl)
    Single<List<Branch>> GetBranches();

    @POST(WebApiConstants.Branches.RelativeUrl)
    Single<String> AddBranch(@Body Branch branch);

    @DELETE(WebApiConstants.Branches.SpecificBranch)
    Single<ResponseBody> DeleteBranch(@Path(WebApiConstants.Branches.BranchId) int branchId);

    //endregion

    @POST(WebApiConstants.Users.RelativeUrl)
    Single<ResponseBody> ValidateUser(@Body Admin admin);
}
