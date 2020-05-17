package ie.gmit.sw;

import java.io.IOException;
import java.util.Scanner;

import ie.gmit.sw.UI.Menu;

public class Runner {
	public static void main(String[] args){
		
//		int inputs = 1000;
//		
//		try {
//			new VectorProcessor(inputs, 4).processFile();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		new NeuralNetwork(inputs);
		
		new Menu().show();
		
//		Scanner s = new Scanner(System.in);
//		while (keepRunning) {
//			System.out.println("Choice");
//			System.out.println("1: Enter n-Gram Number(s)");
//			System.out.println("2: Enter Vector size");
//			int choice = Integer.parseInt(s.next());
//			
//			switch (choice) {
//			case 1:
//				break;
//
//			default:
//				throw new IllegalArgumentException("Unexpected value: " + key);
//			}
//			
//		}
		
		/*			
			Each of the languages in the enum Language can be represented as a number between 0 and 234. You can 
			map the output of the neural network and the training data label to / from the language using the
			following. Eg. index 0 maps to Achinese, i.e. langs[0].  
		*/
//		Language[] langs = Language.values(); //Only call this once...		
//		for (int i = 0; i < langs.length; i++){
//			System.out.println(i + "-->" + langs[i]);
//		}
	}
}