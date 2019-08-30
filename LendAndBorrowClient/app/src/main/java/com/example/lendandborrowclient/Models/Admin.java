package com.example.lendandborrowclient.Models;

import java.io.Serializable;

public class Admin implements Serializable {

    public String Username;

    public String Password;

    // Used for deserialize
    public Admin()
    { }

    public Admin(String user, String password)
    {
        Username = user;
        Password = password;
    }
}
