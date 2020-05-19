package ie.gmit.sw.ui;

import java.io.File;
import java.util.Scanner;

import org.encog.neural.networks.BasicNetwork;

import ie.gmit.sw.nn.CrossValidationNeuralNetwork;
import ie.gmit.sw.nn.NeuralNetworkable;
import ie.gmit.sw.nn.Utilities;
import ie.gmit.sw.processor.TestProcessor;

public class UIFunctions {
	
	private Scanner console = new Scanner(System.in);
	private int kmers;
	private int vectorSize;
	
	public int getKmers() {
		return kmers;
	}
	
	public int getVectorSize() {
		return vectorSize;
	}

	public void saveNetwork(BasicNetwork basicNetwork) {
		boolean isValid;
		
		// Save
		do {
			System.out.println("\nWould you like to save the neural network:\n (1) Yes\n (2) No");
			String option = console.next();
			
			isValid = true;
			
			if (Integer.parseInt(option) == 1) {
				// Save Network
				System.out.println("Please enter the name of the Neural Network file (Extension is not required).");
				String nnFilePath = console.next();
				
				Utilities.saveNeuralNetwork(basicNetwork, "./" + nnFilePath + ".nn");
				
				isValid = false;
			} else if (Integer.parseInt(option) == 2) {
				isValid = false;
			} else {
				System.out.println("Invalid option, please try again.");
			} 
		} while (isValid);
		
		console.nextLine();
	}
	
	public void loadNetwork(NeuralNetworkable nn) {
		boolean isValid;
		
		do {
			System.out.println("Please enter path to Neural Network file.");
			String nnFilePath = console.next();

			File f = new File(nnFilePath);
			isValid = true;
		
			//check if file exists, keeps asking till it is valid
			if (f.exists()) {
				isValid = false;
			
				nn.setNetwork(Utilities.loadNeuralNetwork(nnFilePath));
			} else {
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
		
		console.nextLine();
	}
	
	public void setNgramSizeUI() {
		boolean isValid;
		
		do
		{
        	System.out.println("\nPlease enter the number of ngrams to use between 1 and 4. (2 is recommended)");
            String kmerInput = console.next();
            
            isValid = true;
            try {
                kmers = Integer.parseInt(kmerInput);
                
                if (kmers < 1 || kmers > 4) {
                	System.out.println("Invalid selection, kmers must be between 1 and 4. Please try again.");
                }
                else {
                	isValid = false;
                }
			} catch (Exception e) {
				System.out.println("Input must be numbers. Please try again.");
			}
		} while (isValid);
		
		console.nextLine();
	}

	public void setVectorSizeUI() {
		boolean isValid;
		
		do
		{
			System.out.println("\nPlease enter the vector size. (1000 is recommended)");
            String vectorInput = console.next();
            
            isValid = true;
            try {
                vectorSize = Integer.parseInt(vectorInput);
                
                if (vectorSize < 100 || vectorSize > 2000) {
                	System.out.println("Invalid selection, size must be between 100 and 2000. Please try again");
                }
                else {
                	isValid = false;
                }
			} catch (Exception e) {
				System.out.println("Input must be numbers. Please try again.");
			}
		} while (isValid);
	
		console.nextLine();
	}
	
	public void predictLanguageString(NeuralNetworkable nn) {
		
		System.out.println("Please enter paragraph or sample of language you would like to predict.");
		String userInput = console.nextLine();
		
		TestProcessor processor = new TestProcessor(vectorSize, kmers);
		
		try {
			processor.processLine(userInput);
		} catch (Exception e) {
			System.out.println("Error occured processing string. Please try again.");
		}
		
		String prediction = nn.predict(processor.getVector());
	
		System.out.println("\nThe Predicted language is: " + prediction);
	}
	
	public void predictLanguageFile(NeuralNetworkable nn) {
		File f;
		boolean isValid;
		
		do {
			System.out.println("Please enter path to text file for prediction.");
			String predictionFilePath = console.next();

			f = new File(predictionFilePath);
			isValid = true;
		
			//check if file exists, keeps asking till it is valid
			if (f.exists()) {
				isValid = false;
				
				TestProcessor processor = new TestProcessor(vectorSize, kmers);
				
				processor.processFile(predictionFilePath);
				
				String prediction = nn.predict(processor.getVector());
			
				System.out.println("\nThe Predicted language is: " + prediction);
			} else {
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
		
		console.nextLine();
	}
}
