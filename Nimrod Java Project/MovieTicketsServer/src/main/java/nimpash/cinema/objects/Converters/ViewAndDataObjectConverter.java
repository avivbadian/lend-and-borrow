package nimpash.cinema.objects.Converters;

import nimpash.cinema.objects.PurchaseDetails;
import nimpash.cinema.objects.PurchaseRequest;
import org.bson.types.ObjectId;

public class ViewAndDataObjectConverter
{
	public static PurchaseDetails PurchaseRequestToPurchaseDetails(PurchaseRequest request)
	{
		try
		{
			PurchaseDetails purchase = new PurchaseDetails();

			purchase.Email = request.Email;
			purchase.GivenName = request.GivenName;
			purchase.LastName = request.LastName;
			purchase.PhoneNumber = request.PhoneNumber;
			purchase.ScreeningId = request.ScreeningId;
			purchase.TotalPrice = request.TotalPrice;
			purchase.Id = ObjectId.get().toString();

			return purchase;
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return null;
		}
	}
}


