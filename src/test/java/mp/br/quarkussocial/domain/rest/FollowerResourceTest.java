package mp.br.quarkussocial.domain.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mp.br.quarkussocial.domain.model.Follower;
import mp.br.quarkussocial.domain.model.User;
import mp.br.quarkussocial.domain.repository.FollowerRepository;
import mp.br.quarkussocial.domain.repository.UserRepository;
import mp.br.quarkussocial.domain.rest.dto.FollowerRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestHTTPEndpoint(FollowerResource.class)
class FollowerResourceTest {

    @Inject
    UserRepository userRepository;

    @Inject
    FollowerRepository followerRepository;

    Long userId;
    Long followerId;

    @BeforeEach
    @Transactional
    void setUP(){
        //Usuário padrão de testes
        var user = new User();
        user.setNome("Fulano");
        user.setIdade(30);
        userRepository.persist(user);
        userId = user.getId();

        //Seguidor
        var follower = new User();
        follower.setNome("Sitrano");
        follower.setIdade(31);
        userRepository.persist(follower);
        followerId = follower.getId();

        // Criar um follower
        var followerEntity = new Follower();
        followerEntity.setFollower(follower);
        followerEntity.setUser(user);

        followerRepository.persist(followerEntity);

    }

    @Test
    @DisplayName("Should return 409 when follower Id is equal to UserId")
    public void sameUserAsFollowerTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);


        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(Matchers.is("You can't follow yourself"));
    }

    @Test
    @DisplayName("Should return 404 on follow a user when userId doesn't exist")
    public void UserNotFoundWhenTryingToFollowTest(){

        var body = new FollowerRequest();
        body.setFollowerId(userId);

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", inexistentUserId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should follow a user")
    public void followUserTest(){

        var body = new FollowerRequest();
        body.setFollowerId(followerId);

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .pathParam("userId", userId)
        .when()
                .put()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @DisplayName("Should return 404 on list user follower and userId doesn't exist")
    public void UserNotFoundWhenListingFollowersTest(){

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
        .when()
                .get()
        .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should list a users followers")
    public void listFollowersTest(){

        var response =

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
        .when()
                .get()
        .then()
                .extract()
                .response();

        var followersCount = response.jsonPath().get("followersCount");
        var followersContent = response.jsonPath().getList("content");

        assertEquals(Response.Status.OK.getStatusCode(), response.statusCode());
        assertEquals(1, followersCount);
        assertEquals(1, followersContent.size());
    }

    @Test
    @DisplayName("Should return 404 on unfollow user and userId doesn't exist")
    public void UserNotFoundWhenUnfollowingAUsersTest(){

        var inexistentUserId = 999;

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", inexistentUserId)
                .queryParam("followerId", followerId)
                .when()
                .delete()
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    @DisplayName("Should Unfollow an user")
    public void unfollowUserTest(){

        given()
                .contentType(ContentType.JSON)
                .pathParam("userId", userId)
                .queryParam("followerId", followerId)
        .when()
                .delete()
        .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}