package recommender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import recommender.csvhandler.CSVParser;
import recommender.csvhandler.FactoryFailureException;
import recommender.csvhandler.SubstCreator;
import recommender.slopeOne.SlopeOne;
import weather.exceptions.BadJsonException;

/**
 * REPL used during testing and development for SlopeOne program.
 * No longer in use after integration(!!)
 */
public class REPL {
  private HandleDatabase handleDatabase;
  private SlopeOne slopeOne;

  /**
   * REPL class construtor
   */
  public REPL() throws BadJsonException {
      this.handleDatabase = new HandleDatabase();
      this.slopeOne = new SlopeOne();
  }

  /**
   * Runs the REPL which executes user-inputted commands
   * This method is called in sol/Main.java to executing the REPL
   */
  public void run() {
    System.out.print(">>> ");
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(System.in))) {
      String line = reader.readLine();
      // While loop continuously reads and executes user input during program execution
      while (line != null) {
        // Formats the user input before running commands
        String response = "";
        String[] args = line.split(" ", 3);
        if (args.length == 0) {
          continue;
        }
        String command = args[0];
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\cs32\\term-project-jmaffa-lumuhoza-ntan13-zang\\project\\backend\\src\\main\\java\\recommender\\csvhandler\\substitutes.csv"));
        CSVParser<List<String>> parser = new CSVParser<>(bufferedReader, new SubstCreator(), true);
        HashMap ingrMap = parser.parse();
        // Based on the user-inputted commands, calls the corresponding ITravelController method
        switch (command) {
          case "ingredients":
            if (args.length ==1){
              try{
                response = ingrMap.keySet().toString();
              }
              catch (Exception e){
                e.getMessage();
              }
            }
            break;
          case "substitution":
            if (args.length == 3){
              String key = args[2].toLowerCase();
              if (ingrMap.containsKey(key)) {
                response = ingrMap.get(key).toString();
              }
              else{
                response = "no substitutes available for this ingredient, sorry!";
              }
            }
          case "commands":
            if (args.length == 1){
              try {
                response = "valid commands for this program: substitution, commands, recipes, computations";
              } catch (Exception e){
                response = e.getMessage();
              }
            }
            break;
          case "recipes":
            if (args.length == 1) {
              try {
                response = String.valueOf(this.handleDatabase.getRecipeSet());
              } catch (Exception e) {
                response = e.getMessage();
              }
            } else {
              response = "Usage: type 'recipes' to get list of all recipes in the program";
            }
            break;
          case "computations":
            if (args.length == 1){
              try{
                SlopeOne.slopeOne(this.handleDatabase.getUserRatingMap(), this.handleDatabase.getRecipeSet());
              } catch (Exception e) {
                response = e.getMessage();
              }
            }
            break;
          case "userRec":
            if (args.length == 2){
              try {
                SlopeOne.slopeOne(this.handleDatabase.getUserRatingMap(), this.handleDatabase.getRecipeSet());
                response = SlopeOne.getUserRec(args[1]);
              } catch (Exception e){
                response = e.getMessage();
              }
            }
            break;
          case "exit":
            if (args.length == 1){
              try {
                System.exit(0);
              }
              catch (Exception e){
                response = e.getMessage();
              }
            }
            break;
          default:
            response = "Invalid command. Available commands: substitution, ingredients, commands, recipes, computation, exit";
        }
        System.out.println(response);
        System.out.print(">>> ");
        line = reader.readLine();
      }
    } catch (IOException e) {
      System.out.println("IOException occurred.");
    } catch (FactoryFailureException e){
      System.out.println("Factory Failure Exception occurred");
    }
  }

}