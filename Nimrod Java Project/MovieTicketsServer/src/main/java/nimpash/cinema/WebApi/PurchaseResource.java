package nimpash.cinema.WebApi;

import nimpash.cinema.Managers.PurchasesManager;
import nimpash.cinema.objects.PurchaseDetails;
import nimpash.cinema.objects.PurchaseRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Nimrod on 14-Jun-17
 */


@Path("/purchases")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PurchaseResource
{
    /**
     * @return all the movies
    .*/
    @GET
	@Path("{purchaseId}")
    public Response GetPurchaseDetails(@PathParam("purchaseId") String purchaseId)
    {
		PurchasesManager manager = PurchasesManager.GetInstance();

		PurchaseDetails purchaseDetails = manager.GetPurchaseDetails(purchaseId);

		return purchaseDetails == null ?
				Response.serverError().build() :
				Response.ok(purchaseDetails).build();
    }

    /**
     * @param id of purchase to delete
     * @return deletion status message
     */
    @DELETE
    @Path("{purchaseId}")
    public Response DeletePurchase(@PathParam("purchaseId") String id) {
        PurchasesManager manager = PurchasesManager.GetInstance();

        return manager.RemovePurchase(id) ?
        Response.ok().build() : Response.serverError().build();
    }

    @POST
	public Response AddPurchase(PurchaseRequest request)
	{
		PurchasesManager manager = PurchasesManager.GetInstance();

		PurchaseDetails purchase = manager.HandleNewPurchase(request);

		if (purchase != null)
			return Response.status(Response.Status.CREATED).entity(purchase.Id).build();
		else
			return Response.serverError().build();
	}
}


