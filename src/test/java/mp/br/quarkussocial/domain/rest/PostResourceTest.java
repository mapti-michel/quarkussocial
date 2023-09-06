package mp.br.quarkussocial.domain.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mp.br.quarkussocial.domain.model.Follower;
import mp.br.quarkussocial.domain.model.Post;
import mp.br.quarkussocial.domain.model.User;
import mp.br.quarkussocial.domain.repository.FollowerRepository;
import mp.br.quarkussocial.domain.repository.PostRepository;
import mp.br.quarkussocial.domain.repository.UserRepository;
import mp.br.quarkussocial.domain.rest.dto.CreatePostRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestHTTPEndpoint(PostResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;
    @Inject
    PostRepository postRepository;

    Long userId;
    Long userNotFollowerId;
    Long userFollowerId;

    @BeforeEach// será executado antes dos calls executados
    @Transactional // Sem ele, as operações persist, update e delete não funcionará
    public void setUP(){
        //Usuário padrão de testes
        var user = new User();
        user.setNome("Fulano");
        user.setIdade(30);
        userRepository.persist(user);
        userId = user.getId();

        //Criada a postagem para o usuário
        Post post = new Post();
        post.setText("Hello");
        post.setUser(user);
        postRepository.persist(post);


        //Usuário que não segue ninguém
        var userNotFollower = new User();
        userNotFollower.setNome("Sitrano");
        userNotFollower.setIdade(33);
        userRepository.persist(userNotFollower);
        userNotFollowerId = userNotFollower.getId();

        //Usuário seguidor
        var userFollower = new User();
        userFollower.setNome("Sitrano");
        userFollower.setIdade(33);
        userRepository.persist(userFollower);
        userFollowerId = userFollower.getId();

        Follower follower = new Follower();
        follower.setUser(user);
        follower.setFollower(userFollower);
        followerRepository.persist(follower);


    }

    @Test
    @DisplayName("Should create a post for a user")
    @Order(1)
    public void createPostTest(){

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some Text");

        var userId = 1;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", userId)
        .when()
                .post()
        .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Should return 404 when trying to make a post for a inexistent user")
    @Order(2)
    public void postForAnInexistentUserTest(){

        var postRequest = new CreatePostRequest();
        postRequest.setText("Some Text");

        var inexistenUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(postRequest)
                .pathParam("userId", inexistenUserId)
        .when()
                .post()
        .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 404 when user doesn't exist")
    @Order(3)
    public void listPostUserNotFoundTest(){
        var inexistentUserId = 999;

        given()
                .pathParam("userId", inexistentUserId)
        .when()
                .get()
        .then()
                .statusCode(404);

    }

    @Test
    @DisplayName("Should return 400 when followerId header is not present")
    @Order(4)
    public void listPostFollowerHeaderNotSendTest(){
        given()
                .pathParam("userId", userId)
        .when()
                .get()
        .then()
                .statusCode(400)
                .body(Matchers.is("You forget the header followerId"));
    }

    @Test
    @DisplayName("Should return 400 when followerId doesn't exist")
    @Order(5)
    public void listPostFollowerNotFoundTest(){

    var inexistentFollowerId = 999;

        given()
                .pathParam("userId", userId)
                .header("followerId", inexistentFollowerId)
        .when()
                .get()
        .then()
                .statusCode(400)
                .body(Matchers.is("Inexistent followerId"));

    }

    @Test
    @DisplayName("Should return 403 when followerId isn't a follower")
    @Order(6)
    public void listPostNotAFollower(){
        given()
                .pathParam("userId", userId)
                .header("followerId", userNotFollowerId)
        .when()
                .get()
        .then()
                .statusCode(403)
                .body(Matchers.is("You can't see these posts"));

    }

    @Test
    @DisplayName("Should return posts")
    @Order(7)
    public void listPostsTest(){
        given()
                .pathParam("userId", userId)
                .header("followerId", userFollowerId)
        .when()
                .get()
        .then()
                .statusCode(200)
                .body("size()", Matchers.is(0));

    }



}