package recommender.csvhandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This is the CSVParser class which parses the CSVFile, which means it calculates the number of
 * words, characters, rows and columns in the file, and converts each row of the data set into an
 * object, as chosen by the user through the creator argument
 *
 * @param <T> - any type of object can be used, based on user's discretion
 */
public class CSVParser<T> {
  private Reader rdr;
  private int numChar;
  private int numWords;
  private int numRows;
  private int numCol;
  private List<T> parsedList;
  private CreatorFromRow<T> creator;
  private boolean header;
  private HashMap myMap;

  /**
   * The constructor which instantiates the instance variables
   *
   * @param rdr - any object which implements the Reader interface. Allows the user flexibility
   * @param creator - any object which implements the CreatorFromRow interface, which allows the
   *     user to choose the object that is created from rows of data
   * @param header - a boolean used as a flag to decide if the csv file should be parsed as if it
   *     has a header [true]/as if it does not have a header [false]
   * @throws IOException - thrown when an error occurs with the input/output streams of the Reader
   * @throws FactoryFailureException - thrown when the Factory class fails
   */
  public CSVParser(Reader rdr, CreatorFromRow<T> creator, boolean header)
      throws IOException, FactoryFailureException {
    this.rdr = rdr;
    this.creator = creator;
    this.numChar = 0;
    this.numWords = 0;
    this.numRows = 0;
    this.numCol = 0;
    this.parsedList = new ArrayList<>();
    this.header = header;
    this.myMap = new HashMap();

  }

  /**
   * The main utility method of the class, which assigns the value of row/col/word/char based on the
   * CSVFile parsed. This is also where each row of the CSVFile is converted into a new Object
   * (based on the creator passed in)
   *
   * @return - An ArrayList of the new objects created. Generalised to 'T' to allow user flexibiliyy
   * @throws IOException - thrown when an error occurs with the input/output streams of the Reader
   * @throws FactoryFailureException - thrown when the Factory class fails
   */
  public HashMap<String, List<String>> parse() throws IOException, FactoryFailureException {
    try {
      BufferedReader bufferedReader = new BufferedReader(rdr);
      String line;
      if (this.header) {
        line = bufferedReader.readLine();
        this.numRows++;
        this.numWords += this.countWords(line);
        this.numChar += this.countChar(line);
        this.numCol = this.countCol(line);
      }
      while ((line = bufferedReader.readLine()) != null) {
        this.numChar += this.countChar(line);
        this.numWords = this.numWords + this.countWords(line);
        this.numCol = this.countCol(line);
        this.numRows++;
        List<String> row = (List<String>) this.creator.create(this.stringToList(line));
        this.myMap.put(row.get(0), row.subList(1, row.size()-1));
      }
    } catch (IOException e) {
      System.err.println("ERROR: File not found!");
    }
    return this.myMap;
  }

  // does not count commas or whitespace, but counts all other punctuation.

  /**
   * Counts the number of characters in a string
   *
   * @param line - String that the user wants to know the number of characters of
   * @return - the integer value of the number of characters in that string
   */
  public int countChar(String line) {
    String noCommas = line.replace(",", "");
    String noSpaces = noCommas.replace(" ", "");
    return noSpaces.length();
  }

  /**
   * Counts the number of words in a string
   *
   * @param line - String that the user wants to know the number of words in
   * @return - the integer value of the number of words in that string
   */
  public int countWords(String line) {
    String noCommas = line.replace(',', ' ');
    StringTokenizer tokens = new StringTokenizer(noCommas);
    return tokens.countTokens();
  }

  /**
   * Counts the number of columns in a CSV file
   *
   * @param line - a row in the CSV fike
   * @return - the integer value of the number of columns in the file
   */
  public int countCol(String line) {
    String numOfCommas = line.replaceAll("[^,]", "");
    return numOfCommas.length() + 1;
  }

  /**
   * Converts a string of words into a list of strings
   *
   * @param row - the string you want to split into a list
   * @return - an ArrayList of strings
   */
  public List<String> stringToList(String row) {
    String[] strArray = row.split(",");
    List<String> stringList = Arrays.asList(strArray);
    return stringList;
  }



}