package recommender;


import recommender.slopeOne.mocks.OldREPL;
import weather.exceptions.BadJsonException;

/**
 * Runs the algorithm from terminal. Used during development phase
 * No longer in use after integration (!!)
 */
public class RunAlgorithm {

  /**
   * Main method that runs the repl
   * @param args - taken from the terminal
   */
  public static void main(String[] args) throws BadJsonException {
    REPL repl = new REPL();
    repl.run();
  }
}