package ie.gmit.sw.ui;

import java.util.Scanner;

import ie.gmit.sw.nn.NeuralNetworkFactory;
import ie.gmit.sw.nn.NeuralNetworkFunctions;
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
	private NeuralNetworkFunctions functions;

	/**
	* Displays all functions and menu to the user when loading a neural network.
	* UIFunctions handles all UI methods required and stores all user input variables required (ngrams and vectorSize).
	* NeuralNetworkFactory is used to create an instance of the CrossValidationNeuralNetwork. 
	* More of these could easily be added by adding another UI option to select network type.
	*
	* @see UIFunctions
	* @see NeuralNetworkFactory
	* @see NeuralNetworkable
	*/
	public void display() {
		
		// Create new instance of NN.
		nn = factory.getNeuralNetwork("CV");
				
		// Ask the user to specify neural network file, ngrams size and vector input size. 
		UIFunctions ui = new UIFunctions();
		ui.loadNetwork(nn);
		
		System.out.println("Please note: N-gram and Vector size must be the same as intitial setup of network.");
		ui.setNgramSizeUI();
		ui.setVectorSizeUI();
		
		// Create a new functions object, this holds all common functionality to a neural network
		// such as testing, predicting and viewing the topology. This allows it to be used with any network.
		functions = new NeuralNetworkFunctions(nn);
		
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
					// Pass in instance of NeuralNetworkable which holds network instance variable.
					ui.predictLanguageString(functions);
					break;
				case 2:
					ui.predictLanguageFile(functions);
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
