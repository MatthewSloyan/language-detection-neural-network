package ie.gmit.sw.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import ie.gmit.sw.nn.Utilities;

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
