package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemPreview {

    @JsonProperty("id")
    public int Id;

    @JsonProperty("ownerId")
    public String OwnerId;

    @JsonProperty("lenderId")
    public String LenderId;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("category")
    public String Category;
}
