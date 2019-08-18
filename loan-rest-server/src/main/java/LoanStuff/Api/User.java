package LoanStuff.Api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("uid")
    public String Uid;

    @JsonProperty("username")
    public String Username;

    @JsonProperty("status")
    public String Status;

    // Must have default empty ctor for deserialize
}
