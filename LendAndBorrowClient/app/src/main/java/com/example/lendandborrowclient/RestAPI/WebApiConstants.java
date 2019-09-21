package com.example.lendandborrowclient.RestAPI;

public final class WebApiConstants
{
    public final static String BaseUrl = "http://10.0.0.20:8080/";

    public final class Items
    {
        public final static String ItemId = "id";
        public final static String RelativeUrl = "items";
        public final static String SpecificItem = RelativeUrl + "/{id}";
        public final static String GetItemAvailabilities = SpecificItem + "/availabilities";
    }

    public final class Availabilities
    {
        public final static String AvailabilityId = "id";
        public final static String RelativeUrl = "availabilities";
        public final static String SpecificAvailability = RelativeUrl + "/{id}";
    }

    public class Borrows
    {
        public final static String BorrowId = "id";
        public final static String BorrowStatus = "status";
        public final static String RelativeUrl = "borrows";
        public final static String PendingBorrows =  RelativeUrl + "/pending";
        public final static String UpdateBorrowStatus = "borrows/{id}/{status}";
    }

    public class Branches
    {
        public final static String RelativeUrl = "branches";
        public final static String BranchId = "branchId";
        public final static String SpecificBranch = RelativeUrl + "/{branchId}";
    }

    public class Users
    {
        public final static String RelativeUrl = "users";
    }
}
