package ie.gmit.sw.ui;

import java.io.File;
import java.util.Scanner;

import ie.gmit.sw.nn.NeuralNetworkFunctions;
import ie.gmit.sw.nn.NeuralNetworkable;
import ie.gmit.sw.nn.Utilities;
import ie.gmit.sw.processor.TestProcessor;

/**
* Class that provides all UI functions and user inputs.
* All methods are reusable, for example another type of network could be loaded, saved or have a result predicted.
* All methods are validated, so that the user can only enter correct values, numbers or files that exist.
*
* @author Matthew Sloyan
*/
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

	/**
	* Method that allows the user to save their trained neural network.
	* It will work with any type due to NeuralNetworkable interface.
	* All inputs are validated so that a user can only select yes or no.
	*
	* @param nn instance of neural network type.
	* @see Utilities
	*/
	public void saveNetwork(NeuralNetworkable nn) {
		// Save Network
		System.out.println("Please enter the name of the Neural Network file (Extension is not required).");
		String nnFilePath = console.next();
		
		Utilities.saveNeuralNetwork(nn.getNetwork(), "./" + nnFilePath + ".nn");
		
		console.nextLine();
	}
	
	/**
	* Method that allows the user to load their trained neural network.
	* It will work with any type due to NeuralNetworkable interface.
	* All inputs are validated so that user can only select a valid file.
	*
	* @param nn instance of neural network type.
	* @see Utilities
	*/
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
				System.out.println("File does not exist, please try again.\n");
			}
		} while (isValid);
		
		console.nextLine();
	}
	
	/**
	* Method that gets the n-gram size from the user. (1, 2, 3, 4).
	* All inputs are validated so that user can input 1-4. 
	* 2 is recommended from testing.
	*/
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

	/**
	* Method that gets the vector input size from the user.
	* All inputs are validated so that user can input between 100 and 2000.
	* Around 600 is recommended from testing.
	*/
	public void setVectorSizeUI() {
		boolean isValid;
		
		do
		{
			System.out.println("\nPlease enter the vector size between 100 and 2000. (700 is recommended)");
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
	
	/**
	* Method that allows the user to predict a language result using a string.
	* It will work with any type due to NeuralNetworkable interface.
	* This method is used in CreateMenu and LoadMenu.
	* 
	* The line is processed into a vector and this vector is fed into the nn.
	*
	* @param nn instance of neural network type.
	* @see TestProcessor
	*/
	public void predictLanguageString(NeuralNetworkFunctions functions) {
		
		System.out.println("Please enter paragraph or sample of language you would like to predict.");
		String userInput = console.nextLine();
		
		// Setup processor with inputs and process string into vector.
		TestProcessor processor = new TestProcessor(vectorSize, kmers);
		
		try {
			processor.processLine(userInput);
		} catch (Exception e) {
			System.out.println("Error occured processing string. Please try again.\n");
		}
		
		String prediction = functions.predict(processor.getVector());
	
		System.out.println("\nThe Predicted language is: " + prediction);
	}
	
	/**
	* Method that allows the user to predict a language result from a file.
	* This file must be a text file and can contain any number of lines of text of one language.
	* It will work with any type due to NeuralNetworkable interface.
	* All inputs are validated so that user can only select a valid file.
	* This method is used in CreateMenu and LoadMenu.
	* 
	* The file is processed into a vector and this vector is fed into the nn.
	*
	* @param nn instance of neural network type.
	* @see TestProcessor
	*/
	public void predictLanguageFile(NeuralNetworkFunctions functions) {
		File f;
		boolean isValid;
		
		do {
			System.out.println("Please enter path to text file for prediction. E.g. Test.txt if in the same folder.");
			String predictionFilePath = console.next();

			f = new File(predictionFilePath);
			isValid = true;
		
			//check if file exists, keeps asking till it is valid
			if (f.exists()) {
				isValid = false;
				
				// Setup processor with inputs and process string into vector.
				TestProcessor processor = new TestProcessor(vectorSize, kmers);
				processor.processFile(predictionFilePath);
				
				String prediction = functions.predict(processor.getVector());
			
				System.out.println("\nThe Predicted language is: " + prediction);
			} else {
				System.out.println("File does not exist, please try again.\n");
			}
		} while (isValid);
		
		console.nextLine();
	}
}
