package nimpash.cinema.Managers;

import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.objects.MovieDetails;
import nimpash.cinema.objects.Screening;
import nimpash.cinema.utils.Constants;
import nimpash.cinema.DataAccess.DataAccessObject;
import org.bson.types.ObjectId;
import org.joda.time.LocalDateTime;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles all the operation for movies
 */
public class MoviesManager
{
	private DataAccessObject<Screening> _screeningsDao;
	private DataAccessObject<MovieDetails> _movieDao;

	public MoviesManager()
	{
		_screeningsDao = new DataAccessObject<>(Screening.class);
		_movieDao = new DataAccessObject<>(MovieDetails.class);

	}

	public MovieDetails HandleNewMovie(MovieDetails movie)
	{
		// If we have no id, allocate a new one
		if ( movie.Id == null || movie.Id.isEmpty())
			movie.Id = ObjectId.get().toString();

		String movieId = _movieDao.CreateOne(movie);
		if (movieId == null)
			return null;

		return movie;
	}

	public boolean RemoveMovie(String  Id)
	{
		return ( _screeningsDao.DeleteByField(Constants.Screening.MOVIE_ID, Id) &&
				(_movieDao.DeleteOne(Id) != null));
	}

	public List<Screening> HandleGetMovieScreenings(String movieId, boolean futureDates)
	{
		CRUD<Screening> crud = new DataAccessObject<>(Screening.class);

		List<Screening> screenings = crud.ReadByField(Constants.Screening.MOVIE_ID, movieId);

        if(screenings != null)
		{
			if (futureDates)
				screenings = screenings.stream().filter(screening -> screening.Time.isAfter(LocalDateTime.now())).collect(Collectors.toList());

			return screenings;
		}

		return null;
	}
}