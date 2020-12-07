/**
 * Created by Rodrigo on 03/12/2020.
 */

import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.json.simple.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;


public class adidasTests {

    @BeforeClass
    public void setUp() {
        RestAssured.config = new RestAssuredConfig().encoderConfig(encoderConfig().defaultContentCharset("UTF-8"));
    }

    @BeforeMethod
    public void setUpTest() {
        //Clear all MongoDB records
        MongoHelper.initializeDatabase();
    }

    @Test
    public void testGetAllProductsWhenThereAreNoProducts() throws Exception {
        ValidatableResponse response = MyAPI.getAllProducts();
        response.assertThat().body("", Matchers.empty());
    }

    @Test
    public void testGetAllProductsWhenThereIsOneProduct() throws Exception {
        //Insert test data straight to MongoDB
        MongoHelper.insertProducts("product.json");
        ValidatableResponse response = MyAPI.getAllProducts();
        response.assertThat().body("currency", hasItem("$"));
        response.assertThat().body("price", hasItem(4));
        response.assertThat().body("id", hasItem("1"));
        response.assertThat().body("name", hasItem("The 1st product"));
        response.assertThat().body("description", hasItem("This is a cool product with a 1 index"));
    }

    @Test
    public void testGeneratePriceOfAProduct() throws Exception {
        //Insert test data straight to MongoDB
        MongoHelper.insertProducts("product.json");
        ValidatableResponse response = PriceEngineAPI.getPrice("1");
        response.assertThat().body("currency", equalTo("$"));
        response.assertThat().body("price", equalTo(4));
    }


    @Test
    public void testGetAProductWithNoHeaders() throws Exception {
        //Insert test data straight to MongoDB
        MongoHelper.insertProducts("product.json");
        ValidatableResponse response = PriceEngineAPI.getPriceWithNoHeaders("1");
        response.assertThat().statusCode(400);
        response.assertThat().body("html.body", equalTo("No Accept Language provided"));
    }


    @Test
    public void testGetANonExistantProduct() throws Exception {
        ValidatableResponse response = PriceEngineAPI.getPrice("9876543210");
        response.assertThat().body("html.body", equalTo("Product does not exist"));
    }
    //Bug: Calling the API with a non existant ID still brings up results. Making up an assertion to fix it


    @Test
    public void testCreateAProduct() throws Exception {
        JSONObject json = JsonHelper.parseJson("product.json");
        ValidatableResponse response = MyAPI.postProduct(json);
        response.assertThat().body("name", equalTo("The 1st product"));
        response.assertThat().body("description", equalTo("This is a cool product with a 1 index"));
        response.assertThat().body("id", equalTo("1"));
    }

    @Test
    public void testCreateAProductWithNullValues() throws Exception {
        JSONObject json = JsonHelper.parseJson("product_with_null_values.json");
        ValidatableResponse response = MyAPI.postProduct(json);
        response.assertThat().body("name", equalTo(null));
        response.assertThat().body("description", equalTo(null));
        response.assertThat().body("id", equalTo(null));
    }
    //Possible bug, shouldn't be allowed to create an id with a null value

    @Test
    public void testUpdateExistingProduct() throws Exception {
        MongoHelper.insertProducts("product.json");
        JSONObject json = JsonHelper.parseJson("product_update.json");
        ValidatableResponse response = MyAPI.updateProduct("1", json);
        response.assertThat().body("id", equalTo("1"));
        response.assertThat().body("name", equalTo("The 1st product updated"));
        response.assertThat().body("description", equalTo("This is a cool product with a 1 index updated"));
    }


    @Test
    public void testUpdateExistingProductWithNullValues() throws Exception {
        MongoHelper.insertProducts("product.json");
        JSONObject json = JsonHelper.parseJson("product_update_with_null_values.json");
        ValidatableResponse response = MyAPI.updateProduct("1", json);
        response.assertThat().body("body.html", equalTo("Id can't be updated to null"));
    }
    //Bug: Updating a product with a null id should not be allowed, making up an assertion for later fixes.

    @Test
    public void testUpdateNonExistingProduct() throws Exception {
        JSONObject json = JsonHelper.parseJson("product_update.json");
        ValidatableResponse response = MyAPI.updateProduct("2340923409", json);
        response.assertThat().body("html.body", equalTo("Product does not exist"));
    }
    //Bug: Updating a non existant product should return a non existing error message


    @Test
    public void testDeleteExistingProduct() throws Exception {
        MongoHelper.insertProducts("product.json");
        ValidatableResponse response = MyAPI.deleteProduct("1");
        response.assertThat().body("ok", equalTo(1));
    }


    @Test
    public void testDeleteNonExistingProduct() throws Exception {
        ValidatableResponse response = MyAPI.deleteProduct("99999");
        response.assertThat().body("ok", equalTo(0));
    }

}
