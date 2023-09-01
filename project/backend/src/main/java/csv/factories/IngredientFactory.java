package csv.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IngredientFactory implements CreatorFromRow<List<String>> {

  @Override
  public List<String> create(List<String> row) {
    ArrayList<String> ingredientList = new ArrayList<>();
    for(int i =0; i< row.size()-1; i++){
      String ingredient = "";
      ingredient += row.get(i);
      if (i== row.size()-2){
        ingredientList.add(ingredient);
      }
    }
    return ingredientList;


  }
}
