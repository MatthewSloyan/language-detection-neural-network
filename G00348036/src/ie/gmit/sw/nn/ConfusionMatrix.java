package ie.gmit.sw.nn;

/**
* Class that is instantiated when testing neural network. It contains instance variables for TP, TN, FP and FN.
* These values are updated on each call of addToMatrix.
* 
* With these values the sensitivity (sn) = TP (TP + FN) and 
* specificity (sP) = TN / (TN + FP) can be calculated and displayed.
* 
* @author Matthew Sloyan
*/
public class ConfusionMatrix {
	
	private double truePositive = 0;
	private double trueNegative= 0;
	private double falsePositive = 0;
	private double falseNegative= 0;
	
	public double getTruePositive() {
		return truePositive;
	}
	public double getTrueNegative() {
		return trueNegative;
	}
	public double getFalsePositive() {
		return falsePositive;
	}
	public double getFalseNegative() {
		return falseNegative;
	}
	
	/**
	* TP, TN, FP and FN values are updated on each call depending on input.
	*/
	public void addToMatrix(int expected, int actual) {
		
		if (expected == 1 && actual == 1) {
			truePositive++;
		}
		
		if (expected == 1 && actual != expected) {
			falsePositive++;
		}
	      
		if (expected == 0 && actual == 0) {
			trueNegative++;
		}
		   
		if (expected == 0 && actual != expected) {
			falseNegative++;
		}
	}
	
	/**
	* Method to print out sensitivity (sn) = TP (TP + FN) and 
	* specificity (sP) = TN / (TN + FP)
	*/
	public void printResults() {
		double sensitivity = truePositive * (truePositive + falseNegative);
		double specificity = trueNegative / (trueNegative + falsePositive);
		
		System.out.println("\nTP: " + truePositive);
		System.out.println("TN: " + trueNegative);
		System.out.println("FP: " + falsePositive);
		System.out.println("FN: " + falseNegative);
		
		System.out.println("\nSensitivity (sn): " + sensitivity);
		System.out.println("Specificity (sP): " + specificity);
	}
}
