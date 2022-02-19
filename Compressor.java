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
	}

	final int SYMBOL_LENGTH = 8;
	HashMap<String, String> symbolCodeMap = new HashMap<>();
	HashMap<String, Double> symbolFrequencies = new HashMap<>();


	/**
	 * Builds a symbol code map based on the contents of a text file containing binary.
	 * @param fileContents The text file to compress.
	 */
	Compressor(String fileContents) {
		PriorityQueue<Node> nodeQueue = new PriorityQueue<>(new NodeComparator());
		double totalSymbols = fileContents.length() / SYMBOL_LENGTH;
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

		//update frequencies from count to relative frequency
		//add symbols and frequencies as nodes to the priority queue
		for(String key : symbolFrequencies.keySet()){
			symbolFrequencies.put(key, symbolFrequencies.get(key) / totalSymbols);
			nodeQueue.add(new Node(symbolFrequencies.get(key), key));
		}

		//build tree using priority queue
		while (nodeQueue.size() > 1){
			nodeQueue.add(new Node(nodeQueue.poll(), nodeQueue.poll()));
		}
		rootNode = nodeQueue.poll();

		// Create encoding map
		fillSymbolCodeMap(rootNode, "");
	}

	/**
	 * Generates Hoffman codes based on symbol frequencies.
	 * @param root The root-node of a tree.
	 * @param code Empty string that gets populated recursively.
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


	/**
	 * Prints out each symbol and it's Hoffman code.
	 */
	public void printSymbolCodeMap() {
		for(String symbol : symbolCodeMap.keySet()){
			System.out.printf("Symbol: %s, Code: %s\n", symbol, symbolCodeMap.get(symbol));
		}
	}

	/**
	 * Returns a string compressed with Hoffman encoding.
	 * @param fileContents The data to compress.
	 * @return The compressed data.
	 */ 
	public String compressFileContents(String fileContents) {
		String compressedString = "";

		for (int i = 0; i < fileContents.length(); i = i + SYMBOL_LENGTH){
			String symbol = fileContents.substring(i, i + SYMBOL_LENGTH);
			compressedString += symbolCodeMap.get(symbol);
		}

		return compressedString;
	}


	/**
	 * Returns the sum of expected code lengths per symbol.
	 * @return The sum of expected code lengths per symbol.
	 */
	public double expectedCodeLengthPerSymbol() {
		double sum = 0.0;

		for(String key : symbolFrequencies.keySet()){
			sum += symbolFrequencies.get(key) * symbolCodeMap.get(key).length();
		}

		return sum;
	}


	/**
	 * Represents a node in a tree.
	 */
	class Node{
		double freq;
		String symbol;
		Node left;
		Node right;

		/**
		 * Constructs a new leaf node.
		 * @param frequency The frequency of the symbol.
		 * @param symbol The symbol.
		 */
		Node(double frequency, String symbol){
			this.freq = frequency;
			this.symbol = symbol;
			left = null;
			right = null;
		}

		/**
		 * Constructs a new parent node from two leafs.
		 * @param left Left leaf node.
		 * @param right Right leaf node.
		 */
		Node(Node left, Node right){
			this.freq = left.freq + right.freq;
			symbol = null;
			this.left = left;
			this.right = right;
		}
	}

	/**
	 * Compares two nodes based on their frequency.
	 */
	class NodeComparator implements Comparator<Node> {
		@Override
		public int compare(Node n1, Node n2) {
			if (Math.abs(n1.freq - n2.freq) < 10e-15){
				return 0;
			}
			else {
				return n1.freq - n2.freq > 0 ? 1 : -1;
			}
		}
	}
}