package recipe;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import database.DatabaseReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import recommender.HandleDatabase;
import recommender.slopeOne.SlopeOne;
import responses.FailureResponse;
import server.Handler;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
//import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import responses.MapSerializer.MapRecord;
import weather.exceptions.BadJsonException;
import weather.exceptions.DatasourceException;

/**
 * RatingHandler class which implements the handler class and
 * handles the rating data matrix
 */
public class RatingHandler implements Handler {

  /**
   * Handles the API request and parses it into a readable JSON object
   * @param request - the API request
   * @param response - the response returned
   * @return - JSON object
   */
  @Override
  public Object handle(Request request, Response response) throws URISyntaxException, IOException, InterruptedException, DatasourceException,
      BadJsonException {
    QueryParamsMap qm = request.queryMap();
    String userID = qm.value("userID");
    DatabaseReader databaseReader = new DatabaseReader();
    if (request.queryParams().isEmpty()){
      return this.failureResponse("error_bad_request");
    }
    try {
      HandleDatabase handleDatabase = new HandleDatabase();
      SlopeOne.slopeOne(handleDatabase.getUserRatingMap(), handleDatabase.getRecipeSet());
      List<String> recipeRec = SlopeOne.getUserRecList(userID);
      List<Object> returnRecsList = new ArrayList<>();
      for (String recipes : recipeRec){
        Object resultMap = this.getRecipeData(this.handleRecipeDetails(Double.parseDouble(recipes)));
        returnRecsList.add(resultMap);
      }
      MapRecord.results.put("result", "success");
      MapRecord.results.put("recipes", returnRecsList);
      return this.successResponse();
    } catch (BadJsonException badJsonException){
      return this.failureResponse("error_bad_json");
    }
  }

  /**
   * Returns the relevant data of a recipe from the JSON returned by the API call
   * @param generalMap - the JSON returned by the API call that matches recipeID to recipe data
   * @return - map of relevant information about the recipe
   */
  private Map<String, Object> getRecipeData(Map<String, Object> generalMap)
      throws BadJsonException {
    Map<String, Object> myRecipeMap = new HashMap<>();
    myRecipeMap.put("id", generalMap.get("id"));
    myRecipeMap.put("title", generalMap.get("title"));
    myRecipeMap.put("sourceUrl", generalMap.get("sourceUrl"));
    myRecipeMap.put("vegetarian", generalMap.get("vegetarian"));
    myRecipeMap.put("glutenFree", generalMap.get("glutenFree"));
    myRecipeMap.put("vegan", generalMap.get("vegan"));
    myRecipeMap.put("readyInMinutes", generalMap.get("readyInMinutes"));
    myRecipeMap.put("dishTypes", generalMap.get("dishTypes"));
    myRecipeMap.put("pricePerServing", generalMap.get("pricePerServing"));
    return myRecipeMap;
  }


  /**
   * Makes the API call to retrieve recipe information based on the recipeID
   * @param recipeID - unique recipeID
   * @return - Map of recipge information
   */
  private Map handleRecipeDetails(double recipeID)
      throws URISyntaxException, InaccessibleObjectException, InterruptedException, DatasourceException,
      BadJsonException, IOException {
    APIKey key = new APIKey();
    HttpRequest getRecipeRequest = HttpRequest.newBuilder()
        .uri(new URI("https://api.spoonacular.com/recipes/" + (int)recipeID+ "/information?apiKey=" + key.getKey()+ "&includeNutrition=false"))
        .GET()
        .build();

    HttpResponse<String> recipeRequest = HttpClient.newBuilder()
        .build().
        send(getRecipeRequest, HttpResponse.BodyHandlers.ofString());

    if (recipeRequest.statusCode() == 400 || recipeRequest.statusCode() == 404 ||
        recipeRequest.statusCode() == 301) {
      throw new DatasourceException("API error");
    }
    try {
      return this.detailCreator(recipeRequest.body());
    }
    catch(Exception e){
      throw new BadJsonException("Bad Json");
    }
  }

  /**
   * Uses moshi to adapt the object returned by the web api call into a readable map object
   * @param httpBody - response from web api call
   * @return - readable map
   */
  private Map detailCreator(String httpBody) throws BadJsonException{
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<Map> recipeDataAdapter = moshi.adapter(Map.class);
      return recipeDataAdapter.fromJson(httpBody);
    }
    catch(Exception e){
      throw new BadJsonException("JSON Reading failure");
    }
  }

  /**
   * Parses the response into a success response from the map
   * @return - String success response
   */
  @Override
  public String successResponse() {
    return MapRecord.serialize();
  }

  /**
   * Parses the response into a failure response from the map
   * @param errorMessage - String errorMessage
   * @return
   */
  @Override
  public String failureResponse(String errorMessage) {
    return FailureResponse.FailureRecord.serialize(errorMessage);
  }

}
