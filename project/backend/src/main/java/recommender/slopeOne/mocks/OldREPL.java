package recommender.slopeOne.mocks;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import recommender.csvhandler.CSVParser;
import recommender.csvhandler.FactoryFailureException;
import recommender.csvhandler.SubstCreator;

/**
 * REPL that was created before integration for purposes of testing
 * and being a proof of concept. No longer in use after integration!
 */
public class OldREPL {
    private final ExistingData existingData;
    private final OldSlopeOne slopeOne;
    public OldREPL(){
        this.existingData = new ExistingData();
        this.slopeOne = new OldSlopeOne();
    }
    private List<String> splitString(String args){
        return Arrays.asList(args.split("\\s*,\\s*"));
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
                                response = "valid commands for this program: substitution, commands, recipes, ratings, ratings_details, computations";
                            } catch (Exception e){
                                response = e.getMessage();
                            }
                        }
                        break;
                    case "recipes":
                        if (args.length == 1) {
                            try {
                                response = String.valueOf(this.existingData.getRecipeList());
                            } catch (Exception e) {
                                response = e.getMessage();
                            }
                        } else {
                            response = "Usage: type 'recipes' to get list of all recipes in the program";
                        }
                        break;
                    case "ratings":
                        if (args.length  == 3) {
                            HashMap<String, Double> map = new HashMap<>();
                            String username = args[1];
                            List<String> stringList = this.splitString(args[2]);
                            System.out.println(stringList);
                            for (int i = 0; i < stringList.size(); i += 2) {
                                String recipe = stringList.get(i);
                                map.put(recipe, Double.parseDouble(stringList.get(i + 1)));
                            }
                            String userYou ="User " + username;
                            try {
                                OldSlopeOne.slopeOne(20, userYou, map, false);
                                Map<String, HashMap<String, Double>> mapMap = this.slopeOne.returnOutputMap();
                                HashMap<String, Double> innerMap = mapMap.get(userYou);
                                for (String recipe : this.slopeOne.returnInputMap().get(userYou).keySet()) {
                                    innerMap.keySet().remove(recipe);
                                }
                                double max =  Collections.max(innerMap.values());
                                for (String recipe: innerMap.keySet()){
                                    if (innerMap.get(recipe) == max){
                                        response = "you should try " + recipe + " next!";
                                    }
                                }
                            } catch (Exception e) {
                                response = e.getMessage();
                            }
                        } else {
                            response = "Usage: input your own ratings for recipes in the dataset that you've tried so far";
                        }
                        break;
                    case "ratings_details" :
                        if (args.length  == 3) {
                            HashMap<String, Double> map = new HashMap<>();
                            String username = args[1];
                            List<String> stringList = this.splitString(args[2]);
                            System.out.println(stringList);
                            for (int i = 0; i < stringList.size(); i += 2) {
                                String recipe = stringList.get(i);
                                map.put(recipe, Double.parseDouble(stringList.get(i + 1)));
                            }
                            String userYou = "User " + username;
                            try {
                                OldSlopeOne.slopeOne(20, userYou, map, true);
                                Map<String, HashMap<String, Double>> mapMap = this.slopeOne.returnOutputMap();
                                HashMap<String, Double> innerMap = mapMap.get(userYou);
                                for (String recipe : this.slopeOne.returnInputMap().get(userYou).keySet()) {
                                    innerMap.keySet().remove(recipe);
                                }
                                double max =  Collections.max(innerMap.values());
                                for (String recipe: innerMap.keySet()){
                                    if (innerMap.get(recipe) == max){
                                        response = "you should try " + recipe + " next!";
                                    }
                                }
                            } catch (Exception e) {
                                response = e.getMessage();
                            }
                        } else {
                            response = "Usage: input your own ratings for recipes in the dataset that you've tried so far";
                        }
                    case "computations":
                        if (args.length == 1){
                            try{
                                OldSlopeOne.slopeOneJustComputation(10);
                            } catch (Exception e) {
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
                        response = "Invalid command. Available commands: substitution, ingredients, commands, recipes, computation, ratings, ratings_details, exit";
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