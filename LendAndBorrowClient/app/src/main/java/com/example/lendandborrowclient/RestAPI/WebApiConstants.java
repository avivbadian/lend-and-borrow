package com.example.lendandborrowclient.RestAPI;

public final class WebApiConstants
{
    public final static String BaseUrl = "http://10.0.2.2:8080/";

    public final class Items
    {
        public final static String ItemId = "id";
        public final static String RelativeUrl = "items";
        public final static String Url = BaseUrl + RelativeUrl;
        public final static String GetAllItems = RelativeUrl;
        public final static String SpecificItem = RelativeUrl + "/{" + ItemId + "}";
        public final static String GetItemAvailabilities = SpecificItem + "/availabilities";
    }

    public final class Availabilities
    {
        public final static String AvailabilityId = "id";
        public final static String RelativeUrl = "availabilities";
        public final static String SpecificAvailability = RelativeUrl + "/{" + AvailabilityId + "}";
//        public final static String SaveSeats =  + "/seats/save";
    }

//    public final class Images
//    {
//        public final static String RelativeUrl = "images";
//        public final static String Url = BaseUrl  + RelativeUrl;
//        public final static String ImageName = "name";
//        public final static String RelativeGetImage = RelativeUrl + "/{" + ImageName + "}";
//    }

    public class Borrows
    {
        public final static String RelativeUrl = "borrows";
        public final static String Url = BaseUrl  + RelativeUrl;
        public final static String PurchaseId = "purchaseId";
        public final static String RelativeGetPurchase = RelativeUrl + "/{" + PurchaseId + "}";
    }

    public class Branches
    {
        public final static String RelativeUrl = "branches";
        public final static String Url = BaseUrl  + RelativeUrl;
        public final static String BranchId = "branchId";
        public final static String SpecificBranch = RelativeUrl + "/{" + BranchId + "}";
    }

    public class Users
    {
        public final static String RelativeUrl = "users";
    }
}
