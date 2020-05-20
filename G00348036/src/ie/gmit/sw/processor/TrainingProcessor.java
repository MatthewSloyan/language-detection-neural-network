package ie.gmit.sw.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import ie.gmit.sw.nn.Utilities;

/**
 * Class used to process training data from text file and write results to CSV file.
 * Also hashes file into a vector which is written to file with correct results.
 * 
 * @author Matthew
 */
public class TrainingProcessor implements Processable{

	private int ngramSize;
	private double[] vector;
	
	// Format number to 3 decimal places.
	private DecimalFormat df = new DecimalFormat("###.###");
	private Language[] langs = Language.values();
	private FileWriter fw;
	
	public TrainingProcessor(int vectorInput, int ngramSize) {
		super();
		this.vector = new double[vectorInput];
		this.ngramSize = ngramSize;
	}
	
	/**
	 * Method that can take in any text file, and parse it line by line to be written to a CSV file.
	 */
	public void processFile(String fileName){
		
		//"./wili-2018-Small-11750-Edited.txt"
		
		try {
			// Read in Wili Text file.
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));

			// Create new file to write CSV data to.
			fw = new FileWriter("./data.csv");

			String line = null;
			while((line = br.readLine()) != null) {
				// Process data.
				processLine(line);
				// Track total
			}
			
			// Close Wili and 
			br.close();
			fw.close();
			
		} catch (Exception e) {
			System.out.println("Error occured: Please make sure the Wili text file is in the same directory.");
		}
	}
	
	/**
	 * Method that parses each line.
	 * First get string and language and pre-processes the string.
	 * Initialize the vector input with 0's.
	 * Get the ngrams determined by the user. (2 is recommended from testing.)
	 * And hash each ngram into the vector array using the formula "kmer.hashCode() % vector.length"
	 * Normalize the data between 0 and 1 for consistent results.
	 * Append vector to CSV file along with an array of 0's, and a 1 in the index of the correct language.
	 * This vector is used as the training data, and the array as the tests.
	 */
	public void processLine(String line){

		try {
			String[] record = line.split("@");
			
			if (record.length > 2) return; // Any bad lines of text.
			
			// Remove all punctuation and numbers from dataset.
			String text = record[0].replaceAll("\\p{P}", "").toLowerCase();
			text = text.replaceAll("\\d","");
			
			String lang = record[1];
			
			// Initialize vector to 0;
			for (int i = 0; i < vector.length; i++) vector[i] = 0;
			
			for (int i = 0; i < text.length() - ngramSize; i += ngramSize) {
				CharSequence kmer = text.substring(i, i + ngramSize);
				int index = kmer.hashCode() % vector.length;
				vector[index]++;
			}
			
			// Normalize the vector between 0 and 1, and write to csv file.
			vector = Utilities.normalize(vector, 0, 1);
			
			for (int i = 0; i < vector.length; i++) {
				fw.append(df.format(vector[i]));
	            fw.append(',');
			}
			
			// Get the index of the language 
			// Adapted from: https://stackoverflow.com/questions/15436721/get-index-of-enum-from-string
			int index = Language.valueOf(lang).ordinal();
			
			// Append 1 and 0s. If 1 then it is the language.
			for (int i = 0; i < langs.length; i++){
				if (i == index) {
					fw.append('1');
				}
				else {
					fw.append('0');
				}
				fw.append(',');
			}
			
			fw.append('\n');
			
		} catch (Exception e) {
		}
	}
}
