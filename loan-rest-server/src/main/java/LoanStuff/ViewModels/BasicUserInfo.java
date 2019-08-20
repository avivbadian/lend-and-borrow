package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasicUserInfo {
    @JsonProperty("uid")
    public String Uid;

    @JsonProperty("username")
    public String Username;

    @JsonProperty("status")
    public String Status;
}
