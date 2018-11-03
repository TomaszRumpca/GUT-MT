package torumpca.pl.gut.mt.controller;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import torumpca.pl.gut.mt.model.Coordinates;
import torumpca.pl.gut.mt.model.UserData;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

class SolutionControllerTest {

    @BeforeAll
    static void setUp() {

        //TODO start the API

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/";
    }

    @Test
    void solve() {
        UserData input = new UserData();
        input.setOriginCoordinates(new Coordinates(48.802824d, 13.236774d));
        input.setGoalCoordinates(new Coordinates(50.952824, 14.886774));

        given()
                .contentType(ContentType.JSON)
                .body(input)
                .when()
                .post("/solve")
                .then()
                .statusCode(200)
                .body("overallCost", equalTo(3));

    }
}