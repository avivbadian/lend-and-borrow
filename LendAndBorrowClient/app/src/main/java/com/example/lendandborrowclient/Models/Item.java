package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Item {
    @JsonProperty("id")
    public int Id;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("description")
    public String Description;

    @JsonProperty("owner")
    public String Owner;

    @JsonProperty("category")
    public String Category;

    @JsonProperty("imagePath")
    public String ImagePath;

    @JsonProperty("isAvailable")
    public Boolean IsAvailable;
}
