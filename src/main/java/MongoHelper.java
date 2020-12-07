import org.json.simple.JSONObject;
import com.mongodb.*;
import com.mongodb.util.JSON;

public class MongoHelper {

    public static DBCollection getDbCollection() {
        MongoClient mongoClient = new MongoClient();
        DB database = mongoClient.getDB("inversify-express-example");
        return database.getCollection("product");
    }

    public static void initializeDatabase() {
        //Create a Mongo Connection for Test Data
        DBCollection collection = getDbCollection();
        //Initialize remove all objects
        collection.remove(new BasicDBObject());
    }

    public static void insertProducts(String productData) {
        //Create a Mongo Connection for Test Data
        DBCollection collection = getDbCollection();
        JSONObject json = JsonHelper.parseJson(productData);
        DBObject obj = (DBObject) JSON.parse(json.toJSONString());
        collection.insert(obj);
    }

    public static void closeConnection() {
    }
}
