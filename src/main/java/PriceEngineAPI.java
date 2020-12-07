import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class PriceEngineAPI {

    private static RequestSpecification priceEngineRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("http://localhost:3000/")
                .setBasePath("/product/{id}")
                .build();
    }

    public static ValidatableResponse getPrice(String productId) {
        ValidatableResponse response =
                given().
                        spec(priceEngineRequestSpec().
                                pathParam("id", productId)).
                        header("accept", "application/json").
                        header("accept-language", "en-us").
                        when().
                        get().
                        then().
                        log().all().
                        assertThat().
                        statusCode(200);
        return response;
    }

    public static ValidatableResponse getPriceWithNoHeaders(String productId) {
        ValidatableResponse response =
                given().
                        spec(priceEngineRequestSpec().
                                pathParam("id", productId)).
                        when().
                        get().
                        then().log().all();
        return response;
    }
}
