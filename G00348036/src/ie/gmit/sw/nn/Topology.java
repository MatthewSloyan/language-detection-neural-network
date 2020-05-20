package ie.gmit.sw.nn;

/** 
 * Class used and composed in NeuralNetwork types and NeuralNetworkFunctions.
 * Hold information about the layers in the neural network and can be dynamic.
 * Also sets the number of hidden layer nodes to use using Geometric Pyramid Rule (GPR), 
 * However this can easily be swapped out.
 * 
 * @see CrossValidationNeuralNetwork
 * @see NeuralNetworkFunctions
 * @author Matthew
 */
public class Topology {
	private int inputSize;
	private int hiddenLayerSize;
	private int outputSize;
	
	public Topology(int inputSize, int hiddenLayerSize, int outputSize) {
		super();
		this.inputSize = inputSize;
		this.hiddenLayerSize = hiddenLayerSize;
		this.outputSize = outputSize;
	}

	public int getInputSize() {
		return inputSize;
	}

	public int getHiddenLayerSize() {
		return hiddenLayerSize;
	}

	public int getOutputSize() {
		return outputSize;
	}
	
	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	public void setHiddenLayerSize() {
		// Calculate the number of hidden layer nodes to use (GPR).
		this.hiddenLayerSize = new HiddenLayerCalculator().GeometricPyramidRule(inputSize, outputSize);
	}

	public void setOutputSize(int outputSize) {
		this.outputSize = outputSize;
	}
}
