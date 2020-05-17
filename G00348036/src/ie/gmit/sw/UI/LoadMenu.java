package ie.gmit.sw.UI;

import java.io.File;
import java.util.Scanner;

import ie.gmit.sw.NeuralNetwork;

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
	
	private NeuralNetwork nn;
	
	public void display() {
		
		nn = new NeuralNetwork(1000);
		
		do {
			System.out.println("Please enter path to Neural Network file.");
			nnFilePath = console.nextLine();

			File f = new File(nnFilePath);
			isValid = true;
		
			//check if file exists, keeps asking till it is valid
			if (f.exists()) {
				isValid = false;
				
				nn.loadNetwork(nnFilePath);
			} else {
				System.out.println("File does not exist, please try again.");
			}
		} while (isValid);
		
	}
}
