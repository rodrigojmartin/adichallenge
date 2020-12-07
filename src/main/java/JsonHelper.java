import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JsonHelper {

    public static JSONObject parseJson(String fileName) {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        Object obj;
        obj = null;
        try (FileReader reader = new FileReader(fileName)) {
            //Read JSON file
            obj = jsonParser.parse(reader);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject((Map) obj);

    }
}
