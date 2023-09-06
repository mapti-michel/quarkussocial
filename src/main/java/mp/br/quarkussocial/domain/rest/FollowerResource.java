package mp.br.quarkussocial.domain.rest;


import mp.br.quarkussocial.domain.model.Follower;
import mp.br.quarkussocial.domain.repository.FollowerRepository;
import mp.br.quarkussocial.domain.repository.UserRepository;
import mp.br.quarkussocial.domain.rest.dto.FollowerPerUserResponse;
import mp.br.quarkussocial.domain.rest.dto.FollowerRequest;
import mp.br.quarkussocial.domain.rest.dto.FollowerResponse;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResource {

    private FollowerRepository repository;
    private UserRepository userRepository;

    public FollowerResource(FollowerRepository repository, UserRepository userRepository){

        this.repository = repository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followerUser(@PathParam("userId") Long userId, FollowerRequest request){

        if(userId.equals(request.getFollowerId())){ // Verifica se o usuário é igual ao mesmo.
            return Response
                    .status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself")
                    .build();
        }

        var user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var follower = userRepository.findById(request.getFollowerId());
        boolean follow = repository.followers(follower, user);

        if(!follow){
            var entity = new Follower();
            entity.setUser(user);
            entity.setFollower(follower);

            repository.persist(entity);
        }
        return Response.status(Response.Status.NO_CONTENT).build();

    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        var user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = repository.findByUser(userId);
        FollowerPerUserResponse responseObject = new FollowerPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followerList =  list.stream().map( FollowerResponse::new).collect(Collectors.toList());

        responseObject.setContent(followerList);

        return Response.ok(responseObject).build();
    }

    @DELETE
    @Transactional
    public Response unFollowUser(
            @PathParam("userId") Long userId,
            @QueryParam("followerId") Long followerId){
        var user = userRepository.findById(userId);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).entity(followerId).build();
        }

        repository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
