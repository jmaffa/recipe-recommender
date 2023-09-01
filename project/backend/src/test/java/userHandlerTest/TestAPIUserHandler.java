package userHandlerTest;

import com.squareup.moshi.Moshi;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okio.Buffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import recipe.UserHandler;
import responses.FailureResponse;
import responses.MapSerializer;
import responses.MapSerializer.MapRecord;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class runs tests on all API end points handled by the UserHandler class
 * DISCLAIMER! Each test is expensive, and the free version of Spoonacular cannot support the entire test suite.
 * As such, on the free account, you need approx 5 API keys to run the entire test suite.
 * When you encounter a 500 server error while running the test, it probably means that the API
 * key has reached its request limit for the day. When that happens, replace the key in the API Key
 * class with a new key and re-run the test. All tests will pass.
 */

public class TestAPIUserHandler {
    /** Before any tests run, set up the Spark port and set Logger level. */
    @BeforeAll
    public static void setupBeforeEverything() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING);
    }

    /** Before each test runs, restart Spark server for recipeIngredinets endpoint. */
    @BeforeEach
    public void setup() {
        Spark.get("/recipeIngredients", new UserHandler());
        Spark.init();
        Spark.awaitInitialization();
    }

    /** After each test runs, gracefully stop Spark listening on both endpoints. */
    @AfterEach
    public void teardown() {
        Spark.unmap("/recipeIngredients");
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
     * Test for when no query parameters are given (userID). In this case, the user is met with am
     * error_bad_request.
     *
     * @throws IOException if the connection fails
     */
    @Test
    public void testAPINoParamsGiven() throws IOException {
        HttpURLConnection clientConnection = tryRequest("recipeIngredients");
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

//    /**
//     * When the user enters a non-existent user ID, we expect them to be met with an error_datasource error message
//     * @throws IOException
//     */
//    @Test
//    public void testAPIInvalidUserID() throws IOException {
//        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=noUserhahahah");
//        assertEquals(200, clientConnection.getResponseCode());
//        Moshi moshi = new Moshi.Builder().build();
//        FailureResponse.FailureRecord response =
//            moshi
//                .adapter(FailureResponse.FailureRecord.class)
//                .fromJson((new Buffer().readFrom(clientConnection.getInputStream())));
//        // if the above runs, then the failure record worked
//        assertEquals("User ID not found", response.errorMessageForTest);
//        clientConnection.disconnect();
//    }

    /**
     * Testing the results of using a valid userID. Here we expect the user to get the results expected in the test.
     * @throws IOException
     */
    @Test
    public void testAPIValidUserID() throws IOException {
        // test result generation
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=user0");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(3, MapSerializer.MapRecord.results.size());
        // test recipe generation
        assertEquals(10, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        assertEquals(45.0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("readyInMinutes"));
        assertEquals("http://www.afrolems.com/2014/06/26/stir-fried-quinoa-brown-rice-and-chicken-breast/", ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("sourceUrl"));
        assertEquals(45.0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("readyInMinutes"));
        List<String> missedList = new ArrayList<>();
        missedList.add("Spice Rub");
        missedList.add("butter");
        missedList.add("chicken breast");
        missedList.add("garlic");
        missedList.add("scotch bonnet pepper");
        missedList.add("spring onion");
        assertTrue(((List<String>)((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("missedIngredients")).containsAll(missedList));
        assertEquals(false, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("vegetarian"));
        assertEquals(true, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("glutenFree"));
        assertEquals(false, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("vegan"));
        assertEquals(365.25, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("pricePerServing"));
        assertEquals(716361.0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("id"));
        assertEquals("Stir Fried Quinoa, Brown Rice and Chicken Breast", ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("title"));
        List<String> dishType = new ArrayList<>();
        dishType.add("lunch");
        dishType.add("main course");
        dishType.add("main dish");
        dishType.add("dinner");
        assertTrue(((List<String>)((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).get(0).get("dishTypes")).containsAll(dishType));
        // test ingredient retrieval from the database
        assertEquals("tomato,carrot,peas,pasta,rice", MapSerializer.MapRecord.results.get("ingredients"));
    }

    /**
     * Tests the UserHandler with a randomly generated valid username
     */
    @Test
    public void fuzzTestUserHandler() throws IOException{
        int min = 0;
        int max = 19;
        //Generate random int value from 0 to 19
        int randomInt = (int)Math.floor(Math.random()*(max-min+1)+min);
        String randomUserID = "user" + randomInt;
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=" + randomUserID);
        System.out.println("recipeIngredients?userID=" + randomUserID);
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(3, MapSerializer.MapRecord.results.size());
        assertEquals(10, ((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).size());
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("readyInMinutes"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("pricePerServing"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("id"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("title"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("vegetarian"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("vegan"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("glutenFree"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("dishTypes"));
        assertTrue(((List<Map<String, Object>>) MapRecord.results.get("recipes")).get(0).containsKey("missedIngredients"));
    }

    /**
     * Tests UserHandler with the MaxPrice filter
     */
    @Test
    public void testMaxPrice() throws IOException {
        // test result generation
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=user0");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(3, MapSerializer.MapRecord.results.size());
        // test recipe list generation without filter
        assertEquals(10, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (maxPrice = 100)
        HttpURLConnection clientConnection1 = tryRequest("recipeIngredients?userID=user0&maxPrice=100");
        assertEquals(200, clientConnection1.getResponseCode());
        assertEquals(3, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (maxPrice = 60)
        HttpURLConnection clientConnection2 = tryRequest("recipeIngredients?userID=user0&maxPrice=60");
        assertEquals(200, clientConnection2.getResponseCode());
        assertEquals(2, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
    }

    /**
     * Tests UserHandler with the MaxTime filter
     */
    @Test
    public void testMaxTime() throws IOException {
        // test result generation
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=user0");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(3, MapSerializer.MapRecord.results.size());
        // test recipe list generation without filter
        assertEquals(10, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (maxTime = 60)
        HttpURLConnection clientConnection1 = tryRequest("recipeIngredients?userID=user0&maxTime=60");
        assertEquals(200, clientConnection1.getResponseCode());
        assertEquals(9, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (maxTime = 30)
        HttpURLConnection clientConnection2 = tryRequest("recipeIngredients?userID=user0&maxTime=30");
        assertEquals(200, clientConnection2.getResponseCode());
        assertEquals(0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
    }

    /**
     * Tests UserHandler with diet filter
     */
    @Test
    public void testDiet() throws IOException {
        // test result generation
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=user0");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(3, MapSerializer.MapRecord.results.size());
        // test recipe list generation without filter
        assertEquals(10, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (diet = glutenFree)
        HttpURLConnection clientConnection1 = tryRequest("recipeIngredients?userID=user0&diet=glutenFree");
        assertEquals(200, clientConnection1.getResponseCode());
        assertEquals(3, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (diet = vegan)
        HttpURLConnection clientConnection2 = tryRequest("recipeIngredients?userID=user0&diet=vegan");
        assertEquals(200, clientConnection2.getResponseCode());
        assertEquals(0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (diet = vegetarian)
        HttpURLConnection clientConnection3 = tryRequest("recipeIngredients?userID=user0&diet=vegetarian");
        assertEquals(200, clientConnection3.getResponseCode());
        assertEquals(1, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
    }

    /**
     * Tests UserHandler with dishTypes filter
     */
    @Test
    public void testDishTypes() throws IOException {
        // test recipe list generation with filter (dishTypes = breakfast)
        HttpURLConnection clientConnection1 = tryRequest("recipeIngredients?userID=user0&dishTypes=breakfast");
        assertEquals(200, clientConnection1.getResponseCode());
        assertEquals(0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (dishTypes = lunch)
        HttpURLConnection clientConnection2 = tryRequest("recipeIngredients?userID=user0&dishTypes=lunch");
        assertEquals(200, clientConnection2.getResponseCode());
        assertEquals(6, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (dishTypes = dinner)
        HttpURLConnection clientConnection3 = tryRequest("recipeIngredients?userID=user0&dishTypes=dinner");
        assertEquals(200, clientConnection3.getResponseCode());
        assertEquals(6, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (dishTypes = dessert)
        HttpURLConnection clientConnection4 = tryRequest("recipeIngredients?userID=user0&dishTypes=dessert");
        assertEquals(200, clientConnection4.getResponseCode());
        assertEquals(0, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (dishTypes = sideDish)
        HttpURLConnection clientConnection5 = tryRequest("recipeIngredients?userID=user0&dishTypes=sideDish");
        assertEquals(200, clientConnection5.getResponseCode());
        assertEquals(3, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
    }

    /**
     * Tests UserHandler with dishType and maxPrice filter
     */
    @Test
    public void testDishTypeAndMaxPrice() throws IOException {
        // test recipe list generation with filter (dishTypes = lunch)
        HttpURLConnection clientConnection2 = tryRequest("recipeIngredients?userID=user0&dishTypes=lunch");
        assertEquals(200, clientConnection2.getResponseCode());
        assertEquals(6, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (maxPrice = 100)
        HttpURLConnection clientConnection1 = tryRequest("recipeIngredients?userID=user0&maxPrice=100");
        assertEquals(200, clientConnection1.getResponseCode());
        assertEquals(3, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        // test recipe list generation with filter (maxPrice = 100)
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=user0&maxPrice=100&dishTypes=lunch");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(1, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
    }

    /**
     * Tests UserHandler with dishType and maxPrice filter
     */
    @Test
    public void testSubstitution() throws IOException {
        HttpURLConnection clientConnection = tryRequest("recipeIngredients?userID=nadyatan21");
        assertEquals(200, clientConnection.getResponseCode());
        assertEquals(10, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        assertEquals("sugar,butter,flour", MapSerializer.MapRecord.results.get("ingredients"));

        // test recipe list generation with substitutions
        HttpURLConnection clientConnection1 = tryRequest("recipeIngredients?userID=nadyatan21&sub=y");
        assertEquals(200, clientConnection1.getResponseCode());
        assertEquals(10, ((List<Map<String, Object>>) MapSerializer.MapRecord.results.get("recipes")).size());
        assertEquals("sugar,butter,flour,margarine,shortening,lard", MapSerializer.MapRecord.results.get("ingredients") );

        Map<String, List<String>> subsMap = new HashMap<>();
        List<String> subList = new ArrayList<>();
        subList.add("margarine");
        subList.add("shortening");
        subList.add("lard");
        subsMap.put("butter", subList);
        assertEquals(subsMap,MapSerializer.MapRecord.results.get("substitutes"));
    }

}
