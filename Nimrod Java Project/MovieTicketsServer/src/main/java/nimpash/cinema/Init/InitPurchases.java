package nimpash.cinema.Init;

import nimpash.cinema.objects.PurchaseDetails;
import nimpash.cinema.objects.Seat;
import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.DataAccess.DataAccessObject;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Random;

public class InitPurchases
{
	public static ArrayList<String> AddPurchasesToDB(ArrayList<String> screeningIds, ArrayList<String> movieIds)
	{
		ArrayList<PurchaseDetails> purchases = new ArrayList<>();

		ArrayList<Seat> seats = new ArrayList<>();

		Random rnd = new Random();

		for (int i = 0; i < 4; i++)
			seats.add(new Seat(rnd.nextInt(5) + 7, rnd.nextInt(5) + 7));

		for (int scrIndex = 0; scrIndex < screeningIds.size(); scrIndex++)
		{
			for (int movieIndex = 0; movieIndex < movieIds.size(); movieIndex++)
			{
				PurchaseDetails purchase = new PurchaseDetails();
				purchase.ScreeningId = screeningIds.get(scrIndex);
				purchase.PhoneNumber = "0545964669";
				purchase.Email = "nimrodg14@gmail.com";
				purchase.GivenName = "נמרוד";
				purchase.LastName = "גנז";
				purchase.PurchaseTime = LocalDateTime.now().plusDays(movieIndex);
				purchase.MovieId = movieIds.get(movieIndex);
				purchase.Seats = seats;
				purchase.Id = ObjectId.get().toString();

				purchases.add(purchase);
			}
		}

		CRUD<PurchaseDetails> crud = new DataAccessObject<>(PurchaseDetails.class);
		crud.DeleteAll();
		crud.CreateMany(purchases);

		return null;
	}
}
