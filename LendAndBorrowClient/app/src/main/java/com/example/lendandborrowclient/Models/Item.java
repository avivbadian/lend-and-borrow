package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty("id")
    public int Id;

    @JsonProperty("title")
    public String Title;

    @JsonProperty("category")
    public String Category;

    @JsonProperty("description")
    public String Description;
}
