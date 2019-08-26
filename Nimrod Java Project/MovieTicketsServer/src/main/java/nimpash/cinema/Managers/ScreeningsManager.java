package nimpash.cinema.Managers;

import nimpash.cinema.Services.SeatsSelectionTimeoutHandler;
import nimpash.cinema.Services.SeatsSelectionTimingService;
import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.objects.*;
import nimpash.cinema.utils.Constants;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;

public class ScreeningsManager implements SeatsSelectionTimeoutHandler
{
	private DataAccessObject<Screening> _screeningsDao;
	private DataAccessObject<SeatsSelection> _seatSelectionDao;
	private DataAccessObject<MovieDetails> _movieDao;
	private DataAccessObject<Hall> _hallDao;

	private final static ScreeningsManager _screeningManager = new ScreeningsManager();

	private ScreeningsManager()
	{
		_screeningsDao = new DataAccessObject<>(Screening.class);
		_movieDao = new DataAccessObject<>(MovieDetails.class);
		_seatSelectionDao = new DataAccessObject<>(SeatsSelection.class);
		_hallDao = new DataAccessObject<>(Hall.class);
	}

	public static ScreeningsManager GetInstance(){return _screeningManager; }

	public synchronized String SaveScreeningSeats(String screeningId, ArrayList<Seat> selectedSeats)
	{
		// Getting the screening seats to validate seats and update them
		Screening screeningSeats = _screeningsDao.ReadField(screeningId, Constants.Screening.SEATS);

		if (screeningSeats == null)
			return null;

		// Valid and update at the same time
		ArrayList<Row> seats = screeningSeats.Seats;

		// Checking if any of the seats are already occupied and marking them as occupied if not
		for (Seat seat : selectedSeats)
			if (seats.get(seat.RowNumber).Seats.get(seat.SeatNumber) == SeatState.Occupied.getValue())
				return null;
			else
				seats.get(seat.RowNumber).Seats.set(seat.SeatNumber, SeatState.Occupied.getValue());

		SeatsSelection selection = new SeatsSelection(screeningId, selectedSeats, LocalDateTime.now());

		String selectionId = _seatSelectionDao.CreateOne(selection);

		if (selectionId == null)
			return null;

		// Update the screening seats
		if (!_screeningsDao.UpdateField(screeningId,
										Constants.Screening.SEATS,
										seats))
		{
			_seatSelectionDao.DeleteOne(selectionId);

			return null;
		}

		// Add seats selection to the timers service, to release the seats in 15 minutes
		SeatsSelectionTimingService.AddSelection(selectionId, this);

		return selectionId;
	}

	// If a timer has timed out
	@Override
	public synchronized boolean SeatsSelectionTimedOut(String selectionId)
	{
		SeatsSelection selection = _seatSelectionDao.DeleteOne(selectionId);

		if (selection == null)
			return false;

		Screening screening = _screeningsDao.ReadField(selection.ScreeningId, Constants.Screening.SEATS);

		if (screening == null)
			return false;

		// Change the seats selected back to free
		selection.Seats.forEach(seat -> screening.Seats.get(seat.RowNumber).Seats.set(seat.SeatNumber, SeatState.Free.getValue()));

		// Release all the seats from the DB
		return _screeningsDao.UpdateField(screening.Id, Constants.Screening.SEATS, screening.Seats);
	}

	public boolean ReleaseSaveScreeningSeats(String selectionId)
	{
		return SeatsSelectionTimedOut(selectionId);
	}

	public synchronized Screening HandleNewScreening(Screening screening)
	{
		// Validate that the movie exists
		MovieDetails movie = _movieDao.ReadOne(screening.MovieId);
		if (movie == null)
			return null;

		// Validate that the hall exists
		Hall hall = _hallDao.ReadOne(screening.HallId);
		if (hall == null)
			return null;

		ArrayList<Row> seats = new ArrayList<>();

		// Create empty rows for the new screening
		for (int row = 0 ; row < hall.Row;row ++ ){
			seats.add(new Row(hall.Column));
		}

		screening.Seats = seats;

		// If the screening has no id, assign one
		if(screening.Id.isEmpty())
			screening.Id = ObjectId.get().toString();

		String screeningId = _screeningsDao.CreateOne(screening);

		if(screeningId == null)
			return null;

		return screening;

	}

}
