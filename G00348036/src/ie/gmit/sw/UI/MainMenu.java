package ie.gmit.sw.UI;
import java.io.IOException;
import java.util.*;

public class MainMenu {
	private Scanner console = new Scanner(System.in);
	private String text = "Please select an option:\n (1) Create new Neural Network\n (2) Load Existing Neural Network\n (3) Exit Program";
	boolean keepRunning = true;
	
	public void show() {
		System.out.println("====== Language Detection Application ====== ");
 		
 		//Running time: O(N);
		while(keepRunning) {
			System.out.println(text);
			String option = console.next();
			process(option);
		}
	}

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
				default:
					keepRunning = false;
			} // menu selection switch
		}
		
		catch (Exception e) {
		  System.out.println("Error, please try again.");//print out exception message
		}
	}
}
