package com.example.lendandborrowclient.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Availability implements Serializable {

    public int Id;

    public int Item_id;

    public Date Start_date;

    public Date End_date;

    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormatter.format(Start_date) + " - " + dateFormatter.format(End_date);
    }
}
