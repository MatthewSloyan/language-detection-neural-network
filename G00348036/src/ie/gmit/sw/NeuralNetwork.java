package ie.gmit.sw;

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

public class NeuralNetwork {

	/*
	 * *************************************************************************************
	 * NB: READ THE FOLLOWING CAREFULLY AFTER COMPLETING THE TWO LABS ON ENCOG AND REVIEWING
	 * THE LECTURES ON BACKPROPAGATION AND MULTI-LAYER NEURAL NETWORKS! YOUR SHOULD ALSO 
	 * RESTRUCTURE THIS CLASS AS IT IS ONLY INTENDED TO DEMO THE ESSENTIALS TO YOU. 
	 * *************************************************************************************
	 * 
	 * The following demonstrates how to configure an Encog Neural Network and train
	 * it using backpropagation from data read from a CSV file. The CSV file should
	 * be structured like a 2D array of doubles with input + output number of columns.
	 * Assuming that the NN has two input neurons and two output neurons, then the CSV file
	 * should be structured like the following:
	 *
	 *			-0.385,-0.231,0.0,1.0
	 *			-0.538,-0.538,1.0,0.0
	 *			-0.63,-0.259,1.0,0.0
	 *			-0.091,-0.636,0.0,1.0
	 * 
	 * The each row consists of four columns. The first two columns will map to the input
	 * neurons and the last two columns to the output neurons. In the above example, rows 
	 * 1 an 4 train the network with features to identify a category 2. Rows 2 and 3 contain
	 * features relating to category 1.
	 * 
	 * You can normalize the data using the Utils class either before or after writing to 
	 * or reading from the CSV file. 
	 */
	private int inputs;
	
	private BasicNetwork network;
	private MLDataSet trainingSet;
	
	// First attempt - 500 input nodes, Sigmoid activation functions (all), 250 nodes on hidden layer. Output has biases.
	
	public NeuralNetwork(int inputs) {
		this.inputs = inputs;
	}
	
	public void startTraining() {	
		//int inputs = 100; //Change this to the number of input neurons
		int outputs = 235; //Change this to the number of output neurons
		double scalingFactor = 0.015;
		
		//Read the CSV file "data.csv" into memory. Encog expects your CSV file to have input + output number of columns.
		DataSetCODEC dsc = new CSVDataCODEC(new File("data.csv"), CSVFormat.ENGLISH, false, inputs, outputs, false);
		MemoryDataLoader mdl = new MemoryDataLoader(dsc);
		trainingSet = mdl.external2Memory();
		
		//int hiddenLayerNodes = 500;
		
		// Calculations
		//int hiddenLayerNodes = inputs + outputs;
		//int hiddenLayerNodes = (int) ((inputs * 0.66) + outputs);
		int hiddenLayerNodes = (int) Math.sqrt(inputs * outputs);
		//int hiddenLayerNodes = (int) (11750 / (scalingFactor * (inputs + outputs)));
		
		System.out.println(hiddenLayerNodes);
		
		//Configure the neural network topology. 
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, inputs));
		network.addLayer(new BasicLayer(new ActivationTANH(), true, hiddenLayerNodes));
		network.addLayer(new BasicLayer(new ActivationSoftMax(), true, outputs));
		network.getStructure().finalizeStructure();
		network.reset();
		//network.reset(1000);
		
		FoldedDataSet folded = new FoldedDataSet(trainingSet);
		MLTrain train = new ResilientPropagation(network, folded);
		CrossValidationKFold cv = new CrossValidationKFold(train, 5);
		
		//PruneSelective p = new PruneSelective(network);
		//p.prune(0, 1);

		//Use backpropagation training with alpha=0.1 and momentum=0.2
		//Backpropagation trainer = new Backpropagation(network, trainingSet, 0.1, 0.2);
		
		long startTime = System.nanoTime(); 
		
		System.out.println("Training started..\n");

		//Train the neural network
		double minError = 0.002;
		int epoch = 1; //Use this to track the number of epochs
		do { 
			cv.iteration(); 
			epoch++;
			
			System.out.println("Epoch: " + epoch);
			System.out.println("Error Rate: " + cv.getError());
		} while(cv.getError() > minError);
		
		cv.finishTraining();
		System.out.println("Training complete in " + epoch + " epocs with error of: " + cv.getError());
		System.out.println("\nTraining completed in: " + TimeUnit.SECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + " seconds.");
		
		// Ask the user if they want to save network.
		//saveNetwork();
	}
	
	public void startTests() {
		// Step 4: Test the NN
		double correct = 0;
		double truePositive = 0, trueNegative= 0, falsePositive = 0, falseNegative= 0;
		double total = 0;
		
		//int countone = 0, countminusone = 0, countzero = 0;
		
		// Create new file to write CSV data to.
//		DecimalFormat df = new DecimalFormat("###.###");
//		FileWriter fw = null;
//		try {
//			fw = new FileWriter("./test.csv");
//		} catch (IOException e1) {
//		}
		
		for (MLDataPair pair : trainingSet) {
			total++;
			
			MLData output = network.compute(pair.getInput());
			
			double[] expected = output.getData();
			int expectedIndex = getMaxIndex(expected);
				
			double[] actual = pair.getIdeal().getData();
			int actualIndex = getResult(actual);
			
			if (expectedIndex == actualIndex) {
				correct++;
			}
			
//			try {
//				for (int i = 0; i < actual.length; i++) {
//					fw.append(df.format(actual[i]));
//		            fw.append(',');
//				}
//
//				fw.append('\n');
//			} catch (IOException e) {
//			}
			
			int y = (int) Math.round(output.getData(0));
			int yd = (int) pair.getIdeal().getData(0);
			
			//System.out.println(y + " " + yd);
			
			if(y == 1){
				if(y == yd){
					truePositive++;
				}
				else{
					falseNegative++;
				}
			}
			
			if(y == 0){
				if(y == yd){
					trueNegative++;
				}
				else{
					falsePositive++;
				}
			}
			
			//int test = (int) output.getData(0); 
			
			//System.out.println(output.getData(1));
			
			//System.out.println(indexOfMax);
			
			//double max = Arrays.stream(actual).max().getAsDouble();
			//System.out.println("Max = " + max);
			
			//System.out.println(actual);
			
			
//			for (int i = 0; i < actual.length; i++) {
//				System.out.print(actual[i] + " ");
//			}
//			System.out.println();
//			
//			for (int i = 0; i < expected.length; i++) {
//				System.out.print(expected[i] + " ");
//			}
//			
//			//System.out.println(expected);
//			
//			//System.out.println("E: " + expected + " A: " + actual);
//			
//			if(actual == 1)
//				countone++;
//			
//			if(actual == -1)
//				countminusone++;
//			
//			if(actual == 0)
//				countzero++;
//			
//			if(actual > 0){
//                if(actual == expected){
//                    truePositive++;
//                    correct++;
//                }
//                else{
//                    falseNegative++;
//                }
//            }
//			
//            if(actual <= 0){
//                if(actual == expected){
//                    trueNegative++;
//                    correct++;
//                }
//                else{
//                    falsePositive++;
//                }
//            }
		}
		
//		try {
//			fw.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//sensitivity (sn) = TP (TP + FN)
		//specificity (sP) = TN / (TN + FP)
				
		double sensitivity = truePositive * (truePositive + falseNegative);
		double specificity = trueNegative / (trueNegative + falsePositive);
				
		System.out.println("Testing complete. Acc=" + ((correct / total) * 100));
		System.out.println("Total: 	 " + total);
		System.out.println("Correct: " + correct);
		
		System.out.println("\nTP: " + truePositive);
		System.out.println("TN: " + trueNegative);
		System.out.println("FP: " + falsePositive);
		System.out.println("FN: " + falseNegative);
		
		System.out.println("\nSensitivity (sn): " + sensitivity);
		System.out.println("Specificity (sP): " + specificity);
//		
//		System.out.println("1:  " + countone);
//		System.out.println("-1:  " + countminusone);
//		System.out.println("0:  " + countzero);
	}
	
	public void saveNetwork() {
		Scanner console = new Scanner(System.in);
		boolean isValid;
		
		// Save
		do {
			System.out.println("\nWould you like to save the neural network:\n (1) Yes\n (2) No");
			String option = console.next();
			
			isValid = true;
			
			if (Integer.parseInt(option) == 1) {
				// Save Network
				System.out.println("Please enter the name of the Neural Network file (Extension is not required).");
				String nnFilePath = console.nextLine();
				
				Utilities.saveNeuralNetwork(network, "./" + nnFilePath + ".nn");
				
				isValid = false;
			} else if (Integer.parseInt(option) == 2) {
				isValid = false;
			} else {
				System.out.println("Invalid option, please try again.");
			} 
		} while (isValid);
	}
	
	public void loadNetwork(String fileName) {
		
		network = Utilities.loadNeuralNetwork(fileName);
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
	
	private int getResult(double[] input) {
		int indexOfMax = 0;
		
		for (int i = 0; i < input.length; i++){
		   if (input[i] == 1) {
			   indexOfMax = i;
			   break;
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
		int inputs = 650;
		
		try {
			new VectorProcessor(inputs, 2).processFile();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		NeuralNetwork nn = new NeuralNetwork(inputs);
		nn.startTraining();
		nn.startTests();
	}
}