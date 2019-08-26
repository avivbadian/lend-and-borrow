package nimpash.cinema.Managers;

import nimpash.cinema.objects.Hall;
import nimpash.cinema.objects.Screening;
import nimpash.cinema.utils.Constants;
import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.objects.MovieDetails;

/**
 * Handles all the operation for halls
 */
public class HallsManager {

	private DataAccessObject<Screening> _screeningsDao;
	private DataAccessObject<MovieDetails> _movieDao;
	private DataAccessObject<Hall> _hallDao;

    //constractor
	public HallsManager()
	{
		_screeningsDao = new DataAccessObject<>(Screening.class);
		_movieDao = new DataAccessObject<>(MovieDetails.class);
		_hallDao = new DataAccessObject<>(Hall.class);
	}

	public boolean RemoveHall(String Id)
	{
		return ((_hallDao.DeleteOne(Id) != null) &&
				_screeningsDao.DeleteByField(Constants.Screening.HALL_ID, Id));
	}

}