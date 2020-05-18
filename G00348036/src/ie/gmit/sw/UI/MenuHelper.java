package ie.gmit.sw.UI;

import java.io.File;
import java.util.Scanner;

import ie.gmit.sw.NeuralNetwork;
import ie.gmit.sw.TestProcessor;
import ie.gmit.sw.Utilities;

public class MenuHelper {
	
	private Scanner console = new Scanner(System.in);
	private int kmers;
	private int vectorSize;
	
	public int getKmers() {
		return kmers;
	}
	
	public int getVectorSize() {
		return vectorSize;
	}

	public void saveNetwork() {
		boolean isValid;
		
		// Save
		do {
			System.out.println("\nWould you like to save the neural network:\n (1) Yes\n (2) No");
			String option = console.next();
			
			isValid = true;
			
			if (Integer.parseInt(option) == 1) {
				// Save Network
				System.out.println("Please enter the name of the Neural Network file (Extension is not required).");
				String nnFilePath = console.nextLine();
				
				Utilities.saveNeuralNetwork(NeuralNetwork.getInstance().getNetwork(), "./" + nnFilePath + ".nn");
				
				isValid = false;
			} else if (Integer.parseInt(option) == 2) {
				isValid = false;
			} else {
				System.out.println("Invalid option, please try again.");
			} 
		} while (isValid);
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
	}
	
	public void predictLanguageString() {
		console.nextLine();
		
		System.out.println("Please enter paragraph or sample of language you would like to predict.");
		String userInput = console.nextLine();
		
		TestProcessor processor = new TestProcessor(vectorSize, kmers);
		
		try {
			processor.processLine(userInput);
		} catch (Exception e) {
			System.out.println("Error occured processing string. Please try again.");
		}
		
		String prediction = NeuralNetwork.getInstance().predict(processor.getVector());
	
		System.out.println("The Predicted language is: " + prediction + "\n");
	}
	
	public void predictLanguageFile() {
		console.nextLine();
		
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
				
				processor.processFile(f);
				
				String prediction = NeuralNetwork.getInstance().predict(processor.getVector());
			
				System.out.println("The Predicted language is: " + prediction + "\n");
			} else {
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
	}
}
