package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Category {

    @JsonProperty("id")
    public int Id;

    @JsonProperty("name")
    public String Name;
}
