package ie.gmit.sw.nn;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import ie.gmit.sw.processor.Language;

/**
 * Class that contains common functionality for any type of network. 
 * E.g Testing, predicting and viewing the topology.
 * This promotes resuse so that there wouldn't be duplicate code in the Network type class
 * if another one was created.
 * 
 * @author Matthew
 */
public class NeuralNetworkFunctions {
	
	private Topology topology;
	private BasicNetwork network;
	
	public NeuralNetworkFunctions(NeuralNetworkable nn) {
		super();
		topology = nn.getTopology();
		network = nn.getNetwork();
	}
	
	/**
	 * Training method to test neural network.
	 * Using the training set to test against the number of correct results is calculated,
	 * by getting the index of the highest value in both the actual and expected values.
	 * These values are also passed into addToMatrix to tally up the number of TP, TN, FP and FN.
	 * 
	 * @see ConfusionMatrix
	 */
	public void startTests() {
		double correct = 0;
		double total = 0;
		
		System.out.println("\nTesting started..\n");
		
		//Read the CSV file "data.csv" into memory. Encog expects your CSV file will have input + output number of columns.
		MLDataSet trainingSet = new Utilities().loadTrainingData("data.csv", topology);
		
		// Setup Confusion matrix.
		ConfusionMatrix cm = new ConfusionMatrix();
		
		// Loop through testing data.
		for (MLDataPair pair : trainingSet) {
			total++;
			
			// Compute output, get expected and actual data.
			MLData output = network.compute(pair.getInput());
			
			double[] expected = output.getData();
			double[] actual = pair.getIdeal().getData();
			
			int expectedIndex = 0;
			int actualIndex = 0;
			
			// Loop through array to get max values and add to Confusion matrix.
			// This was completed in one loop for efficiency.
			for (int i = 0; i < expected.length; i++){
			   if (expected[i] > expected[expectedIndex]) {
				   expectedIndex = i;
			   }
			   
			   if (actual[i] > actual[actualIndex]) {
				   actualIndex = i;
			   }
			   
			   // Add to TP, TN, FP and FN.
			   cm.addToMatrix((int)Math.round(expected[i]), (int)actual[i]);
			}
			
			// If the index is the same then the result is correct.
			if (expectedIndex == actualIndex) {
				correct++;
			}
		}

		// Print out confusion matrix results. (Sensitivity and Specificity).
		cm.printResults();
				
		System.out.println("\nTotal: 	  " + total);
		System.out.println("Correct:  " + correct);
		System.out.println("Accuracy: " + ((correct / total) * 100) + "%");
	}
	
	/**
	 * Method that displays the neural network topology to the user.
	 */
	public void viewTopology() {
		System.out.println("\n==== Network Topology ==== \n");
		System.out.println("Number of layers: 3");
		System.out.println("Layer 1: Input Layer using a Sigmoid Activation Function with " + topology.getInputSize() + " nodes.");
		System.out.println("Layer 2: Hidden Layer using a TanH Activation Function with " + topology.getHiddenLayerSize() + " nodes.");
		System.out.println("Layer 3: Output Layer using a Softmax Activation Function with " + topology.getOutputSize() + " nodes.");
		System.out.println("\nAll layers have a Drop Out Rate of 0.8 (80%)");
	}
	
	/**
	 * Method that predicts a language result for the user.
	 * The hashed vector is passed in, added to a MLData object
	 * and then fed into the neural network.
	 * This expected result is then returned and the max index is computed.
	 * This index is the language in Enum so return value.
	 * 
	 * @see Utilities
	 */
	public String predict(double[] vector) {
		
		// Set up data and pass in variables, and add to object.
		MLData input = new BasicMLData(vector.length);
		
		for (int i = 0; i < vector.length; i++) {
			input.setData(i, vector[i]);
		}
		
		// Compute result and get output.
		MLData output = network.compute(input);
		double[] expected = output.getData();
		
		// Get max value and then return language at this index.
		int actualIndex = Utilities.getMaxIndex(expected);
		
		return Language.values()[actualIndex].toString();
	}
}
