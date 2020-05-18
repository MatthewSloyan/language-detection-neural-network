package ie.gmit.sw.nn;

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
	
	public void printResults() {
		//sensitivity (sn) = TP (TP + FN)
		//specificity (sP) = TN / (TN + FP)
		double sensitivity = truePositive * (truePositive + falseNegative);
		double specificity = trueNegative / (trueNegative + falsePositive);
		
//		System.out.println("\nTP: " + truePositive);
//		System.out.println("TN: " + trueNegative);
//		System.out.println("FP: " + falsePositive);
//		System.out.println("FN: " + falseNegative);
		
		System.out.println("\nSensitivity (sn): " + sensitivity);
		System.out.println("Specificity (sP): " + specificity);
	}
}
