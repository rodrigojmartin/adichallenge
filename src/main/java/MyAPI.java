import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.json.simple.JSONObject;

import static io.restassured.RestAssured.given;

public class MyAPI {

    private static RequestSpecification myAPIRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:3001/")
                .build();
    }

    private static ResponseSpecification myAPIValidResponseSpec() {
        return new ResponseSpecBuilder().
                expectStatusCode(200).
                build();
    }

    public static ValidatableResponse getAllProducts() {
        ValidatableResponse response =
                given().
                        spec(myAPIRequestSpec()).
                        when().
                        get("/product").
                        then().
                        spec(myAPIValidResponseSpec()).
                        log()
                        .all();
        return response;
    }

    public static ValidatableResponse postProduct(JSONObject json) {
        ValidatableResponse response =
                given().
                        spec(myAPIRequestSpec()).
                        contentType("application/json").
                        body(json.toString()).
                        when().
                        post("/product").
                        then().
                        spec(myAPIValidResponseSpec()).
                        log()
                        .all();
        return response;
    }

    public static ValidatableResponse updateProduct(String productId, JSONObject payload) {
        ValidatableResponse response =
                given().
                        spec(myAPIRequestSpec()).
                        contentType("application/json").
                        body(payload.toString()).
                        when().
                        put("/product/" + productId).
                        then().
                        spec(myAPIValidResponseSpec()).
                        log()
                        .all();
        return response;
    }

    public static ValidatableResponse deleteProduct(String productId) {
        ValidatableResponse response =
                given().
                        spec(myAPIRequestSpec()).
                        contentType("application/json").
                        when().
                        delete("/product/" + productId).
                        then().
                        spec(myAPIValidResponseSpec()).
                        log()
                        .all();
        return response;
    }
}
