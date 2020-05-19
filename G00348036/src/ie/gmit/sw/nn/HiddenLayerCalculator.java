package ie.gmit.sw.nn;

/**
* Class that calculates the number of hidden layer nodes using some defined formulas.
* 
* @author Matthew Sloyan
*/
public class HiddenLayerCalculator {
	
	/**
	* Sum of the inputs and outputs, not recommended for this dataset.
	*/
	public int CalculationOne(int inputs, int outputs) {
		return inputs + outputs;
	}

	/**
	* Inputs multiplied by 0.66 plus outputs. Number of nodes calculated was quite high 
	* and again not recommended for this dataset.
	*/
	public int CalculationTwo(int inputs, int outputs) {
		return (int) ((inputs * 0.66) + outputs);
	}

	/**
	 * This takes the inputs and outputs and gets the square root. The number of hidden layer nodes is quite proportional to the input. 
	 * For 1000 vectors the number of hidden nodes is 484 which is between the number of outputs and inputs as recommened.
	 */
	public int UpperBoundScaling(int inputs, int outputs) {
		double scalingFactor = 0.015;
		
		return (int) (11750 / (scalingFactor * (inputs + outputs)));
	}

	/** 
	 * This worked quite well too with a scaling factor of 0.015, however this would grow exponentially if the inputs were higher. 
	 * I also tried lower and higher scaling factors but again it can create either too many nodes or too little very easily.
	 */
	public int GeometricPyramidRule(int inputs, int outputs) {
		return (int) Math.sqrt(inputs * outputs);
	}
}
