package com.example.lendandborrowclient.Models;

public class User {

    private String user;
    private String password;

    public User()
    {  }

    public User(String user, String password)
    {
        this.user = user;
        this.password = password;
    }

    public String getPassword()
    {
        return password;
    }

    public String getUser()
    {
        return user;
    }
}
