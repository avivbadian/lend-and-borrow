package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class User {

    @JsonProperty("uid")
    public String Uid;

    @JsonProperty("username")
    public String Username;

    @JsonProperty("status")
    public String Status;

    @JsonProperty("rank")
    public String Rank;

    @JsonProperty("borrowCount")
    public int BorrowCount;

    @JsonProperty("lendingCount")
    public int LendingCount;

    @JsonProperty("items")
    public ArrayList<ItemPreview> Items;

    // Must have default empty ctor for deserialize
}
