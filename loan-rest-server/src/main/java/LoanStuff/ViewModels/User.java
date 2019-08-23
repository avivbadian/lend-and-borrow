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

    @JsonProperty("imagePath")
    public String ImagePath;

    // Must have default empty ctor for deserialize
}
