package LoanStuff.ViewModels;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

//--- id, availability, borrow_date, return_date, phone, email,first name, last name
public class Borrow {
    @JsonProperty("id")
    public int Id;

    @JsonProperty("availability")
    public int Availability;

    @JsonProperty("phone")
    public String Phone;

    @JsonProperty("email")
    public String Email;

    @JsonProperty("first_name")
    public String First_name;

    @JsonProperty("last_name")
    public String Last_name;

}
