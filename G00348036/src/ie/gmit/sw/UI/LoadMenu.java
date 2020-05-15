package ie.gmit.sw.UI;

import java.io.File;
import java.util.Scanner;

/**
* Displays and handles UI options for loading neural network.
*
* @author Matthew Sloyan
*/
public class LoadMenu {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;
	private int fileOrString = 0; // String = 1, File = 2
	
	private String nnFilePath;
	private String predictionFilePath;
	
	public void display() {
		
		do {
			System.out.println("Please enter path to Neural Network file.");
			nnFilePath = console.nextLine();

			File f = new File(nnFilePath);
			isValid = true;
		
			//check if file exists, keeps asking till it is valid
			if (f.exists()) {
				isValid = false;
			} else {
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
		
		do {
			System.out.println("\nPlease select an option:\n "
					+ "(1) View Topology Structure\n "
					+ "(2) Predict language from file\n"
					+ "(1) Predict language with text input\n "
					+ "(2) Predict language from file\n"
					+ "(6) Return to main menu.");
			String option = console.nextLine();
			
			isValid = true;
			
			if (Integer.parseInt(option) == 1) {
				do {
					System.out.println("Please paragraph or sample of language you would like to predict.");
					String queryFileURL = console.nextLine();

					File f = new File(queryFileURL);
					isValid = true;
				
					//check if file exists, keeps asking till it is valid
					if (f.exists()) {
						isValid = false;
						fileOrString = 1;
					} else {
						System.out.println("File does not exist, please try again.");
					}
				} while (isValid);
			} else if (Integer.parseInt(option) == 2) {
				do {
					System.out.println("Please enter path to text file for prediction.");
					predictionFilePath = console.nextLine();

					File f = new File(predictionFilePath);
					isValid = true;
				
					//check if file exists, keeps asking till it is valid
					if (f.exists()) {
						isValid = false;
					} else {
						System.out.println("File does not exist, please try again.");
					}
				} while (isValid);
			} else {
				isValid = false;
			} 
		} while (isValid);
	}
}
