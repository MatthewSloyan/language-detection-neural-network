package ie.gmit.sw.nn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.encog.engine.network.activation.ActivationReLU;
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
import org.encog.neural.networks.training.propagation.back.Backpropagation;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.neural.prune.PruneIncremental;
import org.encog.neural.prune.PruneSelective;
import org.encog.util.csv.CSVFormat;

import ie.gmit.sw.processor.Language;
import ie.gmit.sw.processor.TrainingProcessor;

public class NeuralNetwork {

	private BasicNetwork network;
	private MLDataSet trainingSet;
	
	private int inputSize;
	private int hiddenLayerSize;
	private int outputSize = 235; // Output layer node size (235 languages in training set).
	
	private final static NeuralNetwork instance = new NeuralNetwork();
	
	private NeuralNetwork() {}
	
	public static NeuralNetwork getInstance() {
        return instance;
    }
	
	// Setters
	public void setInputSize(int inputSize) {
		this.inputSize = inputSize;
	}

	public void setTrainingSet(MLDataSet trainingSet) {
		this.trainingSet = trainingSet;
	}
	
	public BasicNetwork getNetwork() {
		return network;
	}
	
	public void setNetwork(BasicNetwork network) {
		this.network = network;
	}

	public void startTraining() {	
		// Calculations
		//int hiddenLayerNodes = new HiddenLayerCalculator().CalculationOne(inputSize, outputs);
		//int hiddenLayerNodes = new HiddenLayerCalculator().CalculationTwo(inputSize, outputs);
		//int hiddenLayerNodes = new HiddenLayerCalculator().UpperBoundScaling(inputSize, outputs);
		
		// Choosen calculation (Geometric Pyramid Rule)
		hiddenLayerSize = new HiddenLayerCalculator().GeometricPyramidRule(inputSize, outputSize);
		System.out.println(hiddenLayerSize);
		
		//Configure the neural network topology. 
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, inputSize, 0.8));
		network.addLayer(new BasicLayer(new ActivationTANH(), true, hiddenLayerSize, 0.8));
		network.addLayer(new BasicLayer(new ActivationSoftMax(), false, outputSize, 0.8));
		network.getStructure().finalizeStructure();
		network.reset();
		
		//Read the CSV file "data.csv" into memory. Encog expects your CSV file to have input + output number of columns.
		DataSetCODEC dsc = new CSVDataCODEC(new File("data.csv"), CSVFormat.ENGLISH, false, inputSize, outputSize, false);
		MemoryDataLoader mdl = new MemoryDataLoader(dsc);
		trainingSet = mdl.external2Memory();
		
		FoldedDataSet folded = new FoldedDataSet(trainingSet);
		MLTrain train = new ResilientPropagation(network, folded);
		CrossValidationKFold cv = new CrossValidationKFold(train, 5);
		
		//PruneSelective p = new PruneSelective(network);
		//p.prune(0, 1);
		
		long startTime = System.nanoTime(); 
		
		System.out.println("\nTraining started..\n");
		
		long start = System.currentTimeMillis();
		long end = start + 120*1000;

		//Train the neural network
		double minError = 0.0025;
		int epoch = 1; //Use this to track the number of epochs
		do { 
			cv.iteration(); 
			epoch++;
			
			System.out.println("Epoch: " + epoch + " Error Rate: " + cv.getError());
		} while(cv.getError() > minError && System.currentTimeMillis() < end);
		
		cv.finishTraining();
		System.out.println("Training complete in " + epoch + " epocs with error of: " + cv.getError());
		System.out.println("\nTraining completed in: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " seconds.");
	}
	
	public void startTests() {
		// Step 4: Test the NN
		double correct = 0;
		double total = 0;
		
		System.out.println("\nTesting started..\n");
		
		ConfusionMatrix cm = new ConfusionMatrix();
		
		for (MLDataPair pair : trainingSet) {
			total++;
			
			MLData output = network.compute(pair.getInput());
			
			double[] expected = output.getData();
			double[] actual = pair.getIdeal().getData();
			
			int expectedIndex = 0;
			int actualIndex = 0;
			
			for (int i = 0; i < expected.length; i++){
			   if (expected[i] > expected[expectedIndex]) {
				   expectedIndex = i;
			   }
			   
			   if (actual[i] > actual[actualIndex]) {
				   actualIndex = i;
			   }
			   
			   // Total up
			   cm.addToMatrix((int)Math.round(expected[i]), (int)actual[i]);
			}
			
			if (expectedIndex == actualIndex) {
				correct++;
			}
		}
				
		System.out.println("Total: 	  " + total);
		System.out.println("Correct:  " + correct);
		System.out.println("Accuracy: " + ((correct / total) * 100) + "%");
		
		cm.printResults();
	}
	
	public void viewTopology() {
		System.out.println("\n==== Network Topology ==== \n");
		System.out.println("Number of layers: 3");
		System.out.println("Layer 1: Input Layer using a Sigmoid Activation Function with " + inputSize + " nodes.");
		System.out.println("Layer 2: Hidden Layer using a TanH Activation Function with " + hiddenLayerSize + " nodes.");
		System.out.println("Layer 3: Output Layer using a Softmax Activation Function with " + outputSize + " nodes.");
		System.out.println("\nAll layers have a Drop Out Rate of 0.8 and contain biases.");
	}
	
	private int getMaxIndex(double[] input) {
		int indexOfMax = 0;
		
		for (int i = 0; i < input.length; i++){
		   if (input[i] > input[indexOfMax]) {
			   indexOfMax = i;
		   }
		}
		return indexOfMax;
	}
	
	public String predict(double[] vector) {
		
		// Set up data and pass in variables.
		MLData input = new BasicMLData(vector.length);
		
		for (int i = 0; i < vector.length; i++) {
			input.setData(i, vector[i]);
		}
		
		MLData output = network.compute(input);
		
		double[] expected = output.getData();
		
		int actualIndex = getMaxIndex(expected);
		
		return Language.values()[actualIndex].toString();
	}
	
	public static void main(String[] args) {
		int inputs = 500;
		
		try {
			new TrainingProcessor(inputs, 2).processFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NeuralNetwork nn = NeuralNetwork.getInstance();
		nn.setInputSize(inputs);
		nn.startTraining();
		nn.startTests();
	}
}