package recommender;

import database.DatabaseReader;
import database.DatabaseReader.Profile;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import weather.exceptions.BadJsonException;

/**
 * HandleDatabase class that takes in data from the database and
 * creates a readable hashmap of recipes-ratings for every user.
 * Integrated with the backend for use in the App
 */
public class HandleDatabase {
  private final DatabaseReader databaseReader;
  private final List<String> usernameList;
  private final Map<String, Map<String, Double>> userRatingMap;
  private final Set<String> recipeSet;

  /**
   * HandleDatabase costructor which initialises the databaseReader,
   * usernameList, userRatingMap and recipeSet. Also populates the map
   */
  public HandleDatabase() throws BadJsonException {

    this.databaseReader = new DatabaseReader();
    this.usernameList = this.databaseReader.getUsers();
    this.userRatingMap = new HashMap<>();
    this.recipeSet = new HashSet<>();
    this.populateMap();
  }

  /**
   * Populates the userRatingMap with usernames and the recipe-rating map
   * for each user
   */
  private void populateMap() throws BadJsonException {
    for (String username: this.usernameList){
      Profile profile = this.databaseReader.getProfile(username);
      System.out.println(profile.ratings());
      Map<String, Double> ratingMap = profile.ratings();

      this.recipeSet.addAll(ratingMap.keySet());
      this.userRatingMap.put(username, ratingMap);
    }
  }

  /**
   * Returns the set of recipes in the database
   * @return - set of strings of recipes
   */
  public Set<String> getRecipeSet(){
    return Collections.unmodifiableSet(this.recipeSet);
  }

  /**
   * Returns the userRatingMap
   * @return Map of username to recipes to ratings
   */
  public Map<String, Map<String, Double>> getUserRatingMap(){
    return Collections.unmodifiableMap(this.userRatingMap);
  }

}
