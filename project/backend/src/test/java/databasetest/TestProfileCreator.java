package databasetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import database.DatabaseReader;
import database.DatabaseReader.Profile;
import database.DatabaseWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import weather.exceptions.BadJsonException;

public class TestProfileCreator {


  @Test
  public void testGetProfileBasic()
      throws BadJsonException, URISyntaxException, IOException, InterruptedException {
    DatabaseReader dbhandler = new DatabaseReader();
    Profile sampleProfile = dbhandler.getProfile("nadyatan21");
    assertTrue(sampleProfile.ingredients().contains("sugar"));
    assertTrue(sampleProfile.saved().contains("blueberry_pie"));
    assertTrue(sampleProfile.pantry_staples().contains("rice"));
    assertEquals(3, sampleProfile.ratings().get("632485"));
  }

  @Test
  public void testRetrieval(){
    DatabaseReader dbhandler = new DatabaseReader();

    HashMap<String, Double> ratingsMap = new HashMap<String, Double>();
    ratingsMap.put("chicken rice", 2.8);

    Profile sample = new Profile(
        "sugar, butter, flour", "blueberry pie, rasberry pie",
        "salt, pepper", ratingsMap
        );

    List<String> ingredients = dbhandler.getIngredients(sample);

    assertTrue(ingredients.contains("sugar"));
    assertEquals(3, ingredients.size());

    List<String> recipes = dbhandler.getSaved(sample);

    assertTrue(recipes.contains("blueberry pie"));
    assertEquals(2, recipes.size());

    List<String> staples = dbhandler.getPantryStaples(sample);
    assertTrue(staples.contains("salt"));

    Map<String, Double> ratings = dbhandler.getRatings(sample);
    assertEquals(2.8, ratings.get("chicken rice"));
  }

  @Test
  public void testAddProfile() throws URISyntaxException, IOException, InterruptedException {
    DatabaseWriter writer = new DatabaseWriter();
    writer.createNewUser("naKKWNEWsnsjm");
  }

  @Test
  public void testGetUserID() throws BadJsonException {
    DatabaseReader reader = new DatabaseReader();
    List<String> users = reader.getUsers();
    assertTrue(users.contains("nadyatan21"));
  }

}
