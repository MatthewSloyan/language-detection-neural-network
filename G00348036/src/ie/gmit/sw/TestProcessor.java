package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

public class TestProcessor {
	
	private int n = 4;
	private double[] vector;

	public TestProcessor(int vectorInput, int n) {
		super();
		this.vector = new double[vectorInput];
		this.n = n;
	}

	public double[] getVector() {
		return vector;
	}
	
    public void processFile(File file) {
		
		try {
			// Read in File
			//BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			BufferedReader br = new BufferedReader(new FileReader(file)); 
			
			System.out.println("Test");
			
			String line = null;
			while((line = br.readLine()) != null) {
				System.out.println(line);
				processLine(line);
			}
			
			br.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void processLine(String line) throws Exception{

		try {
			line = line.toUpperCase();
			
			// Initialise vector to 0;
			//for (int i = 0; i < vector.length; i++) vector[i] = 0;
			
			for (int i = 0; i < line.length() - n; i += n) {
				CharSequence kmer = line.substring(i, i + n);
				System.out.println(kmer);
				int index = kmer.hashCode() % vector.length;
				vector[index]++;
			}
			
			vector = Utilities.normalize(vector, 0, 1);
			
		} catch (Exception e) {
		}
	}
}
