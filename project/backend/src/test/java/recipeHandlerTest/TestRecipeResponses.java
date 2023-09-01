package recipeHandlerTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import responses.FailureResponse;
import responses.MapSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestRecipeResponses {


    /** After each test, clear the results map in MapRecord. */
    @AfterEach
    public void clearResults() {
        MapSerializer.MapRecord.results.clear();
    }

    /**
     * Test the Weather response for when a valid lon, lat point is given by the user, where a Json
     * string containing the result and the temperature would be returned. Checks that the results Map
     * is being serialized correctly.
     */
    @Test
    public void recipeValidPoint() {
        MapSerializer.MapRecord.results.put("result", "success");
        List<Map<String,Object>> recipes = new ArrayList<>();
        Map<String, Object> recipe1details = new HashMap<>();
        recipe1details.put("id",654959.0);
        recipe1details.put("title", "Pasta With Tuna");
        recipe1details.put("image","https://spoonacular.com/recipeImages/654959-312x231.jpg");
        recipe1details.put("imageType", "jpg");
        Map<String, Object> recipe2details = new HashMap<>();
        recipe2details.put("id",511728.0);
        recipe2details.put("title", "Pasta Margherita");
        recipe2details.put("image","https://spoonacular.com/recipeImages/511728-312x231.jpg");
        recipe2details.put("imageType", "jpg");
        recipes.add(recipe1details);
        recipes.add(recipe2details);
        MapSerializer.MapRecord.results.put("recipes", recipes);
        assertEquals(2, MapSerializer.MapRecord.results.size());
        String result = "{\"result\":\"success\",\"recipes\":[{\"id\":\"654959.0\",\"title\":\"Pasta With Tuna\",\"image\":\"https://spoonacular.com/recipeImages/654959-312x231.jpg\",\"imageType\":\"jpg\"},{\"id\":511728.0,\"title\":\"Pasta Margherita\",\"image\":\"https://spoonacular.com/recipeImages/511728-312x231.jpg\",\"imageType\":\"jpg\"}";
        assertTrue(MapSerializer.MapRecord.serialize().contains("Pasta With Tuna"));
        assertTrue(MapSerializer.MapRecord.serialize().contains("https://spoonacular.com/recipeImages/654959-312x231.jpg"));
        assertTrue(MapSerializer.MapRecord.serialize().contains("jpg"));
        assertTrue(MapSerializer.MapRecord.serialize().contains("Pasta Margherita"));
        assertTrue(MapSerializer.MapRecord.serialize().contains("https://spoonacular.com/recipeImages/511728-312x231.jpg"));
        assertTrue(MapSerializer.MapRecord.serialize().contains(String.valueOf(654959.0)));
        assertTrue(MapSerializer.MapRecord.serialize().contains(String.valueOf(511728.0)));

//        assertEquals(result, MapSerializer.MapRecord.serialize());
    }

    /**
     * Test the Recipe response for when a query parameter is given incorrectly by the user. The user
     * would be met with an error_bad_request. Checks that the FailureRecord containing this result:
     * error is being serialized correctly.
     */
    @Test
    public void recipeWrongParams() {
        String output = "{\"result\":\"error_bad_request\"}";
        assertEquals(output, FailureResponse.FailureRecord.serialize("error_bad_request"));
    }

    /**
     * Test the Recipe response for when an recipeID, is given by the user. The user
     * would be met with an error_datasource. Checks that the FailureRecord containing this result:
     * error is being serialized correctly.
     */
    @Test
    public void recipeInvalidPoint() {
        String output = "{\"result\":\"error_datasource\"}";
        assertEquals(output, FailureResponse.FailureRecord.serialize("error_datasource"));
    }
}
