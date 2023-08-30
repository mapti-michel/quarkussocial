package mp.br.quarkussocial.domain.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import mp.br.quarkussocial.domain.model.User;
import mp.br.quarkussocial.domain.repository.UserRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
public class UserResource {

    public static final String USERS = "users";
    private UserRepository repository;

    @Inject
    public UserResource(UserRepository repository) {
        this.repository = repository;
    }

    @POST
    @Transactional
    public Response createUser(@Valid UserRequest request) {

        User user = new User();

        user.setNome(request.getNome());
        user.setIdade(request.getIdade());

        repository.persist(user);

        return Response.ok(user).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response listAllUsers() {

        PanacheQuery<User> query = repository.findAll();

        return Response.ok(query.list()).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {

        User user = repository.findById(id);

        if (user != null) {
            repository.delete(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserRequest request) {

        User user = repository.findById(id);

        if (user != null) {
            user.setNome(request.getNome());
            user.setIdade(request.getIdade());

            return Response.ok().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


}
