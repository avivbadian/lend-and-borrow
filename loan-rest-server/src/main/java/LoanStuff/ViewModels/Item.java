package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {

    @JsonProperty("id")
    public int Id;

    @JsonProperty("ownerId")
    public String OwnerId;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("category")
    public String Category;

    @JsonProperty("image")
    public Byte[] Image;
}
