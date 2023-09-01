package server;

import static spark.Spark.after;
import static spark.Spark.get;
import csv.GetCSVHandler;
import csv.LoadCSVHandler;
import database.DatabaseHandler;
import recipe.RatingHandler;
import recipe.RecipeHandler;
import recipe.UserHandler;
import spark.Spark;
import weather.WeatherHandler;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers. There are three endpoints: loadcsv, getcsv, and weather.
 */
public class Server {
  public static void main(String[] args) {
    Spark.port(3232);

    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });
    // sets /loadcsv, /getcsv, and /weather endpoints
    Spark.get("loadcsv", new LoadCSVHandler());
    Spark.get("getcsv", new GetCSVHandler());
    Spark.get("weather", new WeatherHandler());
    Spark.get("recipe", new RecipeHandler());
    Spark.get("recipeIngredients", new UserHandler());
    Spark.get("database", new DatabaseHandler());
    Spark.get("getRatingRec", new RatingHandler());
    Spark.init();
    Spark.awaitInitialization();
    System.out.println("Server started.");
  }
}
