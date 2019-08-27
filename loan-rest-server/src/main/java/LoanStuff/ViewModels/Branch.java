package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Branch {
    @JsonProperty("title")
    public String Title;

    @JsonProperty("address")
    public String Address;
}
