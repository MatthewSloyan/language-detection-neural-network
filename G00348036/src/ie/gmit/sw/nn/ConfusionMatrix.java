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
		
		// TP, so neural network has guessed correctly
		if (expected == 1 && actual == 1) {
			truePositive++;
		}
		
		// FP, the actual is negative but the expected is positive.
		// It was actually not something but the nn thought it was.
		if (expected == 1 && actual != expected) {
			falsePositive++;
		}
	      
		// TN, so neural network has denied it correctly.
		if (expected == 0 && actual == 0) {
			trueNegative++;
		}
		   
		// FP, the actual is positive but the expected is negative.
		// It was actually something but the nn thought it was something else.
		if (expected == 0 && actual != expected) {
			falseNegative++;
		}
	}
	
	/**
	* Method to print out sensitivity (sn) = TP (TP + FN) and 
	* specificity (sP) = TN / (TN + FP)
	* Also calculate accuracy based on these values.
	*/
	public void printResults() {
		double sensitivity = truePositive * (truePositive + falseNegative);
		double specificity = trueNegative / (trueNegative + falsePositive);
		double accuracy = (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
		
		System.out.println("TP: " + truePositive);
		System.out.println("TN: " + trueNegative);
		System.out.println("FP: " + falsePositive);
		System.out.println("FN: " + falseNegative);
		
		System.out.println("\nSensitivity (sn): " + sensitivity);
		System.out.println("Specificity (sP): " + specificity);
		
		// Accuracy on average is 0.99 which I think it good.
		System.out.println("\nAccuracy from confusion matrix: " + accuracy);
	}
	
	/**
	 * Calculate Matthew's Correlation Coefficient
	 * If the result is -1 then the binary classifier is wrong, but if it +1 it is completely correct. 
	 */
	public void calculateMCC() {
		double topline = (truePositive * trueNegative) - (falsePositive * falseNegative);
		double bottomline = Math.sqrt((truePositive + falsePositive) * (truePositive + falseNegative) 
				* (trueNegative + falsePositive) * (trueNegative + falseNegative));
		
		double mcc = topline / bottomline;
		System.out.println("Matthew's Correlation Coefficient: " + mcc);
	}
}
