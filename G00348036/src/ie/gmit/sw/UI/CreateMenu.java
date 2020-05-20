package ie.gmit.sw.ui;

import java.util.Scanner;

import ie.gmit.sw.nn.NeuralNetworkFactory;
import ie.gmit.sw.nn.NeuralNetworkFunctions;
import ie.gmit.sw.nn.NeuralNetworkable;
import ie.gmit.sw.nn.Topology;
import ie.gmit.sw.processor.TrainingProcessor;

/**
* Displays and handles UI options for creating neural network.
*
* @author Matthew Sloyan
*/
public class CreateMenu implements Menuable {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;

	private NeuralNetworkFactory factory = NeuralNetworkFactory.getInstance();
	private NeuralNetworkable nn;
	private NeuralNetworkFunctions functions;

	/**
	* Displays all functions and menu to the user when creating a new neural network.
	* UIFunctions handles all UI methods required and stores all user input variables required (ngrams and vectorSize).
	* NeuralNetworkFactory is used to create an instance of the CrossValidationNeuralNetwork. 
	* More of these could easily be added by adding another UI option to select network type.
	*
	* @see UIFunctions
	* @see NeuralNetworkFactory
	* @see NeuralNetworkable
	*/
	public void display() {
		// Create new MenuHelper, this contains all UI methods 
		// and instance variables for ngrams and vector input sizes.
		UIFunctions ui = new UIFunctions();
		ui.setNgramSizeUI();
		ui.setVectorSizeUI();
		
		// Get instance of NeuralNetwork.
		nn = factory.getNeuralNetwork("CV");
		
		// Set topology
		Topology topology = new Topology(ui.getVectorSize(), 0, 235);
		topology.setHiddenLayerSize();
		nn.setTopology(topology);
		
		// Process training data set, and start training.
		try {
			new TrainingProcessor(ui.getVectorSize(), ui.getKmers()).processFile("./wili-2018-Small-11750-Edited.txt");
			nn.configureNetwork();
			nn.startTraining();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		// Create a new functions object, this holds all common functionality to a neural network
		// such as testing, predicting and viewing the topology. This allows it to be used with any network.
		functions = new NeuralNetworkFunctions(nn);
		
		// Menu
		do {
			System.out.println("\nPlease select an option:\n"
					+ "(1) View Topology Structure\n"
					+ "(2) View Accuracy, Confusion Matrix & Run Tests\n"
					+ "(3) Save Network\n"
					+ "(4) Predict Language from String Input\n"
					+ "(5) Predict Language from File\n"
					+ "(6) Return to main menu.");
			String option = console.next();
			
			isValid = true;
			
			switch (Integer.parseInt(option))
			{
				case 1:
					functions.viewTopology();
					break;
				case 2:
					functions.startTests();
					break;
				case 3:
					ui.saveNetwork(nn);
					break;
				case 4:
					// Pass in instance of NeuralNetworkable which holds network instance variable.
					ui.predictLanguageString(functions);
					break;
				case 5:
					ui.predictLanguageFile(functions);
					break;
				case 6:
					// Exit and return to main menu.
					isValid = false;
					break;
				default:
					System.out.println("Invalid input, please try again.");
			} // menu selection switch
			
		} while (isValid);
	}
}
