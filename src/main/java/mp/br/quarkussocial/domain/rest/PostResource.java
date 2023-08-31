package mp.br.quarkussocial.domain.rest;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import lombok.extern.slf4j.Slf4j;
import mp.br.quarkussocial.domain.model.Post;
import mp.br.quarkussocial.domain.model.User;
import mp.br.quarkussocial.domain.repository.PostRepository;
import mp.br.quarkussocial.domain.repository.UserRepository;
import mp.br.quarkussocial.domain.rest.dto.CreatePostRequest;
import mp.br.quarkussocial.domain.rest.dto.PostResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Slf4j
@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository repository;
    private PostRepository postRepository;

    @Inject
    public PostResource(UserRepository repository, PostRepository postRepository) {
        this.repository = repository;
        this.postRepository = postRepository;
    }


    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long id, CreatePostRequest request) {
        User user = repository.findById(id);

        if (user == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

        Post post = new Post();
        post.setText(request.getText());
        post.setUser(user);

        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    @Produces(MediaType.APPLICATION_JSON + "; charset=UTF-8")
    public Response listPosts(@PathParam("userId") Long userId) {
        User user = repository.findById(userId);

        if (user == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }
        var query = postRepository.find("user", Sort.by("dateTime", Sort.Direction.Descending), user);

        var lista = query.list();

        var postResponseList = lista
                .stream()
//                .map(post -> PostResponse.fromEntity(post))
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        return Response.ok(postResponseList).build();
        //return Response.ok(lista).build();

    }

}
