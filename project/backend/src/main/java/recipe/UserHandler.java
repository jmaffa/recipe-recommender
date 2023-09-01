package recipe;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import database.DatabaseReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.xml.crypto.Data;
import recommender.csvhandler.CSVParser;
import recommender.csvhandler.FactoryFailureException;
import recommender.csvhandler.SubstCreator;
import responses.FailureResponse;
import responses.MapSerializer;
import server.Handler;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import weather.exceptions.BadJsonException;
import weather.exceptions.DatasourceException;

import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * This class handles the API calls to Spoonacular, based on data from
 * the backend database, and processes them into
 * a hashmap of data that is relevant to our App.
 */
public class UserHandler implements Handler {
  private Map<String, List<String>> subRecMap;

  /**
   * Constructor of the UserHandler class that initialises the substitution
   * recommendation map
   */
  public UserHandler() {
    this.subRecMap = new HashMap<>();
  }

  /**
   * Handles the API calls based on the query parameters
   * @param request - the API request
   * @param response - the API response
   * @return - success/failure response based on API calls

   */
  @Override
  public Object handle(Request request, Response response)
      throws URISyntaxException, IOException, InterruptedException, DatasourceException,
      BadJsonException, FactoryFailureException {

    if (request.queryParams().isEmpty()){
      return this.failureResponse("error_bad_request");
    }

    QueryParamsMap qm = request.queryMap();
    String userID = qm.value("userID");
    String maxPrice = qm.value("maxPrice");
    String maxTime = qm.value("maxTime");
    String diet = qm.value("diet");
    String dishType = qm.value("dishTypes");
    DatabaseReader.Profile newProfile;

    System.out.println(userID);
    try {
      DatabaseReader databaseReader = new DatabaseReader();
      newProfile = databaseReader.getProfile(userID);
    }
    catch (BadJsonException e){
      System.out.println("hello");
      return this.failureResponse("User ID not found");
    }

    String ingredientsList = newProfile.ingredients();
    // adds recipes based on ingredients that can be substituted for one another
    if (qm.hasKey("sub") && qm.value("sub").equals("y")) {
      Map<String, Object> subMap = new HashMap<>();
      this.fillSubMap();
      List<String> initialList = Arrays.asList(ingredientsList.split(","));
      List<String> substituteList = new ArrayList<>();
      for (String ingredient : initialList) {
        if (this.subRecMap.containsKey(ingredient)) {
          subMap.put(ingredient, this.subRecMap.get(ingredient));
          List<String> listIngr = this.subRecMap.get(ingredient);
          for (String subIn : listIngr){
            ingredientsList = ingredientsList.concat("," + subIn);
            substituteList.add(subIn);
          }

        }
      }
      MapSerializer.MapRecord.results.put("substitutes", subMap);
    }
    else {
      ingredientsList = ingredientsList;
    }
    try {
      List<Object> filteredList = new ArrayList<>();
      List recipeData = this.handleRecipeRequest(ingredientsList);
      for (int i = 0; i < recipeData.size(); i++) {
        Map<String, Object> myMap = this.getRecipeData((Map<String, Object>) recipeData.get(i));
        // handles maxPrice param
        if (qm.hasKey("maxPrice") && !qm.hasKey("maxTime") &!qm.hasKey("dishTypes") & !qm.hasKey("diet")) {
          if ((double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
            filteredList.add(myMap);
          }
        }
        // handles maxTime param
        if (qm.hasKey("maxTime") && !qm.hasKey("maxPrice") &!qm.hasKey("dishTypes") & !qm.hasKey("diet")){
          if ((double) myMap.get("readyInMinutes") <= Double.parseDouble(maxTime)) {
            filteredList.add(myMap);
          }
        }
        // handles maxTime and maxPrice concurrent params
        if (qm.hasKey("maxTime") && qm.hasKey("maxPrice") &!qm.hasKey("dishTypes") & !qm.hasKey("diet")){
          if ((double) myMap.get("readyInMinutes") <= Double.parseDouble(maxTime) && (double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
            filteredList.add(myMap);
          }
        }
        // handles diet param
        if (qm.hasKey("diet") & !qm.hasKey("maxTime") & !qm.hasKey("maxPrice") & !qm.hasKey("dishTypes")){
          switch (diet){
            case "vegan":
              if (myMap.get("vegan").equals(true)){
                filteredList.add(myMap);
              }
              break;
            case "glutenFree":
              if (myMap.get("glutenFree").equals(true)){
                filteredList.add(myMap);
              }
              break;
            case "vegetarian":
              if (myMap.get("vegetarian").equals(true)){
                filteredList.add(myMap);
              }
              break;
            default:
              break;
          }
        }
        // handles dishTypes param
        if (qm.hasKey("dishTypes") && !qm.hasKey("maxPrice") &!qm.hasKey("maxTime")& !qm.hasKey("diet")){
          List dishList = (List) myMap.get("dishTypes");
         if (dishType.equals("breakfast")){
           if (dishList.contains("breakfast")){
             filteredList.add(myMap);
           }
         }
         if (dishType.equals("lunch")){
           if (dishList.contains("lunch") || dishList.contains("main dish") || dishList.contains("main course")){
             filteredList.add(myMap);
           }
         }
         if (dishType.equals("dinner")){
           if (dishList.contains("dinner") || dishList.contains("main dish") || dishList.contains("main course")){
             filteredList.add(myMap);
           }
         }
         if (dishType.equals("dessert")) {
           if (dishList.contains("dessert")) {
             filteredList.add(myMap);
           }
         }
         if (dishType.equals("sideDish")) {
           if (dishList.contains("side dish")) {
             filteredList.add(myMap);
           }
         }
        }
        // handles dishTypes and maxPrice params
        if (qm.hasKey("dishTypes") && qm.hasKey("maxPrice") &!qm.hasKey("maxTime")& !qm.hasKey("diet")) {
          List dishList = (List) myMap.get("dishTypes");
          if (dishType.equals("breakfast")) {
            if (dishList.contains("breakfast")) {
              if ((double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
                filteredList.add(myMap);
              }
            }
          }
          if (dishType.equals("lunch")) {
            if (dishList.contains("lunch") || dishList.contains("main dish") || dishList.contains(
                "main course")) {
              if ((double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
                filteredList.add(myMap);
              }
            }
          }
          if (dishType.equals("dinner")) {
            if (dishList.contains("dinner") || dishList.contains("main dish") || dishList.contains(
                "main course")) {
              if ((double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
                filteredList.add(myMap);
              }
            }
          }
          if (dishType.equals("dessert")) {
            if (dishList.contains("dessert")) {
              if ((double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
                filteredList.add(myMap);
              }
            }
          }
          if (dishType.equals("sideDish")) {
            if (dishList.contains("side dish")) {
              if ((double) myMap.get("pricePerServing") <= Double.parseDouble(maxPrice)) {
                filteredList.add(myMap);
              }
            }
          }

        }
        // handles recipeData without any query params
        else if (!qm.hasKey("maxPrice") && !qm.hasKey("maxTime") && !qm.hasKey("diet") && !qm.hasKey("dishTypes")){
            filteredList.add(this.getRecipeData((Map<String, Object>) recipeData.get(i)));
        }
      }
      MapSerializer.MapRecord.results.put("result", "success");
      MapSerializer.MapRecord.results.put("ingredients", ingredientsList);
      MapSerializer.MapRecord.results.put("recipes", filteredList);

      return this.successResponse();
    }
    catch (DatasourceException datasourceException){
      return this.failureResponse("error_datasource");
    }
    catch (BadJsonException badJsonException){
      return this.failureResponse("error_bad_json");
    }
  }

  /**
   * Fills up the substitute recommendation map based on the CSV file of substitutes (web-scrapped)
   */
  private void fillSubMap() throws IOException, FactoryFailureException {
    BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\cs32\\term-project-jmaffa-lumuhoza-ntan13-zang\\project\\backend\\src\\main\\java\\recommender\\csvhandler\\substitutes.csv"));
    CSVParser parser = new CSVParser<>(bufferedReader, new SubstCreator(), true);
    this.subRecMap = parser.parse();
  }

  /**
   * Returns a JSON file of a map that contains all the relevant information
   * for use in the front-end of the app
   * @param generalMap - the map returned by the API call and generified by Moshi
   * @return Map<String, Object> of data relevant for the front end
   */
  private Map<String, Object> getRecipeData(Map<String, Object> generalMap)
      throws BadJsonException, DatasourceException, URISyntaxException, IOException, InterruptedException {
    Map<String, Object> myRecipeMap = new HashMap<>();
    myRecipeMap.put("id", generalMap.get("id"));
    myRecipeMap.put("title", generalMap.get("title"));
    myRecipeMap.put("missedIngredients", this.getMissedIngredientsList(
        (List<Map<Object, Object>>) generalMap.get("missedIngredients")));
    Map recipePriceURLTime = this.handleRecipeDetails((double) generalMap.get("id"));
    myRecipeMap.put("vegetarian", recipePriceURLTime.get("vegetarian"));
    myRecipeMap.put("glutenFree", recipePriceURLTime.get("glutenFree"));
    myRecipeMap.put("vegan", recipePriceURLTime.get("vegan"));
    myRecipeMap.put("readyInMinutes", recipePriceURLTime.get("readyInMinutes"));
    myRecipeMap.put("sourceUrl", recipePriceURLTime.get("sourceUrl"));
    myRecipeMap.put("pricePerServing", recipePriceURLTime.get("pricePerServing"));
    myRecipeMap.put("dishTypes", recipePriceURLTime.get("dishTypes"));
    return myRecipeMap;
  }

  /**
   * Creates a Map that matches ingredient name to ID, for missedIngredients
   * @param missedIngList - List of missedIngredients
   * @return - map of ingredient string name to ID
   */
  private Map<Object, Object> getMissedIngredients(List<Map<Object, Object>> missedIngList){
    Map<Object, Object> myRecipeMap = new HashMap<>();
    for (Map<Object, Object> ingredient : missedIngList){
      myRecipeMap.put(ingredient.get("name"), ingredient.get("id"));
    }
    return myRecipeMap;
  }

  /**
   * Creates a List of missed ingredients, based on current ingredients in the database and
   * the recommended recipes
   * @param missedIngList - List of missedIngredients
   * @return - list of missed ingredients
   */
  private List<Object> getMissedIngredientsList(List<Map<Object, Object>> missedIngList){
    List<Object> returnList = new ArrayList<>();
    for (Map<Object, Object> ingredient : missedIngList){
      returnList.add(ingredient.get("name"));
    }
    return returnList;
  }

  /**
   * Handles and makes the API call to spoonacular, to filter recipes based on the
   * ingredients passed into the params, and handles the exceptions that may be thrown
   * @param ingredients - ingredients that the recipes should be filtered on.
   * @return - list of recipes that contain the ingredients given in the parameters
   */
  public List handleRecipeRequest(String ingredients)
      throws URISyntaxException, InaccessibleObjectException, InterruptedException, DatasourceException,
      BadJsonException, IOException {
    APIKey key = new APIKey();
    HttpRequest getRecipeRequest = HttpRequest.newBuilder()
        .uri(new URI("https://api.spoonacular.com/recipes/findByIngredients?apiKey="+key.getKey()+"&ingredients=" + ingredients))
        .GET()
        .build();

    HttpResponse<String> recipeRequest = HttpClient.newBuilder()
        .build().
        send(getRecipeRequest, HttpResponse.BodyHandlers.ofString());

    if (recipeRequest.statusCode() == 400 || recipeRequest.statusCode() == 404 ||
        recipeRequest.statusCode() == 301 || recipeRequest.statusCode() == 500) {
      throw new DatasourceException("API error");
    }
    try {
      return this.recipeCreator(recipeRequest.body());
    }
    catch(Exception e){
      throw new BadJsonException("Bad Json");
    }
  }

  /**
   * Makes and handles the API call to spoonacular to fetch more information on the
   * recipe based on the recipe ID.
   * @param recipeID - the recipe ID to get more information on the recipe from Spoonacular
   * @return - Map of recipe details
   */
  public Map handleRecipeDetails(double recipeID)
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
   * Creates a list of recipes by using moshi to adapt the string returned
   * by the httpBody
   * @param httpBody - direct response from API call
   * @return - list of recipes
   */
  public List recipeCreator(String httpBody) throws BadJsonException{
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<List> recipeDataAdapter = moshi.adapter(List.class);
      return recipeDataAdapter.fromJson(httpBody);
    }
    catch(Exception e){
      throw new BadJsonException("JSON Reading failure");
    }
  }

  /**
   * Creates a Map of recipe details by using moshi to adapt the string returned
   * by the httpBody
   * @param httpBody - direct response from API call
   * @return - Map of recipe details
   */
  public Map detailCreator(String httpBody) throws BadJsonException{
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
   * Serialises the response into a consistent string
   * @return - String success response
   */
  @Override
  public String successResponse() {
    return MapSerializer.MapRecord.serialize();
  }

  /**
   * Serialises the response into a consistent string
   * @return - String failure response
   */
  @Override
  public String failureResponse(String errorMessage) {
    return FailureResponse.FailureRecord.serialize(errorMessage);
  }

}
