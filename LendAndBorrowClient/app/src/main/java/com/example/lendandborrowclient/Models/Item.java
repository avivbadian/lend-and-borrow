package com.example.lendandborrowclient.Models;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Item implements Serializable {

    public int Id;

    public String Title;

    public String Category;

    public String Description;

    public String Path;

    @NonNull
    @Override
    public String toString() {
        return Title;
    }
}
