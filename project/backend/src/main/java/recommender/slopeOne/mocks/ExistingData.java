package recommender.slopeOne.mocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Used to build and test SlopeOne algo during development phase.
 * Not in use(!!) after integration
 */
public class ExistingData {

  public List<String> recipeList;

  protected static List<String> items = Arrays.asList("chicken_satay", "chicken_burritos", "tomato_soup",
      "tomato_tarte_tatin", "apple_crumble", "chicken_lo_mein", "pasta_margherita");

  /**
   * Converts recipes to string
   */
  private void recipeToString(){
    this.recipeList = new ArrayList<>();
    for (int i =0; i< items.size(); i++){
      recipeList.add(items.get(i));
    }
  }

  /**
   * Creates Map of username to recipe to ratings
   * @param numberOfUsers - number of users to randomly create recipe-rating pairs for
   * @param user - user that we want to obtain the data for
   * @param userHashmap - the hashmap of data
   * @return - map of data filled
   */
  public static Map<String, HashMap<String, Double>> initializeData(int numberOfUsers, String user, HashMap<String, Double> userHashmap) {
    Map<String, HashMap<String, Double>> data = new HashMap<>();
    HashMap<String, Double> newUser;
    Set<String> newRecommendationSet;
    for (int i = 0; i < numberOfUsers; i++) {
      newUser = new HashMap<>();
      newRecommendationSet = new HashSet<>();
      for (int j = 0; j < 3; j++) {
        newRecommendationSet.add(items.get((int) (Math.random() * 6)));
      }
      for (String recipe : newRecommendationSet) {
        newUser.put(recipe, Math.random());
      }
      data.put("User " + i, newUser);
      if (Objects.equals(user, "User" + i)) {
        HashMap<String, Double> hashMap = data.get(user);
        for (Map.Entry<String, Double> entry : userHashmap.entrySet()) {
          hashMap.put(entry.getKey(),
              entry.getValue());
        }
      }
      else {
        data.put(user, userHashmap);
      }
    }
    return data;
  }

  /**
   * Creates Map of username to recipe to ratings
   * @param numberOfUsers - number of users to randomly create recipe-rating pairs for
   * @return - map of data filled
   */
  public static Map<String, HashMap<String, Double>> initializeDataForCompute(int numberOfUsers) {
    Map<String, HashMap<String, Double>> data = new HashMap<>();
    HashMap<String, Double> newUser;
    Set<String> newRecommendationSet;
    for (int i = 0; i < numberOfUsers; i++) {
      newUser = new HashMap<String, Double>();
      newRecommendationSet = new HashSet<>();
      for (int j = 0; j < 3; j++) {
        newRecommendationSet.add(items.get((int) (Math.random() * 6)));
      }
      for (String recipe : newRecommendationSet) {
        newUser.put(recipe, Math.random());
      }
      data.put("User " + i, newUser);
    }
    return data;
  }

  /**
   * Returns list of recipes
   * @return - list of strings of recipes
   */
  public List<String> getRecipeList(){
    this.recipeToString();
    return Collections.unmodifiableList(this.recipeList);
  }
}
