package mp.br.quarkussocial.domain.rest;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import mp.br.quarkussocial.domain.rest.dto.ResponseError;
import mp.br.quarkussocial.domain.rest.dto.UserRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.net.URL;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserResourceTest {

    public static final String ENDPOINT = "/users";

    @TestHTTPResource("/users")
    URL apiURL;

    @Test
    @DisplayName("Should create an user successfull")
    @Order(1)
    public void createUserTest(){
        var user = new UserRequest();
        user.setNome("Fulano");
        user.setIdade(30);

        //Dado a informação
        var response =
            given()
                    .contentType(ContentType.JSON)
                    .body(user)
            .when() //quando
                    .post(ENDPOINT)
                    //Então
            .then()
                    .extract()
                    .response();

        assertEquals(201, response.statusCode());
        assertNotNull(response.jsonPath().getString("id"));
    }

    @Test
    @DisplayName("should return when json is not valid")
    @Order(2)
    public void createUserValidationErrorTest(){

        var user = new UserRequest();

        user.setNome(null);
        user.setIdade(null);

        var response =
                given()
                        .contentType(ContentType.JSON)
                        .body(user)
                .when()
                        .post(ENDPOINT)
                .then()
                        .extract().response();

        assertEquals(ResponseError.UNPROCESSABLE_ENTITY_STATUS, response.statusCode());
        assertEquals("Validation Error", response.jsonPath().getString("message"));

        List<Map<String, String>> errors = response.jsonPath().getList("errors");

        assertNotNull(errors.get(0).get("message"));
        assertNotNull(errors.get(1).get("message"));

/*
        assertEquals("Name is required", errors.get(0).get("message"));
        assertEquals("Idade is required", errors.get(1).get("message"));
*/

    }


    @Test
    @DisplayName("Should list all users test")
    @Order(3)
    public void listAllUsersTest(){
        given()
                .contentType(ContentType.JSON)
        .when()
                .get(ENDPOINT)
        .then()
                .statusCode(200)
                .body("size()", Matchers.is(36));
    }


}