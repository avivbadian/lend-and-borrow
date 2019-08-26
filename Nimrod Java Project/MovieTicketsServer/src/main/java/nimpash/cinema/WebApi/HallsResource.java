package nimpash.cinema.WebApi;

import nimpash.cinema.Managers.HallsManager;
import nimpash.cinema.DataAccess.CRUD;
import nimpash.cinema.DataAccess.DataAccessObject;
import nimpash.cinema.objects.Hall;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/halls")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HallsResource
{
	/**
	*@return all halls
	*/
	@GET
	public Response GetAllHalls(){
		DataAccessObject<Hall> dao = new DataAccessObject<>(Hall.class);
		List<Hall> halls = dao.ReadAll();

		if (halls != null)
		{
			return Response.ok(halls).build();
		}
		else
			return Response.serverError().build();
	}

	@POST
	public Response AddHall(Hall hall)
	{
		CRUD<Hall> dao = new DataAccessObject<>(Hall.class);

		String id = dao.CreateOne(hall);

		return !id.isEmpty() ?
				Response.status(Response.Status.CREATED).entity(id).build() :
				Response.serverError().build();
	}


	/**
	 * @param hallId of the movie to delete
	 * @return deletion status message
	 */
	@DELETE
	@Path("/{hallId}")
	public Response deleteHall(@PathParam("hallId") String hallId) {

		HallsManager hallsManager = new HallsManager();

		return hallsManager.RemoveHall(hallId) ?
				Response.ok().build() :
				Response.serverError().build();
	}
}
