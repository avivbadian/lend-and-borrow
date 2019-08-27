package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Branch {
    @JsonProperty("id")
    public int Id;

    @JsonProperty("title")
    public String Title;

    @JsonProperty("address")
    public String Address;
}
