package ie.gmit.sw;

import java.io.File;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLDataSet;
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
	
	
	// First attempt - 500 input nodes, Sigmoid activation functions (all), 250 nodes on hidden layer. Output has biases.
	
	public NeuralNetwork() {
		int inputs = 100; //Change this to the number of input neurons
		int outputs = 235; //Change this to the number of output neurons
		
		//Configure the neural network topology. 
		BasicNetwork network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, inputs)); //You need to figure out the activation function
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 50));
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, outputs));
		network.getStructure().finalizeStructure();
		network.reset();
		//network.reset(1000);

		//Read the CSV file "data.csv" into memory. Encog expects your CSV file to have input + output number of columns.
		DataSetCODEC dsc = new CSVDataCODEC(new File("data.csv"), CSVFormat.ENGLISH, false, inputs, outputs, false);
		MemoryDataLoader mdl = new MemoryDataLoader(dsc);
		MLDataSet trainingSet = mdl.external2Memory();
		
		FoldedDataSet folded = new FoldedDataSet(trainingSet);
		MLTrain train = new ResilientPropagation(network, folded);
		CrossValidationKFold cv = new CrossValidationKFold(train, 5);

		//Use backpropagation training with alpha=0.1 and momentum=0.2
		//Backpropagation trainer = new Backpropagation(network, trainingSet, 0.1, 0.2);
		
		System.out.println("Training started..\n");

		//Train the neural network
		int epoch = 1; //Use this to track the number of epochs
		do { 
			cv.iteration(); 
			epoch++;
		} while(cv.getError() > 0.01);
		
		cv.finishTraining();
		System.out.println("Training complete " + epoch + " epocs with error of: " + cv.getError());
		
		Utilities.saveNeuralNetwork(network, "./test.nn");
		
		// Step 4: Test the NN
		double correct = 0;
		double truePositive = 0;
		double trueNegative= 0;
		double falsePositive = 0;
		double falseNegative= 0;
		double total = 0;
		
		int countone = 0, countminusone = 0, countzero = 0;
		
		for (MLDataPair pair : trainingSet) {
			total++;
			
			MLData output = network.compute(pair.getInput());
			
			int actual = (int) output.getData(0);
			int expected = (int) pair.getIdeal().getData(0);
			
			//System.out.println("E: " + expected + " A: " + actual);
			
			if(actual == 1)
				countone++;
			
			if(actual == -1)
				countminusone++;
			
			if(actual == 0)
				countzero++;
			
			if(actual > 0){
                if(actual == expected){
                    truePositive++;
                    correct++;
                }
                else{
                    falseNegative++;
                }
            }
			
            if(actual <= 0){
                if(actual == expected){
                    trueNegative++;
                    correct++;
                }
                else{
                    falsePositive++;
                }
            }
		}
		
		// sensitivity (sn) = TP (TP + FN)
		// specificity (sP) = TN / (TN + FP)
		
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
		
		System.out.println("1:  " + countone);
		System.out.println("-1:  " + countminusone);
		System.out.println("0:  " + countzero);
	}
}