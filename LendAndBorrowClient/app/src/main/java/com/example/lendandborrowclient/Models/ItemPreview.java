package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemPreview {

    @JsonProperty("id")
    public int Id;

    @JsonProperty("name")
    public String Name;

    @JsonProperty("isAvailable")
    public boolean IsAvailable;

    @JsonProperty("imagePath")
    public String ImagePath;

}
