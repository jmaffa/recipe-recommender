package recommender.slopeOne.mocks;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import recommender.slopeOne.mocks.ExistingData;

/**
 * Slope One algorithm implementation based on https://www.baeldung.com/java-collaborative-filtering-recommendations
 * Old version that is not in use after integration(!!)
 */
public class OldSlopeOne {

  private static Map<String, Map<String, Double>> diff = new HashMap<>();
  private static Map<String, Map<String, Integer>> freq = new HashMap<>();
  private static Map<String, HashMap<String, Double>> inputData;
  private static Map<String, HashMap<String, Double>> outputData = new HashMap<>();

  public static void slopeOne(int numberOfUsers, String user, HashMap<String, Double> userMap, Boolean wantDetails) {
    if (wantDetails){
      inputData = ExistingData.initializeData(numberOfUsers,user, userMap);
      System.out.println("Slope One - Before the Prediction\n");
      buildDifferencesMatrix(inputData, true);
      System.out.println("\nSlope One - With Predictions\n");
      predict(inputData, true);
    }
    else{
      inputData = ExistingData.initializeData(numberOfUsers,user, userMap);
      buildDifferencesMatrix(inputData, false);
      predict(inputData, false);
    }
  }

  public static void slopeOneJustComputation(int numberOfUsers) {
    inputData = ExistingData.initializeDataForCompute(numberOfUsers);
    System.out.println("Slope One - Before the Prediction\n");
    buildDifferencesMatrix(inputData, true);
    System.out.println("\nSlope One - With Predictions\n");
    predict(inputData, true);

  }


  public Map<String, HashMap<String, Double>> returnOutputMap(){
    return Collections.unmodifiableMap(outputData);
  }

  public Map<String, HashMap<String, Double>> returnInputMap(){
    return Collections.unmodifiableMap(inputData);
  }
  /**
   * Based on the available data, calculate the relationships between the
   * items and number of occurences
   *
   * @param data - existing user data and their items' ratings
   */
  private static void buildDifferencesMatrix(Map<String, HashMap<String, Double>> data, Boolean print) {
    for (HashMap<String, Double> user : data.values()) {
      for (Entry<String, Double> e : user.entrySet()) {
        if (!diff.containsKey(e.getKey())) {
          diff.put(e.getKey(), new HashMap<String, Double>());
          freq.put(e.getKey(), new HashMap<String, Integer>());
        }
        for (Entry<String, Double> e2 : user.entrySet()) {
          int oldCount = 0;
          if (freq.get(e.getKey()).containsKey(e2.getKey())) {
            oldCount = freq.get(e.getKey()).get(e2.getKey()).intValue();
          }
          double oldDiff = 0.0;
          if (diff.get(e.getKey()).containsKey(e2.getKey())) {
            oldDiff = diff.get(e.getKey()).get(e2.getKey()).doubleValue();
          }
          double observedDiff = e.getValue() - e2.getValue();
          freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
          diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
        }
      }
    }
    for (String j : diff.keySet()) {
      for (String i : diff.get(j).keySet()) {
        double oldValue = diff.get(j).get(i).doubleValue();
        int count = freq.get(j).get(i).intValue();
        diff.get(j).put(i, oldValue / count);
      }
    }
    if (print) {
      printData(data);
    }
  }

  /**
   * Based on existing data predict all missing ratings. If prediction is not
   * possible, the value will be equal to -1
   *
   * @param data - existing user data and their items' ratings
   */
  private static void predict(Map<String, HashMap<String, Double>> data, Boolean printData) {
    HashMap<String, Double> uPred = new HashMap<String, Double>();
    HashMap<String, Integer> uFreq = new HashMap<String, Integer>();
    for (String j : diff.keySet()) {
      uFreq.put(j, 0);
      uPred.put(j, 0.0);
    }
    for (Entry<String, HashMap<String, Double>> e : data.entrySet()) {
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
      HashMap<String, Double> clean = new HashMap<String, Double>();
      for (String j : uPred.keySet()) {
        if (uFreq.get(j) > 0) {
          clean.put(j, uPred.get(j).doubleValue() / uFreq.get(j).intValue());
        }
      }
      for (String j : ExistingData.items) {
        if (e.getValue().containsKey(j)) {
          clean.put(j, e.getValue().get(j));
        } else if (!clean.containsKey(j)) {
          clean.put(j, -1.0);
        }
      }
      outputData.put(e.getKey(), clean);
    }
    if (printData) {
      printData(outputData);
    }
  }

  private static void printData(Map<String, HashMap<String, Double>> data) {
    for (String user : data.keySet()) {
      System.out.println(user + ":");
      print(data.get(user));
    }
  }

  private static void print(HashMap<String, Double> hashMap) {
    NumberFormat formatter = new DecimalFormat("#0.000");
    for (String j : hashMap.keySet()) {
      System.out.println(" " + j + " --> " + formatter.format(hashMap.get(j).doubleValue()));
    }
  }

}