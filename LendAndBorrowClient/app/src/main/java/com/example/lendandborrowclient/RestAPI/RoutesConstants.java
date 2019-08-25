package com.example.lendandborrowclient.RestAPI;

public class RoutesConstants {

    // TODO: change this address in the end
    public final static String BaseURL = "http://10.0.2.2:8080/api/";

    public final class Items
    {
        public final static String ResourceName = "items";
        public final static String ItemId = "id";

        public final static String ItemById = ResourceName + "/{" + ItemId + "}";
    }

    public final class Users
    {
        public final static String ResourceName = "users";
        public final static String Username = "username";

        public final static String UserByUsername = ResourceName + "/{" + Username + "}";
        public final static String GetUserActivity = ResourceName + "/{" + Username + "}/activity";
        public final static String GetUserProfile = ResourceName + "/{" + Username + "}/profile";
        public final static String GetUserItems = ResourceName + "/{" + Username + "}/items";
    }

    public final class Categories
    {
        public final static String ResourceName = "categories";
        public final static String CategoryId = "categoryId";

        public final static String GetAllCategories = ResourceName;
        public final static String GetItemsOfSpecificCategory = ResourceName + "/{" + CategoryId + "}/items" ;
    }
}
