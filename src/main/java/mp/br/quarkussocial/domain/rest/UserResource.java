package mp.br.quarkussocial.domain.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import mp.br.quarkussocial.domain.model.User;
import mp.br.quarkussocial.domain.repository.UserRepository;
import mp.br.quarkussocial.domain.rest.dto.ResponseError;
import mp.br.quarkussocial.domain.rest.dto.UserRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.Validator;
import java.util.Set;

@Path("")
public class UserResource {

    public static final String USERS = "users";
    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Transactional
    @Path(USERS)
    public Response createUser(UserRequest request) {

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(request);

        if(!violations.isEmpty()){

            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

            /*
            ConstraintViolation<UserRequest> erro = violations.stream().findAny().get(); // Pegar a mensagem de erro
            String errorMessage = erro.getMessage();

            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity(responseError).build();
            */

        }

        User user = new User();
        user.setNome(request.getNome());
        user.setIdade(request.getIdade());

        repository.persist(user);

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
//        return Response.ok(user).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path(USERS)
    public Response listAllUsers() {

        PanacheQuery<User> query = repository.findAll();

        return Response.ok(query.list()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path(USERS + "/{id}")
    public Response listEspecif(@PathParam("id") Long id) {

        User user = repository.findById(id);

        return Response.ok(user).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path(USERS + "/{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {

        User user = repository.findById(id);

        if (user != null) {
            repository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Path(USERS + "/{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserRequest request) {

        User user = repository.findById(id);

        if (user != null) {
            user.setNome(request.getNome());
            user.setIdade(request.getIdade());

            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


}
