package ie.gmit.sw.processor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import ie.gmit.sw.nn.Utilities;

/**
 * Class used to process a piece of text from a text file and add it to a vector array.
 * Also can add a single string to vector array for use in UI.
 * 
 * @author Matthew
 */
public class TestProcessor implements Processable{
	
	private int ngramSize;
	private double[] vector;

	public TestProcessor(int vectorInput, int ngramSize) {
		super();
		this.vector = new double[vectorInput];
		this.ngramSize = ngramSize;
	}

	public double[] getVector() {
		return vector;
	}
	
	/**
	 * Method that can take in any text file, and parse it line by line to add to the vector only.
	 * The functionality is similar to TraninProcessor but I felt having two separate classes improved simplicity.
	 */
    public void processFile(String fileName) {
		
		try {
			// Read in File
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			
			String line = null;
			while((line = br.readLine()) != null) {
				processLine(line);
			}
			
			br.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
    /**
	 * Method that parses each line.
	 * This can be reused for predicting a string.
	 * First get string and pre-process.
	 * Get the ngrams determined by the user. (2 is recommended from testing.)
	 * And hash each ngram into the vector array using the formula "kmer.hashCode() % vector.length"
	 * Normalize the data between 0 and 1 for consistent results.
	 */
	public void processLine(String line){

		try {
			line = line.toLowerCase();
			
			for (int i = 0; i < line.length() - ngramSize; i += ngramSize) {
				CharSequence kmer = line.substring(i, i + ngramSize);
				//System.out.println(kmer);
				int index = kmer.hashCode() % vector.length;
				vector[index]++;
			}
			
			vector = Utilities.normalize(vector, 0, 1);
			
		} catch (Exception e) {
		}
	}
}
