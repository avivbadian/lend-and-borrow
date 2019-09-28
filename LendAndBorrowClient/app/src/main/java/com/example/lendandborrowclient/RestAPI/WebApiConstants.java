package com.example.lendandborrowclient.RestAPI;

final class WebApiConstants
{
    final static String BaseUrl = "http://10.0.0.15:8080/";

    final class Items
    {
        final static String ItemId = "id";
        final static String RelativeUrl = "items";
        final static String SpecificItem = RelativeUrl + "/{id}";
        final static String GetItemAvailabilities = SpecificItem + "/availabilities";
        final static String GetItemFullAvailabilities = GetItemAvailabilities + "/all";
    }

    final class Availabilities
    {
        final static String AvailabilityId = "id";
        final static String RelativeUrl = "availabilities";
        final static String SpecificAvailability = RelativeUrl + "/{id}";
    }

    class Borrows
    {
        final static String BorrowId = "id";
        final static String BorrowStatus = "status";
        final static String RelativeUrl = "borrows";
        final static String PendingBorrows =  RelativeUrl + "/pending";
        final static String UpdateBorrowStatus = "borrows/{id}/{status}";
    }

    class Branches
    {
        final static String RelativeUrl = "branches";
        final static String BranchId = "branchId";
        final static String SpecificBranch = RelativeUrl + "/{branchId}";
    }

    class Users
    {
        final static String RelativeUrl = "users";
    }
}
