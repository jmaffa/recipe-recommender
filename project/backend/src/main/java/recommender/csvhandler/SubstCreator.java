package recommender.csvhandler;
import java.util.List;

/**
 * SubstCreator that implements the CreatorFromRow interface
 */
public class SubstCreator implements CreatorFromRow<List<String>>{

  /**
   * Creates a list of strings, based on the param passed in
   * @param row - csv row
   * @return - list of strings, equal to the row
   * @throws FactoryFailureException
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }

}