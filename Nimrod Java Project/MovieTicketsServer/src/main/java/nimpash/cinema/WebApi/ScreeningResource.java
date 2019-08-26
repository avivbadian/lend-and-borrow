package nimpash.cinema.WebApi;

import nimpash.cinema.Managers.ScreeningsManager;
import nimpash.cinema.objects.Screening;
import nimpash.cinema.objects.Seat;
import nimpash.cinema.utils.Constants;
import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.DataAccess.DataAccessObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/screenings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScreeningResource
{
    /**
     * @return all the screenings
     */
    @GET
    public Response GetAllScreenings()
    {
		DataAccessObject<Screening> dao = new DataAccessObject<>(Screening.class);

        List<Screening> screenings = dao.ReadAll();

        if (screenings != null)
        {
            return Response.ok(screenings).build();
        }
        else
            return Response.serverError().build();
    }

	/**
	 * @param screeningsId to get seats for
	 * @return screening seats
	 */
	@GET
	@Path("/{screeningsId}/seats")
	public Response GetScreeningSeats(@PathParam("screeningsId") String screeningsId)
	{
		DataAccessObject<Screening> dao = new DataAccessObject<>(Screening.class);

		Screening screening = dao.ReadField(screeningsId, Constants.Screening.SEATS);

			if (screening != null)
				if (screening.Seats != null)
					return Response.ok(screening.Seats).build();

		return Response.serverError().build();
	}

	/**
	 * @param screeningsId to save seats for
	 * @return seats selection id
	 */
	@POST
	@Path("/{screeningsId}/seats/save")
	public Response SaveSeatsInScreening(@PathParam("screeningsId") String screeningsId, ArrayList<Seat> selectedSeats)
	{
		ScreeningsManager manager = ScreeningsManager.GetInstance();

		String seatsSavedId = manager.SaveScreeningSeats(screeningsId, selectedSeats);

		if (seatsSavedId != null)
			return Response.status(Response.Status.CREATED).entity(seatsSavedId).build();

		return Response.serverError().build();
	}

	/**
	 * @param screeningsId to save seats for
	 * @return seats selection id
	 */
	@PUT
	@Path("/{screeningsId}/seats/save")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response ReleaseSaveSeatsInScreening(@PathParam("screeningsId") String screeningsId, @FormParam("selectionId") String selectionId)
	{
		ScreeningsManager manager = ScreeningsManager.GetInstance();

		return manager.ReleaseSaveScreeningSeats(selectionId) ?
			Response.ok().build() : Response.serverError().build();
	}

	@POST
	public Response AddScreening(Screening screening)
	{
		ScreeningsManager manager = ScreeningsManager.GetInstance();
		Screening scr = manager.HandleNewScreening(screening);

		if (scr != null)
			return Response.status(Response.Status.CREATED).entity(scr.Id).build();
		else
			return Response.serverError().build();

	}





	@DELETE
	@Path("/{screeningId}")
	public Response DeleteScreening(@PathParam("screeningId") String screeningid) {
		CRUD<Screening> crud = new DataAccessObject<>(Screening.class);

		if (crud.DeleteOne(screeningid) != null)
			return Response.ok().build();
		else
			return Response.serverError().build();
	}

}
