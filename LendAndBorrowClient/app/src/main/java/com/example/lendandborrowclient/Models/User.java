package com.example.lendandborrowclient.Models;

public class User {
    public String Uid;
    public String Username;
    public String Status;

    public User(String uid, String name, String status) {
        Uid = uid;
        Username = name;
        Status = status;
    }
}
