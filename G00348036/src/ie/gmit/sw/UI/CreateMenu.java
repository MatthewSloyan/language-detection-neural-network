package ie.gmit.sw.UI;

import java.io.File;
import java.util.Scanner;

import ie.gmit.sw.NeuralNetwork;
import ie.gmit.sw.TestProcessor;
import ie.gmit.sw.Utilities;
import ie.gmit.sw.VectorProcessor;

/**
* Displays and handles UI options for creating neural network.
*
* @author Matthew Sloyan
*/
public class CreateMenu {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;
	private int fileOrString = 0; // String = 1, File = 2
	private int kmers;
	private int vectorSize;
	
	private NeuralNetwork nn;

	public void display() {
		
		// Create new 
		do
		{
        	System.out.println("\nPlease enter the number of kmers to use between 1 and 4. (4 is recommended)");
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
		
		try {
			new VectorProcessor(vectorSize, kmers).processFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		nn = new NeuralNetwork(vectorSize);
		nn.startTraining();
		
		// Menu
		do {
			System.out.println("\nPlease select an option:\n"
					+ "(1) View Topology Structure\n "
					+ "(2) View Accuracy & Run Tests\n"
					+ "(3) Predict Language from String Input\n"
					+ "(4) Predict Language from File\n"
					+ "(5) Return to main menu.");
			String option = console.next();
			
			isValid = true;
			
			switch (Integer.parseInt(option))
			{
				case 1:
					//nn.viewTopology();
					break;
				case 2:
					nn.startTests();
					break;
				case 3:
					predictLanguageString();
					break;
				case 4:
					predictLanguageFile();
					break;
				case 5:
					// Exit and return to main menu.
					isValid = false;
					break;
				default:
					System.out.println("Invalid input, please try again.");
			} // menu selection switch
			
		} while (isValid);
	}

	private void predictLanguageString() {
		console.nextLine();
		
		System.out.println("Please enter paragraph or sample of language you would like to predict.");
		String userInput = console.nextLine();
		
		TestProcessor processor = new TestProcessor(vectorSize, kmers);
		
		try {
			processor.processLine(userInput);
		} catch (Exception e) {
			System.out.println("Error occured processing string. Please try again.");
		}
		
		String prediction = nn.predict(processor.getVector());
	
		System.out.println("The Predicted language is: " + prediction);
	}

	private void predictLanguageFile() {
		console.nextLine();
		
		File f;
		
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
				
				String prediction = nn.predict(processor.getVector());
			
				System.out.println("The Predicted language is: " + prediction);
			} else {
				console.nextLine();
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
		
//		System.out.println("Please enter path to text file for prediction.");
//		predictionFilePath = console.next();
		
		//System.out.println("Your file path: " + predictionFilePath);
		
	}
}
