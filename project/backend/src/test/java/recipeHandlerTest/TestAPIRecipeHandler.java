package recipeHandlerTest;

import com.squareup.moshi.Moshi;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recipe.RecipeHandler;
import responses.FailureResponse;
import responses.MapSerializer.MapRecord;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class TestAPIRecipeHandler {
    private RecipeHandler recipeHandler;

    public TestAPIRecipeHandler() {
        this.recipeHandler = new RecipeHandler();

    }
    /** Before any tests run, set up the Spark port and set Logger level. */
    @BeforeAll
    public static void setupBeforeEverything() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING);
    }

    /** Before each test runs, restart Spark server for recipe endpoint. */
    @BeforeEach
    public void setup() {
        Spark.get("/recipe", new RecipeHandler());
        Spark.init();
        Spark.awaitInitialization();
    }

    /** After each test runs, gracefully stop Spark listening on both endpoints. */
    @AfterEach
    public void teardown() {
        Spark.unmap("/recipe");
        Spark.awaitStop();
    }

    /**
     * Helper method to start a connection to a specific API endpoint/params
     *
     * @param apiCall the call string, including endpoint
     * @return the connection for the given URL, just after connecting
     * @throws IOException if the connection fails
     */
    private static HttpURLConnection tryRequest(String apiCall) throws IOException {
        URL requestURL = new URL("http://localhost:" + Spark.port() + "/" + apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();
        clientConnection.connect();
        return clientConnection;
    }
    /**
     * Test for when no query parameters are given (recipeID). In this case, the user is met with am
     * error_bad_request.
     *
     * @throws IOException if the connection fails
     */
    @Test
    public void testAPINoParamsGiven() throws IOException {
        HttpURLConnection clientConnection = tryRequest("recipe");
        assertEquals(200, clientConnection.getResponseCode());
        Moshi moshi = new Moshi.Builder().build();
        FailureResponse.FailureRecord response =
            moshi
                .adapter(FailureResponse.FailureRecord.class)
                .fromJson((new Buffer().readFrom(clientConnection.getInputStream())));
        // if the above runs, then the failure record worked
        assertEquals("error_bad_request", response.errorMessageForTest);
        clientConnection.disconnect();
    }

    /**
     * This test ensures that when the user enters invalid numbers, they are met with an error_datasource as expected.
     * @throws IOException
     */
    @Test
    public void testAPIInvalidRecipeID() throws IOException {
        HttpURLConnection clientConnection = tryRequest("recipe?recipeID=0");
        assertEquals(200, clientConnection.getResponseCode());
        Moshi moshi = new Moshi.Builder().build();
        FailureResponse.FailureRecord response =
            moshi
                .adapter(FailureResponse.FailureRecord.class)
                .fromJson((new Buffer().readFrom(clientConnection.getInputStream())));
        // if the above runs, then the failure record worked
        assertEquals("error_datasource", response.errorMessageForTest);
        clientConnection.disconnect();
        //TODO: Only takes in numbers, need to account for when the user enters Strings or other charactes
    }


    /**
     * Testing the results of using a valid recipeID. Here we expect the user to get the results expected in the test.
     * @throws IOException
     */
    @Test
    public void testAPIValidRecipeID() throws IOException {
        HttpURLConnection clientConnection = tryRequest("recipe?recipeID=654959.0");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(2, MapRecord.results.size());
        assertEquals("success", MapRecord.results.get("result"));
        assertEquals("http://www.foodista.com/recipe/K6QWSKQM/pasta-with-tuna", ((Map<String, Object>) MapRecord.results.get("recipes")).get("sourceUrl"));
        assertEquals(45.0, ((Map<String, Object>) MapRecord.results.get("recipes")).get("readyInMinutes"));
        assertEquals(168.12, ((Map<String, Object>) MapRecord.results.get("recipes")).get("pricePerServing"));
        assertEquals(654959.0, ((Map<String, Object>) MapRecord.results.get("recipes")).get("id"));
        assertEquals("Pasta With Tuna",((Map<String, Object>) MapRecord.results.get("recipes")).get("title"));
    }

    /**
     * Test valid recipeID2 to ensure program doesn't crush on smaller numbers.
     * @throws IOException
     */
    @Test
    public void testAPIValidRecipeID2() throws IOException {
        HttpURLConnection clientConnection = tryRequest("recipe?recipeID=1");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(2, MapRecord.results.size());
        assertEquals("success", MapRecord.results.get("result"));
        assertEquals("http://latavolamarcherecipebox.blogspot.com/2009/10/fried-anchovies-with-sage.html", ((Map<String, Object>) MapRecord.results.get("recipes")).get("sourceUrl"));
        assertEquals(45.0, ((Map<String, Object>) MapRecord.results.get("recipes")).get("readyInMinutes"));
        assertEquals(560.51, ((Map<String, Object>) MapRecord.results.get("recipes")).get("pricePerServing"));
        assertEquals(1.0, ((Map<String, Object>) MapRecord.results.get("recipes")).get("id"));
        assertEquals("Fried Anchovies with Sage",((Map<String, Object>) MapRecord.results.get("recipes")).get("title"));
    }

    /**
     * Fuzz testing the contents of the results in the result map. only runs once.
     * @throws IOException
     */
    @Test
    public void fuzzTestRecipeHandler() throws IOException{
        double randomRecipeIDs = ThreadLocalRandom.current().nextDouble(1, 1000000);
        String recipeIDs = Double.toString(randomRecipeIDs);
        HttpURLConnection clientConnection = tryRequest("recipe?recipeID=" + recipeIDs);
        System.out.println("recipe?recipeID=" + recipeIDs);
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(2, MapRecord.results.size());
        assertTrue(((Map<String, Object>) MapRecord.results.get("recipes")).containsKey("sourceUrl"));
        assertTrue(((Map<String, Object>) MapRecord.results.get("recipes")).containsKey("readyInMinutes"));
        assertTrue(((Map<String, Object>) MapRecord.results.get("recipes")).containsKey("pricePerServing"));
        assertTrue(((Map<String, Object>) MapRecord.results.get("recipes")).containsKey("id"));
        assertTrue(((Map<String, Object>) MapRecord.results.get("recipes")).containsKey("title"));
    }

    /**
     * Generating randomIDs within the valid boundary
     * @return - list of random recipeIDs
     */
    private List<Double> generateRandomRecipeIDs() {
        final ThreadLocalRandom r = ThreadLocalRandom.current();
        Double recipeID = r.nextDouble(1, 999999);
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(recipeID);
        return coordinates;
    }

    //
//    /**
//     * Fuzz testing recipeIDs given by the user to ensure program does not crash.
//     */
//    @Test
//    public void fuzzTestingBounds() {
//        int NUM_TRIALS = 100;
//        for (int counter = 0; counter < NUM_TRIALS; counter++) {
//            List<Double> recipeIDs = this.generateRandomRecipeIDs();
//            Double recipeID = recipeIDs.get(0);
//            assertDoesNotThrow(
//                () -> this.recipeHandler.handleRecipeDetails(recipeID));
//        }
//    }
}
