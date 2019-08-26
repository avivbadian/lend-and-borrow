
package nimpash.cinema.Managers;

import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.Services.SeatsSelectionTimingService;
import nimpash.cinema.objects.Converters.ViewAndDataObjectConverter;
import nimpash.cinema.objects.*;
import nimpash.cinema.utils.EmailService;
import org.joda.time.LocalDateTime;

/**
 * Handles everything related to purchases
 */
public class PurchasesManager
{
	private DataAccessObject<MovieDetails> _movieDao;
	private DataAccessObject<Screening> _screeningDao;
	private DataAccessObject<SeatsSelection> _seatsSelectionDao;
	private DataAccessObject<PurchaseDetails> _purchaseDao;

	private final static PurchasesManager _purchaseManager = new PurchasesManager();

	private PurchasesManager()
	{
		_movieDao = new DataAccessObject<>(MovieDetails.class);
		_screeningDao = new DataAccessObject<>(Screening.class);
		_seatsSelectionDao = new DataAccessObject<>(SeatsSelection.class);
		_purchaseDao = new DataAccessObject<>(PurchaseDetails.class);
	}

	public static PurchasesManager GetInstance()
	{
		return _purchaseManager;
	}
	public synchronized PurchaseDetails HandleNewPurchase(PurchaseRequest request)
	{
		// Get screening
		Screening screening = _screeningDao.ReadOne(request.ScreeningId);

		if (screening == null)
			return null;

		// find and delete the selection from out DB if it exists (we dont need the seats themselves as the seats are already
		SeatsSelection seatsSelection = _seatsSelectionDao.DeleteOne(request.SeatsSelectionId);

		// Check that the selection exists
		if (seatsSelection == null)
			return null;

		// If the user has waited more than 15 minuts for his purchase we cancel it and release the seats if it wasnt released
		if (seatsSelection.SelectionTime.plusMinutes(15).isBefore(LocalDateTime.now()))
		{
			ScreeningsManager.GetInstance().ReleaseSaveScreeningSeats(seatsSelection.ScreeningId);
			return null;
		}

		// Convert the request to full data object
		PurchaseDetails purchase = ViewAndDataObjectConverter.PurchaseRequestToPurchaseDetails(request);

		if (purchase == null)
			return null;

		purchase.PurchaseTime = LocalDateTime.now();
		purchase.MovieId = screening.MovieId;
		purchase.Seats = seatsSelection.Seats;

		// Purchase was created, we can stop the seats countdown timer
		if (!SeatsSelectionTimingService.StopTimerSelection(request.SeatsSelectionId))
			return null;

		String resultId = _purchaseDao.CreateOne(purchase);

		if (resultId == null || resultId.isEmpty())
			return null;


		MovieDetails movie = _movieDao.ReadOne(screening.MovieId);

		EmailService.GetInstance().SendMail(purchase,movie,screening);

		return purchase;
	}

	public PurchaseDetails GetPurchaseDetails(String purchaseId)
	{
		return _purchaseDao.ReadOne(purchaseId);
	}

	public synchronized boolean RemovePurchase(String id)
	{
		return _purchaseDao.DeleteOne(id) != null;
	}
}