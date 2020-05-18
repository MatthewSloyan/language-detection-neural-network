package ie.gmit.sw.ui;

import java.util.Scanner;

/**
* Displays and handles UI options for loading neural network.
*
* @author Matthew Sloyan
*/
public class LoadMenu {
	
	private Scanner console = new Scanner(System.in);
	private boolean isValid;

	public void display() {
		
		UIFunctions ui = new UIFunctions();
		
		ui.loadNetwork();
		
		ui.setNgramSizeUI();
		ui.setVectorSizeUI();
		
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
					ui.predictLanguageString();
					break;
				case 2:
					ui.predictLanguageFile();
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
