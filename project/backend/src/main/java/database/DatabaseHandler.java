package database;

import database.DatabaseReader.Profile;
import java.util.Map;
import java.util.Objects;
import responses.FailureResponse;
import responses.MapSerializer.MapRecord;
import server.Handler;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import weather.exceptions.BadJsonException;

public class DatabaseHandler implements Handler {

  //localhost:3232/database?function=new&userID=USERID --> CREATE A NEW PROFILE - DONE
  //localhost:3232/database?function=lookup&userID=USERID --> lookup existing profile - DONE
  //localhost:3232/database?function=ingredient&userID=USERID&input=STRING --> ENTER INGREDIENTS TO A PROFILE - DONE
  //localhost:3232/database?function=viewIngredients&userID=USERID --> retrieve ingredients! - DONE
  //localhost:3232/database?function=editRatings&userID=USERID&recipe=recipeID&rating=RATING --> edit ratings!-DONE
  //localhost:3232/database?function=viewRatings&userID=USERID --> retrieve ratings hashmap - DONE
  //localhost:3232/database?function=unsave&userID=USERID&recipe=recipeID - DONE

  @Override
  public Object handle(Request request, Response response) throws Exception {
    MapRecord.results.clear();
    QueryParamsMap qm = request.queryMap();
    String function = qm.value("function");
    String userID = qm.value("userID");

    if (Objects.equals(function, "new")){
      new DatabaseWriter().createNewUser(userID);
      return this.successResponse();
    }

    if (Objects.equals(function, "ingredient")){
      String ingredients = qm.value("input");
      Profile userProfile = new DatabaseReader().getProfile(userID);
      new DatabaseWriter().updateUser(userID, ingredients, userProfile.pantry_staples(),
          userProfile.saved(), userProfile.ratings());
      return this.successResponse();
    }

    if (Objects.equals(function, "viewIngredients")){
      try{
        MapRecord.results.put("ingredients", new DatabaseReader().getProfile(userID).ingredients());
        return this.successResponse();
      }
      catch(BadJsonException e){
        return this.failureResponse("user not found");
      }

    }

    //localhost:3232/database?function=editRatings&userID=USERID&recipe=recipeID&rating=RATING --> edit ratings!
    if (Objects.equals(function, "editRatings")){
      String recipeID = qm.value("recipe");
      Double rating = Double.parseDouble(qm.value("rating"));
      Profile userProfile = new DatabaseReader().getProfile(userID);
      Map<String, Double> newRatings = userProfile.ratings();
      newRatings.put(recipeID, rating);
      new DatabaseWriter().updateUser(userID, userProfile.ingredients(), userProfile.pantry_staples(),
          userProfile.saved(), newRatings);
      return this.successResponse();
    }

    //localhost:3232/database?function=unsave&userID=USERID&recipe=recipeID
    if (Objects.equals(function, "unsave")){
      String recipeID = qm.value("recipe");
      Profile userProfile = new DatabaseReader().getProfile(userID);
      Map<String, Double> newRatings = userProfile.ratings();
      newRatings.remove(recipeID);
      new DatabaseWriter().updateUser(userID, userProfile.ingredients(), userProfile.pantry_staples(),
          userProfile.saved(), newRatings);
      return this.successResponse();
    }

    if (Objects.equals(function, "viewRatings")){
      try{
        MapRecord.results.put("ratings", new DatabaseReader().getProfile(userID).ratings());
        return this.successResponse();
      }
      catch(BadJsonException e){
        return this.failureResponse("user not found");
      }
    }

    if (Objects.equals(function, "lookup")){
      try{
        MapRecord.results.put(userID, new DatabaseReader().getProfile(userID));
        return this.successResponse();
      }
      catch(BadJsonException e){
        return this.failureResponse("user not found");
      }

    }
    return this.failureResponse("bad request");
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