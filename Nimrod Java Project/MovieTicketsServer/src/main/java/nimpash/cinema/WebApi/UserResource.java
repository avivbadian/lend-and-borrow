package nimpash.cinema.WebApi;

import nimpash.cinema.Managers.UserManager;
import nimpash.cinema.objects.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
    @POST
    public Response ValidateUser(User user)
    {
        UserManager userManager = new UserManager();

        User validatedUser = userManager.HandleValidateUser(user);

        return validatedUser != null ?
                Response.ok(validatedUser).build():
                Response.serverError().build();
    }
}
