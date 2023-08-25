package mp.br.quarkussocial;

import javax.transaction.Transactional;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mp.br.quarkussocial.domain.dto.UserRequest;
import mp.br.quarkussocial.domain.model.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {


    @POST
    @Transactional
    public Response createUser(UserRequest request){

        User user = new User();
        user.setNome(request.getNome());
        user.setIdade(request.getIdade());

        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers(){
        return Response.ok().build();
    }






}
