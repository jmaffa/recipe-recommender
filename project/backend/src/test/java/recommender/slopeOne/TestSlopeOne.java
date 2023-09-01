package recommender.slopeOne;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * For testing the slopeOne predictive alogorithm
 */
public class TestSlopeOne {

  private Map<String, Map<String, Double>> userMap;
  private Map<String, Map<String, Double>> edgeMap;
  private Set<String> recipeSet;

  /**
   * Creates basic Map with data on every recipe
   */
  private void populateBasicMap(){
    Map<String, Double> dataMap = new HashMap<>();
    dataMap.put("A", 5.0);
    dataMap.put("B", 3.0);
    dataMap.put("C", 2.0);
    Map<String, Double> dataMap1 = new HashMap<>();
    dataMap1.put("A", 3.0);
    dataMap1.put("B", 4.0);
    Map<String, Double> dataMap2 = new HashMap<>();
    dataMap2.put("B", 2.0);
    dataMap2.put("C", 5.0);
    this.userMap = new HashMap<>();
    this.userMap.put("user0", dataMap);
    this.userMap.put("user1", dataMap1);
    this.userMap.put("user2", dataMap2);
    this.recipeSet = new HashSet<>();
    this.recipeSet.add("A");
    this.recipeSet.add("B");
    this.recipeSet.add("C");
  }

  /**
   * Creates a larger and more complex basic map for testing
   */
  private void populateBasicMap2(){
    Map<String, Double> dataMap = new HashMap<>();
    dataMap.put("A", 4.0);
    dataMap.put("C", 3.0);
    dataMap.put("D", 2.0);
    Map<String, Double> dataMap1 = new HashMap<>();
    dataMap1.put("A", 3.0);
    dataMap1.put("B", 2.0);
    dataMap1.put("D", 1.0);
    Map<String, Double> dataMap2 = new HashMap<>();
    dataMap2.put("A", 4.0);
    dataMap2.put("B", 3.0);
    dataMap2.put("C", 2.0);
    Map<String, Double> dataMap3 = new HashMap<>();
    dataMap3.put("A", 1.0);
    dataMap3.put("D", 2.0);
    this.userMap = new HashMap<>();
    this.userMap.put("user0", dataMap);
    this.userMap.put("user1", dataMap1);
    this.userMap.put("user2", dataMap2);
    this.userMap.put("user3", dataMap3);
    this.recipeSet = new HashSet<>();
    this.recipeSet.add("A");
    this.recipeSet.add("B");
    this.recipeSet.add("C");
    this.recipeSet.add("D");
  }

  /**
   * Creates Map with no data on recipe A
   */
  private void populateEdgeMap(){
    this.edgeMap = new HashMap<>();
    this.recipeSet = new HashSet<>();
    this.recipeSet.add("A");
    this.recipeSet.add("B");
    this.recipeSet.add("C");
    Map<String, Double> dataMap = new HashMap<>();
    dataMap.put("B", 2.0);
    dataMap.put("C", 3.0);
    Map<String, Double> dataMap1 = new HashMap<>();
    dataMap1.put("B", 4.0);
    dataMap1.put("C", 1.0);
    Map<String, Double> dataMap2 = new HashMap<>();
    dataMap2.put("B", 3.0);
    this.edgeMap.put("user0", dataMap);
    this.edgeMap.put("user1", dataMap1);
    this.edgeMap.put("user2", dataMap2);
  }

  /**
   * Test predictions for basic map
   */
  @Test
  public void testSlopeOnePredictions(){
    this.populateBasicMap();
    SlopeOne.slopeOne(this.userMap, this.recipeSet);
    Assert.assertEquals(SlopeOne.getUserRec("user0"), "A");
    Assert.assertEquals(SlopeOne.getUserRec("user1"), "B");
    Assert.assertEquals(SlopeOne.getUserRec("user2"), "C");
  }

  /**
   * Checks predictions, and verifies that existing ratings remain unchanged
   */
  @Test
  public void testSlopeOneAllocations(){
    this.populateBasicMap();
    SlopeOne.slopeOne(this.userMap, this.recipeSet);
    Map<String, Map<String, Double>> outputMap = SlopeOne.returnOutputMap();
    Assert.assertEquals(Math.round(outputMap.get("user0").get("A")), 5);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("B")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("C")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("A")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("B")), 4);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("C")) ,3);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("A")), 4);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("B")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("C")), 5);

  }

  /**
   * Tests the slopeOne algo on a more complex and large dataset
   */
  @Test
  public void testLargerDataSet(){
    this.populateBasicMap2();
    SlopeOne.slopeOne(this.userMap, this.recipeSet);
    Map<String, Map<String, Double>> outputMap = SlopeOne.returnOutputMap();
    System.out.println(outputMap);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("A")), 4);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("B")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("C")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("D")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("A")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("B")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("C")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("D")), 1);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("A")), 4);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("B")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("C")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("D")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user3").get("A")), 1);
    Assert.assertEquals(Math.round(outputMap.get("user3").get("C")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user3").get("D")), 2);
  }

  /**
   * Tests that recipes with no data are automatically assigned the value '-1'
   */
  @Test
  public void testMapWithMissingData(){
    this.populateEdgeMap();
    SlopeOne.slopeOne(this.edgeMap, this.recipeSet);
    Map<String, Map<String, Double>> outputMap = SlopeOne.returnOutputMap();
    Assert.assertEquals(Math.round(outputMap.get("user0").get("A")), -1);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("B")), 2);
    Assert.assertEquals(Math.round(outputMap.get("user0").get("C")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("A")), -1);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("B")), 4);
    Assert.assertEquals(Math.round(outputMap.get("user1").get("C")) ,1);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("A")), -1);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("B")), 3);
    Assert.assertEquals(Math.round(outputMap.get("user2").get("C")), 2);
  }

  /**
   * Test predictions for edge map
   */
  @Test
  public void testEdgeMapPredictions(){
    this.populateEdgeMap();
    SlopeOne.slopeOne(this.edgeMap, this.recipeSet);
    Assert.assertEquals(SlopeOne.getUserRec("user0"), "C");
    Assert.assertEquals(SlopeOne.getUserRec("user1"), "B");
  }


}
