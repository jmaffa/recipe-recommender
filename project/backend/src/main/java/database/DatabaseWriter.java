package database;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import responses.MapSerializer.MapRecord;

public class DatabaseWriter {

  public DatabaseWriter(){

  }

  public void createNewUser(String UserID)
      throws URISyntaxException, IOException, InterruptedException {
    try {
      String token = new FirebaseInitializer().getToken();
      String URL =
          "https://cs32reciperecommender-default-rtdb.firebaseio.com/users/" + UserID + ".json" +
              "?access_token=" + token;
      MapRecord.results.clear();
      MapRecord.results.put("ingredients", "");
      MapRecord.results.put("pantry_staples", "");
      MapRecord.results.put("saved", "");
      MapRecord.results.put("ratings", new HashMap<>());
      HttpRequest newProfile =
          HttpRequest.newBuilder()
              .header(UserID, MapRecord.serialize())
              .PUT(HttpRequest.BodyPublishers.ofString(MapRecord.serialize()))
              .uri(URI.create(URL))
              .build();

      HttpClient client = HttpClient.newHttpClient();

      client.sendAsync(newProfile, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body)
          .thenAccept(System.out::println)
          .join();
    }
    catch (IOException e){
      throw new IOException("error with token file");
    }
  }

  public void updateUser(String UserID, String ingredients, String staples, String saved,
      Map<String, Double> ratings) throws IOException {

    try{
      String token = new FirebaseInitializer().getToken();
      String URL = "https://cs32reciperecommender-default-rtdb.firebaseio.com/users/"+UserID+".json"+
          "?access_token="+token;
      MapRecord.results.clear();
      MapRecord.results.put("ingredients", ingredients);
      MapRecord.results.put("pantry_staples", staples);
      MapRecord.results.put("saved", saved);
      MapRecord.results.put("ratings", ratings);
      HttpRequest newProfile =
          HttpRequest.newBuilder()
              .header(UserID, MapRecord.serialize())
              .PUT(HttpRequest.BodyPublishers.ofString(MapRecord.serialize()))
              .uri(URI.create(URL))
              .build();

      HttpClient client = HttpClient.newHttpClient();

      client.sendAsync(newProfile, HttpResponse.BodyHandlers.ofString())
          .thenApply(HttpResponse::body)
          .thenAccept(System.out::println)
          .join();
    }
    catch(IOException e){
      throw new IOException("error with token file");
    }



  }




  }




