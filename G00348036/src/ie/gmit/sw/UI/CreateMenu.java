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
	private NeuralNetwork nn;

	public void display() {
		
		// Create new MenuHelper, this contains all UI methods 
		// and instance variables for ngrams and vector input sizes.
		MenuHelper ui = new MenuHelper();
		ui.setNgramSizeUI();
		ui.setVectorSizeUI();
		
		// Get instance of NeuralNetwork, and set input size.
		nn = NeuralNetwork.getInstance();
		nn.setInputSize(ui.getVectorSize());
		
		// Process traing data set, and start training.
		try {
			new VectorProcessor(ui.getVectorSize(), ui.getKmers()).processFile();
			nn.startTraining();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// Ask the user if they'd like to save the neural network.
		ui.saveNetwork();
		
		// Menu
		do {
			System.out.println("\nPlease select an option:\n"
					+ "(1) View Topology Structure\n"
					+ "(2) View Accuracy & Run Tests\n"
					+ "(3) Predict Language from String Input\n"
					+ "(4) Predict Language from File\n"
					+ "(5) Return to main menu.");
			String option = console.next();
			
			isValid = true;
			
			switch (Integer.parseInt(option))
			{
				case 1:
					nn.viewTopology();
					break;
				case 2:
					nn.startTests();
					break;
				case 3:
					ui.predictLanguageString();
					break;
				case 4:
					ui.predictLanguageFile();
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
}
