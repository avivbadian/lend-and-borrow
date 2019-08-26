package nimpash.cinema.WebApi;

import nimpash.cinema.objects.Screening;
import nimpash.cinema.Managers.MoviesManager;
import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.objects.MovieDetails;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class MoviesResource
{
    /**
     * @return all the movies
    .*/
    @GET
    public Response GetAllMoviesDetails()
    {
        CRUD<MovieDetails> crud = new DataAccessObject<>(MovieDetails.class);

        List<MovieDetails> movies = crud.ReadAll();

        if (movies != null && movies.size() != 0)
            return Response.ok(movies).build();
        else
            return Response.serverError().build();
    }

    @GET
    @Path("/{movieId}/screenings")
    public Response GetMovieScreenings(@PathParam("movieId") String  movieId, @QueryParam("future") boolean futureDates)
    {

        MoviesManager moviesManager = new MoviesManager();

        List<Screening> newMovieScreenings = moviesManager.HandleGetMovieScreenings(movieId, futureDates);

        return newMovieScreenings != null  ?
                Response.ok(newMovieScreenings).build():
                Response.serverError().build();
    }

    /**
     * @param movie object to add to DB
     * @return movie created URL
     */
    @POST
    public Response AddMovie(MovieDetails movie)
    {
        MoviesManager moviesManager = new MoviesManager();

        MovieDetails newMovie = moviesManager.HandleNewMovie(movie);

        return newMovie != null ?
                Response.status(Response.Status.CREATED).entity(newMovie.Id).build() :
                Response.serverError().build();
    }

    /**
     * @param movieId of the movie to delete
     * @return deletion status message
     */
    @DELETE
    @Path("/{movieId}")
    public Response DeleteMovie(@PathParam("movieId") String movieId)
    {
        MoviesManager moviesManager = new MoviesManager();

        return moviesManager.RemoveMovie(movieId) ?
                Response.ok().build():
                Response.serverError().build();
    }
}
