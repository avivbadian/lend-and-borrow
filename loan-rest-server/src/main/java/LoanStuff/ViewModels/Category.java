package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Category {

    @JsonProperty("name")
    public String Name;

    @JsonProperty("numberOfItems")
    public int NumberOfItems;
}
