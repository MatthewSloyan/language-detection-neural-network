package ie.gmit.sw.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import ie.gmit.sw.nn.Utilities;

public class TrainingProcessor implements Processable{

	private int ngramSize;
	private double[] vector;
	
	private DecimalFormat df = new DecimalFormat("###.###");
	private Language[] langs = Language.values();
	private FileWriter fw;
	
	public TrainingProcessor(int vectorInput, int ngramSize) {
		super();
		this.vector = new double[vectorInput];
		this.ngramSize = ngramSize;
	}
	
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
	
	public void processLine(String line){

		try {
			String[] record = line.split("@");
			
			if (record.length > 2) return; // Any bad lines of text.
			
			String text = record[0].toLowerCase();
			String lang = record[1];
			
			// Initialise vector to 0;
			for (int i = 0; i < vector.length; i++) vector[i] = 0;
			
			for (int i = 0; i < text.length() - ngramSize; i += ngramSize) {
				CharSequence kmer = text.substring(i, i + ngramSize);
				int index = kmer.hashCode() % vector.length;
				vector[index]++;
			}
			
			// 3 and 4 kmers
//			for (int i = 1; i <= 4; i++) {
//				for (int j = 0; j < text.length() - i; j += i) {
//					CharSequence kmer = text.substring(j, j + i);
//					int index = kmer.hashCode() % vector.length;
//					vector[index]++;
//				}
//			}
			
			vector = Utilities.normalize(vector, 0, 1);
			
			for (int i = 0; i < vector.length; i++) {
				fw.append(df.format(vector[i]));
	            fw.append(',');
			}
			
			// Get the index of the language 
			// Adapted from: https://stackoverflow.com/questions/15436721/get-index-of-enum-from-string
			int index = Language.valueOf(lang).ordinal();
			
			// Append 1 and 0s
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
		
		// Loop over "text"
		// For each n-gram
		// compute
			// index = ngram.hashCode() % vector.length
		//vector[index]++
//		for (int i = 1; i <= n; i++) {
//			for (int j = 0; j < record[0].length() - i; j++) {
//				CharSequence kmer = record[0].substring(j, j + i);
//				int index = kmer.hashCode() % vector.length;
//				vector[index]++;
//			}
//		}
	}
}
