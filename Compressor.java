import java.io.File;
import java.io.FileNotFoundException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

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
		
	}
	
	final int SYMBOL_LENGTH = 8;
	HashMap<String, String> symbolCodeMap;
	HashMap<String, Double> symbolFrequencies;
	
	// The constructor should build a symbol to code map based on the 
	//  symbol frequencies in the fileContents provided.
	// Chunk through the file with lengths of SYMBOL_LENGTH;
	Compressor(String fileContents) {
		
    //initialize HashMaps
    symbolCodeMap = new HashMap<>();
    symbolFrequencies = new HashMap<>();
    final double symbolCount = fileContents.length() / SYMBOL_LENGTH;

		// Determine symbol frequencies
    //loop through the fileContents
    for (int i = 0; i < fileContents.length(); i = i + SYMBOL_LENGTH){
      String subString = fileContents.substring(i, i + SYMBOL_LENGTH);

      //if already in hashmap, update value
      if (symbolFrequencies.containsKey(subString)){
        symbolFrequencies.merge(subString, 1.0, (oldValue, newValue) -> oldValue + newValue);
      }
      //add to HashMap
      else{
        symbolFrequencies.put(subString, 1.0);
      }
    }

    //update HashMap values to the percentage frequency
    for (String key : symbolFrequencies.keySet()){
      symbolFrequencies.merge(key, 0.0, (oldValue, newValue) -> oldValue / symbolCount);
    }


		// Build code tree
		
		// Create encoding map
		
		
	}
	
	//  Prints out each symbol with its code
	public void printSymbolCodeMap() {
		
	}

	// 
	public String compressFileContents(String fileContents) {
		
		return null;
	}
	
	// Using the frequencies of the symbols and lengths of their associated
	//  codeword, calculate the expected code length per symbol in fileContents
 	public double expectedCodeLengthPerSymbol() {
 		
 		return 0;
 	}
 	
}

