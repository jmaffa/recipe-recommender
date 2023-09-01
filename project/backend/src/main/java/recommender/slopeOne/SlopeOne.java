package recommender.slopeOne;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * Slope One algorithm implementation based on https://www.baeldung.com/java-collaborative-filtering-recommendations
 */
public class SlopeOne {

  private static Map<String, Map<String, Double>> diff = new HashMap<>();
  private static Map<String, Map<String, Integer>> freq = new HashMap<>();
  private static Map<String, Map<String, Double>> inputData;
  private static Map<String, Map<String, Double>> outputData = new HashMap<>();


  /**
   * Static method that loads and builds the matrices that conduct SlopeOne computation
   * and predictions
   * @param userMap - map of current user recipes/ratings
   * @param recipes - set of all recipes in the dataset
   */
  public static void slopeOne(Map<String, Map<String, Double>> userMap, Set<String> recipes) {
    inputData = userMap;
    buildDifferencesMatrix(inputData);
    predict(inputData, recipes);
  }

  /**
   * Returns the highest ranking recipe that the user has not tried yet
   * @param username - userID in the database
   * @return - String recommendation of recipe to try next based on ratings of
   * other recipes
   */
  public static String getUserRec(String username) {
    Map<String, Double> ratingMap = new HashMap<>(
        Collections.unmodifiableMap(outputData.get(username)));
    Set<Double> ratingSet = new HashSet<>(ratingMap.values());
    List<Double> sortedList = new ArrayList<>(ratingSet);
    Collections.sort(sortedList);
    Collections.reverse(sortedList);

    for (Double values : sortedList) {
      for (String recipeName : ratingMap.keySet()){
        if (Objects.equals(ratingMap.get(recipeName), values) && !inputData.containsKey(recipeName)) {
          return recipeName;
        }
      }
    }
    return null;
  }

  /**
   * Returns the top 5 highest ranked recipes that the user has not tried yet
   * @param username - userID in the database
   * @return - List of recipes that the user should try based on recipes enjoyed previously
   */
  public static List<String> getUserRecList(String username) {
    Map<String, Double> ratingMap = new HashMap<>(
        Collections.unmodifiableMap(outputData.get(username)));
    Set<Double> ratingSet = new HashSet<>(ratingMap.values());
    List<Double> sortedList = new ArrayList<>(ratingSet);
    Collections.sort(sortedList);
    Collections.reverse(sortedList);
    List<String> topFive = new ArrayList<>();
    int counter = 0;
    for (Double values : sortedList) {
      for (String recipeName : ratingMap.keySet()){
        if (counter < 5 && Objects.equals(ratingMap.get(recipeName), values) && !inputData.containsKey(recipeName)) {
          topFive.add(recipeName);
          counter += 1;
          System.out.println(recipeName + ": " + values);
        }
      }
    }
    return topFive;
  }

  /**
   * Returns the output map, which stores current recipe ratings and predicted recipe ratings
   * @return - Outputdata map
   */
  public static Map<String, Map<String, Double>> returnOutputMap(){
    return Collections.unmodifiableMap(outputData);
  }

  /**
   * Returns the input map, which stores current recipe ratings only
   * @return - Inputdata map
   */
  public static Map<String, Map<String, Double>> returnInputMap(){
    return Collections.unmodifiableMap(inputData);
  }

  /**
   * Based on the available data, calculate the relationships between the
   * items and number of occurences
   *
   * @param data - existing user data and their items' ratings
   */
  private static void buildDifferencesMatrix(Map<String, Map<String, Double>> data) {
    for (Map<String, Double> user : data.values()) {
      for (Entry<String, Double> e : user.entrySet()) {
        if (!diff.containsKey(e.getKey())) {
          diff.put(e.getKey(), new HashMap<>());
          freq.put(e.getKey(), new HashMap<>());
        }
        for (Entry<String, Double> e2 : user.entrySet()) {
          int oldCount = 0;
          if (freq.get(e.getKey()).containsKey(e2.getKey())) {
            oldCount = freq.get(e.getKey()).get(e2.getKey());
          }
          double oldDiff = 0.0;
          if (diff.get(e.getKey()).containsKey(e2.getKey())) {
            oldDiff = diff.get(e.getKey()).get(e2.getKey());
          }
          double observedDiff = e.getValue() - e2.getValue();
          freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
          diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
        }
      }
    }
    for (String j : diff.keySet()) {
      for (String i : diff.get(j).keySet()) {
        double oldValue = diff.get(j).get(i);
        int count = freq.get(j).get(i);
        diff.get(j).put(i, oldValue / count);
      }
    }
  }

  /**
   * Based on existing data predict all missing ratings. If prediction is not
   * possible, the value will be equal to -1
   *
   * @param data - existing user data and their items' ratings
   */
  private static void predict(Map<String, Map<String, Double>> data, Set<String> recipes) {
    Map<String, Double> uPred = new HashMap<>();
    Map<String, Integer> uFreq = new HashMap<>();
    for (String j : diff.keySet()) {
      uFreq.put(j, 0);
      uPred.put(j, 0.0);
    }
    for (Entry<String, Map<String, Double>> e : data.entrySet()) {
      for (String j : e.getValue().keySet()) {
        for (String k : diff.keySet()) {
          try {
            double predictedValue = diff.get(k).get(j) + e.getValue().get(j);
            double finalValue = predictedValue * freq.get(k).get(j);
            uPred.put(k, uPred.get(k) + finalValue);
            uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
          } catch (NullPointerException e1) {
          }
        }
      }
      HashMap<String, Double> clean = new HashMap<>();
      for (String j : uPred.keySet()) {
        if (uFreq.get(j) > 0) {
          clean.put(j, uPred.get(j) / uFreq.get(j));
        }
      }
      for (String j : recipes) {
        if (e.getValue().containsKey(j)) {
          clean.put(j, e.getValue().get(j));
        } else if (!clean.containsKey(j)) {
          clean.put(j, -1.0);
        }
      }
      outputData.put(e.getKey(), clean);
    }
  }

}