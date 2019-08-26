package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;


public class Admin {
    @JsonProperty("username")
    public String Username;

    @JsonProperty("password")
    public String Password;
}
