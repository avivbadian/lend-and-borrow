package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class User {
    @JsonProperty("username")
    public String Username;

    @JsonProperty("bio")
    public String Bio;

    @JsonProperty("imagepath")
    public String ImagePath;
}
