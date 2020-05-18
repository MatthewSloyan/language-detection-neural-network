package ie.gmit.sw.UI;

import java.io.File;
import java.util.Scanner;

import ie.gmit.sw.NeuralNetwork;
import ie.gmit.sw.Utilities;

/**
* Displays and handles UI options for loading neural network.
*
* @author Matthew Sloyan
*/
public class LoadMenu {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;
	
	private String nnFilePath;
	public void display() {
		
		MenuHelper ui = new MenuHelper();
		ui.setNgramSizeUI();
		ui.setVectorSizeUI();
		
		do {
			System.out.println("Please enter path to Neural Network file.");
			nnFilePath = console.nextLine();

			File f = new File(nnFilePath);
			isValid = true;
		
			//check if file exists, keeps asking till it is valid
			if (f.exists()) {
				isValid = false;
			
				NeuralNetwork.getInstance().setNetwork(Utilities.loadNeuralNetwork(nnFilePath));
			} else {
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
	}
}
