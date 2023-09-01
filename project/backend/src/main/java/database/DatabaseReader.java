package database;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import weather.exceptions.BadJsonException;

public class DatabaseReader {

  public DatabaseReader(){

  }

  public Profile getProfile (String userID)
      throws BadJsonException{

    try{
      String token = new FirebaseInitializer().getToken();

      String URL = "https://cs32reciperecommender-default-rtdb.firebaseio.com/users/"+userID+".json"
          + "?access_token="+token;

      HttpRequest getProfile =
          HttpRequest.newBuilder()
              .uri(new URI(URL))
              .GET()
              .build();

      HttpResponse<String> sendGetProfileResponse =
          HttpClient.newBuilder().build().send(getProfile, HttpResponse.BodyHandlers.ofString());

      System.out.println(this.profileDataCreator(sendGetProfileResponse.body()));
//      if (this.profileDataCreator(sendGetProfileResponse.body())== null){
//        throw new BadJsonException("User ID not found!");
//      }

      return this.profileDataCreator(sendGetProfileResponse.body());
    }

    catch (URISyntaxException e){
      throw new BadJsonException("User ID not found!");
    }
    catch (Exception e) {
      System.out.println("json reading failure");
      throw new BadJsonException("JSON Reading failure");
    }
  }

  public List<String> getUsers() throws BadJsonException {

    try{
      String token = new FirebaseInitializer().getToken();

      String URL = "https://cs32reciperecommender-default-rtdb.firebaseio.com/users/.json?"
          + "access_token="+token+"&shallow=true";

      HttpRequest getProfile =
          HttpRequest.newBuilder()
              .uri(new URI(URL))
              .GET()
              .build();

      HttpResponse<String> sendGetProfileResponse =
          HttpClient.newBuilder().build().send(getProfile, HttpResponse.BodyHandlers.ofString());

      String innerString = sendGetProfileResponse.body()
          .substring(1, sendGetProfileResponse.body().length() -1);
      String nextString = innerString.replaceAll(":true", "");
      String lastString = nextString.replaceAll("\"", "");
      return List.of(lastString.split(","));

    }
    catch (URISyntaxException e){
      throw new BadJsonException("User ID not found!");
    }
    catch (Exception e) {
      throw new BadJsonException("JSON Reading failure");
    }
  }

  public List<String> getIngredients(Profile profile){
    String ingredientsString = profile.ingredients();
    return List.of(ingredientsString.split("[,]"));
  }

  public List<String> getSaved(Profile profile){
    String recipeString = profile.saved();
    return List.of(recipeString.split(","));
  }

  public List<String> getPantryStaples(Profile profile){
    String pantryStaplesString = profile.pantry_staples();
    return List.of(pantryStaplesString.split(","));
  }

  public Map<String, Double> getRatings(Profile profile) {
    return profile.ratings();
  }

  public record Profile (String ingredients, String saved, String pantry_staples,
                         Map<String, Double> ratings){}


  public Profile profileDataCreator(String httpBody) throws BadJsonException {
    try {
      Moshi moshi = new Moshi.Builder().build();
      JsonAdapter<Profile> profileDataAdapter = moshi.adapter(Profile.class);
      return profileDataAdapter.fromJson(httpBody);
    } catch (Exception e) {
      throw new BadJsonException("JSON Reading failure");
    }
  }


}
