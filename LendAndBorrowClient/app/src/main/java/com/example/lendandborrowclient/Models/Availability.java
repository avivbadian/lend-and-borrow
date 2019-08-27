package com.example.lendandborrowclient.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Availability {
    @JsonProperty("id")
    public int Id;

    @JsonProperty("item_id")
    public int Item_id;

    @JsonProperty("branch")
    public int Branch;

    @JsonProperty("start_date")
    public Date Start_date;

    @JsonProperty("end_date")
    public Date End_date;
}
