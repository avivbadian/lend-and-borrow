package com.example.lendandborrowclient.Models;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.time.LocalDate;

public class Availability {
    @JsonProperty("id")
    public int Id;

    @JsonProperty("item_id")
    public int Item_id;

    @JsonProperty("start_date")
    public Date Start_date;

    @JsonProperty("end_date")
    public Date End_date;
}