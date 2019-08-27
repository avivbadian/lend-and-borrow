package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Admin {
    @JsonProperty("username")
    public String Username;

    @JsonProperty("password")
    public String Password;

    public Admin()
    { }

    public Admin(String user, String password)
    {
        Username = user;
        Password = password;
    }
}
