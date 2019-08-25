package com.example.lendandborrowclient.RestAPI;

import com.example.lendandborrowclient.Models.Category;
import com.example.lendandborrowclient.Models.Item;
import com.example.lendandborrowclient.Models.ItemPreview;
import com.example.lendandborrowclient.Models.UserAction;

import java.util.List;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface WebServiceAPI {

    // Items
    @GET(RoutesConstants.Items.ResourceName)
    Single<List<ItemPreview>> GetAllItems();

    @GET(RoutesConstants.Items.ItemById)
    Single<Item> GetItemById(@Path(RoutesConstants.Items.ItemId) int itemId);

    @PUT(RoutesConstants.Items.ItemById)
    Single<ResponseBody> UpdateItem(@Path(RoutesConstants.Items.ItemId) int itemId);

    @POST(RoutesConstants.Items.ResourceName)
    Single<String> AddItem(@Body Item item);

    @DELETE(RoutesConstants.Items.ItemById)
    Single<ResponseBody> DeleteItem(@Path(RoutesConstants.Items.ItemId) int itemId);


    // Categories
    @GET(RoutesConstants.Categories.ResourceName)
    Single<List<Category>> GetAllCategories();

    @GET(RoutesConstants.Categories.GetItemsOfSpecificCategory)
    Single<Category> GetCategoryItems(@Path(RoutesConstants.Categories.CategoryId) int categoryId);


    // Users
    @GET(RoutesConstants.Users.GetUserItems)
    Single<List<ItemPreview>> GetUserItems(@Path(RoutesConstants.Users.Username) String username);

    @GET(RoutesConstants.Users.GetUserActivity)
    Single<List<UserAction>> GetUserActivity(@Path(RoutesConstants.Users.Username) String username);

    @POST(RoutesConstants.Users.ResourceName)
    Single<String> CreateUser(@Body String username, @Body String password);
}
