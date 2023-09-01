package recipe;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import database.DatabaseReader;
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
import java.lang.reflect.MalformedParameterizedTypeException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import responses.MapSerializer.MapRecord;
import weather.exceptions.BadJsonException;
import weather.exceptions.DatasourceException;

public class RecipeHandler implements Handler {

    @Override
    public Object handle(Request request, Response response) throws URISyntaxException, IOException, InterruptedException, DatasourceException{
        QueryParamsMap qm = request.queryMap();
        String recipeID = qm.value("recipeID");

        if (request.queryParams().isEmpty()){
            return this.failureResponse("error_bad_request");
        }
        try {
            Object resultMap = this.getRecipeData(this.handleRecipeDetails(Double.parseDouble(recipeID)));
            MapRecord.results.put("result", "success");
            MapRecord.results.put("recipes", resultMap);
            return this.successResponse();
        } catch (BadJsonException badJsonException){
            return this.failureResponse("error_bad_json");
        }
        catch (DatasourceException datasourceException){
            return this.failureResponse("error_datasource");
        }
    }

    private Map<String, Object> getRecipeData(Map<String, Object> generalMap)
        throws BadJsonException, DatasourceException, URISyntaxException, IOException, InterruptedException {
        Map<String, Object> myRecipeMap = new HashMap<>();
        myRecipeMap.put("id", generalMap.get("id"));
        myRecipeMap.put("title", generalMap.get("title"));
        myRecipeMap.put("sourceUrl", generalMap.get("sourceUrl"));
        myRecipeMap.put("readyInMinutes", generalMap.get("readyInMinutes"));
        myRecipeMap.put("pricePerServing", generalMap.get("pricePerServing"));
        return myRecipeMap;
    }

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


    @Override
    public String successResponse() {
        return MapRecord.serialize();
    }


    @Override
    public String failureResponse(String errorMessage) {
        return FailureResponse.FailureRecord.serialize(errorMessage);
    }

}
