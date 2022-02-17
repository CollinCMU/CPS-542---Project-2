import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;


/**
 * Reads in binary from a file called 'File.txt'.
 * Determines symbol frequencies and generates Hoffman codes.
 * Compresses the original file with Hoffman encoding.
 * 
 * @author vanbr1c, tallu1r, uniss1sh, rojan1a
 * @since 2-16-2022
 */
public class Compressor {

	public static void main(String[] args) {
		String fileContents = "";
		
		try {
			Scanner scnr = new Scanner(new File("File.txt"));
			fileContents = scnr.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Compressor c = new Compressor(fileContents);
		c.printSymbolCodeMap();
		
		String compressedFileContents = c.compressFileContents(fileContents);
    System.out.println(fileContents.length());
		System.out.println(compressedFileContents.length());
	}
	
	final int SYMBOL_LENGTH = 8;
	HashMap<String, String> symbolCodeMap = new HashMap<>();
	HashMap<String, Double> symbolFrequencies = new HashMap<>();
	
	// The constructor should build a symbol to code map based on the 
	//  symbol frequencies in the fileContents provided.
	// Chunk through the file with lengths of SYMBOL_LENGTH;
	Compressor(String fileContents) {
    PriorityQueue<Node> nodeQueue = new PriorityQueue<>((n1, n2) -> (n1.freq < n2.freq) ? -1 : 1);
    Node rootNode;

		// Determine symbol frequencies
    for (int i = 0; i < fileContents.length(); i = i + SYMBOL_LENGTH){
      String symbol = fileContents.substring(i, i + SYMBOL_LENGTH);

      //if already in hashmap, update value
      if (symbolFrequencies.containsKey(symbol)){
        symbolFrequencies.put(symbol, symbolFrequencies.get(symbol) + 1.0);
      }
      //add to HashMap
      else{
        symbolFrequencies.put(symbol, 1.0);
      }
    }

		//add symbols and frequencies as nodes to the priority queue
    for(String key : symbolFrequencies.keySet()){
      nodeQueue.add(new Node(symbolFrequencies.get(key), key));
    }

    //build tree
    while (nodeQueue.size() > 1){
      Node nodeOne = nodeQueue.poll();
      Node nodeTwo = nodeQueue.poll();
      Node combineNode = new Node(nodeOne, nodeTwo);
      nodeQueue.add(combineNode);
    }
    rootNode = nodeQueue.poll();

		// Create encoding map
    fillSymbolCodeMap(rootNode, "");
	}
	
  /**
  *
  *
  */
  public void fillSymbolCodeMap(Node root, String code){
    if (root.left == null && root.right == null){
      symbolCodeMap.put(root.symbol, code);
    }
    else{
      fillSymbolCodeMap(root.left, code + "0");
      fillSymbolCodeMap(root.right, code + "1");
    }
  }

	//  Prints out each symbol with its code
	public void printSymbolCodeMap() {
		for(String symbol : symbolCodeMap.keySet()){
      System.out.printf("Symbol: %s, Code: %s\n", symbol, symbolCodeMap.get(symbol));
    }
	}

	/**
  *
  *
  */ 
	public String compressFileContents(String fileContents) {
		String compressedString = "";

    for (int i = 0; i < fileContents.length(); i = i + SYMBOL_LENGTH){
      String symbol = fileContents.substring(i, i + SYMBOL_LENGTH);
      compressedString += symbolCodeMap.get(symbol);
    }

		return compressedString;
	}
	
	// Using the frequencies of the symbols and lengths of their associated
	//  codeword, calculate the expected code length per symbol in fileContents
 	public double expectedCodeLengthPerSymbol() {
 		
 		return 0;
 	}
 	
   
   /**
   *
   *
   */
  class Node{
    double freq;
    String symbol;
    Node left;
    Node right;

    Node(double frequency, String symbol){
      this.freq = frequency;
      this.symbol = symbol;
      left = null;
      right = null;
    }

    Node(Node left, Node right){
      this.freq = left.freq + right.freq;
      symbol = null;
      this.left = left;
      this.right = right;
    }
  }
}