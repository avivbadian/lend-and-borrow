package nimpash.cinema.objects;

import nimpash.cinema.objects.Converters.LocalDateTimeConverter;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;
import org.mongodb.morphia.annotations.Converters;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;

@Entity("purchases")
@Converters(LocalDateTimeConverter.class)
public class PurchaseDetails
{
	@Id
	public String Id;
	public ArrayList<Seat> Seats;
	public String MovieId;
	public String ScreeningId;
	public String Email;
	public String GivenName;
	public String LastName;
	public String PhoneNumber;
	public LocalDateTime PurchaseTime;
	public int TotalPrice;

	public PurchaseDetails()
	{

	}

	public PurchaseDetails(ArrayList<Seat> seats, String email, String givenName, String lastName, String phone, LocalDateTime purchaseTime)
	{
		Id = ObjectId.get().toString();
		Seats = seats;
		Email = email;
		GivenName = givenName;
		LastName = lastName;
		PhoneNumber = phone;
		PurchaseTime = purchaseTime;
	}
}
