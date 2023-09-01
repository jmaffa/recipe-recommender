package recommender.csvhandler;

import java.util.List;

/**
 * Exception thrown when a Factory class fails.
 * */
public class FactoryFailureException extends Exception {
  final List<String> row;

  /**
   * The constructor of the factory failure exception class
   * @param row - csvfile row
   */
  public FactoryFailureException(List<String> row) {
    this.row = row;
  }
}