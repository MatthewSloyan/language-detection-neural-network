package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Arrays;

public class VectorProcessor {
	private double[] vector = new double[100];
	private DecimalFormat df = new DecimalFormat("###.###");
	private int n = 4;
	private Language[] langs = Language.values();
	private FileWriter fw;
	
	public void go() throws Exception {
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File("./wili-2018-Small-11750-Edited.txt"))));
					
			fw = new FileWriter("./data.csv");

			String line = null;
			while((line = br.readLine()) != null) {
				process(line);
				// Track total
			}
			
			br.close();
			fw.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void process(String line) throws Exception{

		try {
			String[] record = line.split("@");
			
			if (record.length > 2) return; // Any bad lines of text.
			
			String text = record[0].toUpperCase();
			String lang = record[1];
			
			// Initialise vector to 0;
			for (int i = 0; i < vector.length; i++) vector[i] = 0;
			
			for (int i = 0; i < text.length() - n; i += n) {
				CharSequence kmer = text.substring(i, i + n);
				int index = kmer.hashCode() % vector.length;
				//System.out.println(kmer);
				vector[index]++;
			}
			
			vector = Utilities.normalize(vector, -1, 1);
			
			for (int i = 0; i < vector.length; i++) {
				fw.append(df.format(vector[i]));
	            fw.append(',');
			}
			
			// Language
			//int index = Arrays.asList(langs).indexOf(lang);
			
			// Get the index of the language 
			// Adapted from: https://stackoverflow.com/questions/15436721/get-index-of-enum-from-string
			int index = Language.valueOf(lang).ordinal();
			//System.out.println(index);
			
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
		
		
		// write out the vector to a CSV file using df.format(number) for each vector index
		// write out the language numbers to the same row in CSV file.
	
		// vector.length + #labels
	}

}
