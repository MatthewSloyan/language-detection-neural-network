package ie.gmit.sw.ui;
import java.util.*;

/**
* Main menu of application. This is created by the runner.
*
* @author Matthew Sloyan
*/
public class MainMenu implements Menuable {
	private Scanner console = new Scanner(System.in);
	private String text = "\nPlease select an option:\n (1) Create new Neural Network\n (2) Load Existing Neural Network\n (3) View Recommendations\n (4) Exit Program";
	boolean keepRunning = true;
	
	/**
	* Outer loop which keeps menu running until 4 or any other number is entered.
	*/
	public void display() {
		System.out.println("====== Language Detection Application ======");
 		
 		//Running time: O(N);
		while(keepRunning) {
			System.out.println(text);
			String option = console.next();
			process(option);
		}
	}

	/**
	* Menu processor which creates objects depending on selection.
	*/
	private void process(String option) {
		try {
			int selection = Integer.parseInt(option);
			
			switch (selection)
			{
				case 1:
					new CreateMenu().display();
					break;
				case 2:
					new LoadMenu().display();
					break;
				case 3:
					viewRecommendations();
					break;
				default:
					keepRunning = false;
			} // menu selection switch
		}
		
		catch (Exception e) {
		  System.out.println("Error, please try again.");//print out exception message
		}
	}

	/**
	* Recommendations for configuration based on testing.
	* More information can be found in README.
	*/
	private void viewRecommendations() {
		System.out.println("\n==== Recommendations ==== \n");
		System.out.println("Ngram Size: 2  |  Vector Size: 350  |  Training time: 45 seconds  |  Accuracy: 73%");
		System.out.println("Ngram Size: 2  |  Vector Size: 550  |  Training time: 65 seconds  |  Accuracy: 78%");
		System.out.println("Ngram Size: 2  |  Vector Size: 700  |  Training time: 90 seconds  |  Accuracy: 85%  RECOMMENDED");
		
		System.out.println("\nPlease Note: Training time and accuracy can vary between machine. Tests completed on a 7th Gen I7");
	}
}
