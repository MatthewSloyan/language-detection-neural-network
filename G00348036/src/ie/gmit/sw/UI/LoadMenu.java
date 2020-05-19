package ie.gmit.sw.ui;

import java.util.Scanner;

import ie.gmit.sw.nn.NeuralNetworkFactory;
import ie.gmit.sw.nn.NeuralNetworkable;

/**
* Displays and handles UI options for loading neural network.
*
* @author Matthew Sloyan
*/
public class LoadMenu implements Menuable {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;
	
	// Get instance of NeuralNetwork, and set input size.
	NeuralNetworkFactory factory = NeuralNetworkFactory.getInstance();
	private NeuralNetworkable nn;

	public void display() {
		
		// 
		nn = factory.getNeuralNetwork("CV");
				
		UIFunctions ui = new UIFunctions();
		ui.loadNetwork(nn);
		ui.setNgramSizeUI();
		ui.setVectorSizeUI();
		nn.setInputSize(ui.getVectorSize());
		
		// Menu
		do {
			System.out.println("\nPlease select an option:\n"
					+ "(1) Predict Language from String Input\n"
					+ "(2) Predict Language from File\n"
					+ "(3) Return to main menu.");
			String option = console.next();
			
			isValid = true;
			
			switch (Integer.parseInt(option))
			{
				case 1:
					ui.predictLanguageString(nn);
					break;
				case 2:
					ui.predictLanguageFile(nn);
					break;
				case 3:
					// Exit and return to main menu.
					isValid = false;
					break;
				default:
					System.out.println("Invalid input, please try again.");
			} // menu selection switch
			
		} while (isValid);
	}
}
