package ie.gmit.sw.nn;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.buffer.MemoryDataLoader;
import org.encog.ml.data.buffer.codec.CSVDataCODEC;
import org.encog.ml.data.buffer.codec.DataSetCODEC;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.prune.PruneSelective;
import org.encog.util.csv.CSVFormat;

import ie.gmit.sw.processor.Language;
import ie.gmit.sw.processor.TrainingProcessor;

/**
* Main class that contains all functions for training, testing predicting results from the neural network.
* A factory design pattern is used with this class, so that the network type can be swapped out easily.
* 
* Networkable is implemented, so that any number of neural network types could be created.
* 
* @see Networkable
* @author Matthew Sloyan
*/
public class CrossValidationNeuralNetwork implements NeuralNetworkable {

	// Instance variables.
	private BasicNetwork network;
	private MLDataSet trainingSet;
	
	private int inputSize;
	private int hiddenLayerSize;
	private int outputSize = 235; // Output layer node size (235 languages in training set).
	
	// Getters & Setters
	// These are used to set variables when creating and loading network.
	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}
	
	public BasicNetwork getNetwork() {
		return network;
	}
	
	public void setNetwork(BasicNetwork network) {
		this.network = network;
	}

	/**
	 * Training method for creating and training 5 Fold Cross Validation Neural Network.
	 * The number of hidden layers are calculated using the Geometric Pyramid Rule.
	 * 
	 * A basic network is created with the following layers.
	 * Layer 1: Input Layer using a Sigmoid Activation Function with x nodes.
	 * Layer 2: Hidden Layer using a TanH Activation Function with x nodes.
	 * Layer 3: Output Layer using a Softmax Activation Function with 235 nodes.
	 * All layers have a Drop Out Rate of 0.8.
	 * 
	 * These activations functions were chosen to be the most accurate and efficient
	 * through trial and error and research. My attempts and all configurations can be found in my README.
	 * 
	 * Training will occur until the error rate is hit, 2.5 minutes has elapsed or 6 epochs has completed.
	 * This was determined to be the best variables to achieve the most accurate results in minimal time.
	 */
	public void startTraining() {
		// Calculations
		//int hiddenLayerNodes = new HiddenLayerCalculator().CalculationOne(inputSize, outputs);
		//int hiddenLayerNodes = new HiddenLayerCalculator().CalculationTwo(inputSize, outputs);
		//int hiddenLayerNodes = new HiddenLayerCalculator().UpperBoundScaling(inputSize, outputs);
		
		// Choosen calculation (Geometric Pyramid Rule)
		hiddenLayerSize = new HiddenLayerCalculator().GeometricPyramidRule(inputSize, outputSize);
		
		//Configure the neural network topology. 
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, inputSize, 0.8));
		network.addLayer(new BasicLayer(new ActivationTANH(), true, hiddenLayerSize, 0.8));
		network.addLayer(new BasicLayer(new ActivationSoftMax(), false, outputSize, 0.8));
		network.getStructure().finalizeStructure();
		network.reset();
		
		// Tested pruning, but increased error rate.
		//PruneSelective p = new PruneSelective(network);
		//p.stimulateWeakNeurons(1, 100, 50);
		
		//Read the CSV file "data.csv" into memory. Encog expects your CSV file to have input + output number of columns.
		DataSetCODEC dsc = new CSVDataCODEC(new File("data.csv"), CSVFormat.ENGLISH, false, inputSize, outputSize, false);
		MemoryDataLoader mdl = new MemoryDataLoader(dsc);
		trainingSet = mdl.external2Memory();
		
		FoldedDataSet folded = new FoldedDataSet(trainingSet);
		MLTrain train = new ResilientPropagation(network, folded);
		CrossValidationKFold cv = new CrossValidationKFold(train, 5);
		
		long startTime = System.nanoTime(); 
		
		System.out.println("\nTraining started..\n");
		
		long start = System.currentTimeMillis();
		long end = start + 170*1000; // 2mins 50 seconds

		double minError = 0.0017;
		int epoch = 1; //Use this to track the number of epochs
		int maxEpochs = 5; // 5 Epochs was found to be the max accuracy achievable.

		//Train the neural network
		do { 
			cv.iteration(); 
			epoch++;
			
			System.out.println("Epoch: " + epoch + " Error Rate: " + cv.getError());
		} while(cv.getError() > minError && System.currentTimeMillis() < end && epoch < maxEpochs);
		
		cv.finishTraining();
		
		System.out.println("\nTraining complete in " + epoch + " epocs with error of: " + cv.getError());
		System.out.println("Training completed in: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " seconds.");
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

		// Print out confustion matrix results. (Sensitivity and Specificity).
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
		System.out.println("Layer 1: Input Layer using a Sigmoid Activation Function with " + inputSize + " nodes.");
		System.out.println("Layer 2: Hidden Layer using a TanH Activation Function with " + hiddenLayerSize + " nodes.");
		System.out.println("Layer 3: Output Layer using a Softmax Activation Function with " + outputSize + " nodes.");
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
		
		// Comput result and get output.
		MLData output = network.compute(input);
		double[] expected = output.getData();
		
		// Get max value and then return language at this index.
		int actualIndex = Utilities.getMaxIndex(expected);
		
		return Language.values()[actualIndex].toString();
	}
	
	// Testing purposes only.
	public static void main(String[] args) {
		int inputs = 700;
		
		try {
			new TrainingProcessor(inputs, 2).processFile("./wili-2018-Small-11750-Edited.txt");
		} catch (Exception e) {
			
		}
		
		CrossValidationNeuralNetwork nn = new CrossValidationNeuralNetwork();
		nn.setInputSize(inputs);
		nn.startTraining();
		nn.startTests();
	}
}