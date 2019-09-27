package com.example.lendandborrowclient.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Branch implements Serializable {

    public String Title;

    public String Address;

    @NonNull
    @Override
    public String toString() {
        return Title + ": " + Address;
    }
}
