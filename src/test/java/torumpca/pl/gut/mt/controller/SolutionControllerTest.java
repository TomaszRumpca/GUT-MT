package torumpca.pl.gut.mt.controller;

import com.jayway.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;



class SolutionControllerTest {

    @BeforeAll
    static void setUp() {

        //TODO start the API

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        RestAssured.basePath = "/api/";
    }


//    @Test
//    void solve() {
//        AlgorithmInputData input = new AlgorithmInputData();
//        input.setOrigin(new Coordinates(48.802824d, 13.236774d));
//        input.setDestination(new Coordinates(50.952824, 14.886774));
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(input)
//                .when()
//                .post("/solve")
//                .then()
//                .statusCode(200)
//                .body("overallCost", equalTo(3));
//
//    }
}