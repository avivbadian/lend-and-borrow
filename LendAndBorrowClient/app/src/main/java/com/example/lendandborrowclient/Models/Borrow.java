package com.example.lendandborrowclient.Models;

import java.io.Serializable;

//--- id, availability, borrow_date, return_date, phone, email,first name, last name
public class Borrow implements Serializable {

    public int Id;

    public String Phone;

    public String Email;

    public String First_name;

    public String Last_name;

    public String Branch;

    public int Availability;

    public Status Status;
}
