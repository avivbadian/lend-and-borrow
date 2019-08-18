package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("uid")
    public String Uid;

    @JsonProperty("username")
    public String Username;

    @JsonProperty("status")
    public String Status;
}
