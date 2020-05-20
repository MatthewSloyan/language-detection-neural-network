package ie.gmit.sw.nn;

import java.util.concurrent.TimeUnit;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.engine.network.activation.ActivationSoftMax;
import org.encog.engine.network.activation.ActivationTANH;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.folded.FoldedDataSet;
import org.encog.ml.train.MLTrain;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.cross.CrossValidationKFold;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import ie.gmit.sw.processor.TrainingProcessor;

/**
* Main class that contains all functions for training the neural network.
* A factory design pattern is used with this class, so that the network type can be swapped out easily.
* NeuralNetworkFunctions holds all the common functionality used for this class such as testing, 
* predicting and viewing the topology. This is so there is no duplicate code.
* 
* Networkable is implemented, so that any number of neural network types could be created.
* 
* @see Networkable
* @see NeuralNetworkFunctions
* @author Matthew Sloyan
*/
public class CrossValidationNeuralNetwork implements NeuralNetworkable {

	// Instance variables.
	private BasicNetwork network;
	private Topology topology;
	
	// Getters & Setters
	// These are used to set variables when creating and loading network.
	public BasicNetwork getNetwork() {
		return network;
	}
	
	public void setNetwork(BasicNetwork network) {
		this.network = network;
	}
	
	public Topology getTopology() {
		return topology;
	}

	public void setTopology(Topology topology) {
		this.topology = topology;
	}
	
	/**
	 * Method for creating and configuring Neural Network.
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
	 */
	public void configureNetwork() {
		// Chosen calculation (Geometric Pyramid Rule)
		topology.setHiddenLayerSize();
				
		//Configure the neural network topology. 
		network = new BasicNetwork();
		network.addLayer(new BasicLayer(new ActivationSigmoid(), true, topology.getInputSize(), 0.8));
		network.addLayer(new BasicLayer(new ActivationTANH(), true, topology.getHiddenLayerSize(), 0.8));
		network.addLayer(new BasicLayer(new ActivationSoftMax(), false, topology.getOutputSize(), 0.8));
		network.getStructure().finalizeStructure();
		network.reset();
		
		// Tested pruning, but increased error rate.
		// Could possibly used to stimulate or remove neurons if maybe too many.
		//PruneSelective p = new PruneSelective(network);
		//p.stimulateWeakNeurons(1, 100, 50);
		//p.prune(0, 100);
	}

	/**
	 * Training method for training 5 Fold Cross Validation Neural Network.
	 * 
	 * Training will occur until the error rate is hit, 2.5 minutes has elapsed or 6 epochs has completed.
	 * This was determined to be the best variables to achieve the most accurate results in minimal time.
	 */
	public void startTraining() {
		//Read the CSV file "data.csv" into memory. Encog expects your CSV file will have input + output number of columns.
		MLDataSet trainingSet = new Utilities().loadTrainingData("data.csv", topology);
		
		FoldedDataSet folded = new FoldedDataSet(trainingSet);
		MLTrain train = new ResilientPropagation(network, folded);
		CrossValidationKFold cv = new CrossValidationKFold(train, 5);
		
		System.out.println("\nTraining started..\n");
		
		// Used to stop network after 2 minutes and 50 seconds.
		long start = System.currentTimeMillis();
		long end = start + 170*1000;

		// Set error limit, this was the lowest I could get it to within a reasonable time. 
		double minError = 0.0017;
		
		// Track number of epochs and set limit. This limit was found to be the lowest error rate achievable.
		int epoch = 1; 
		int maxEpochs = 5;
		
		// Start running time.
		long startTime = System.nanoTime(); 

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

	// Testing purposes only.
	public static void main(String[] args) {
		int inputs = 700;
		
		try {
			new TrainingProcessor(inputs, 2).processFile("./wili-2018-Small-11750-Edited.txt");
		} catch (Exception e) {
			
		}
		
		NeuralNetworkable nn = new CrossValidationNeuralNetwork();
		Topology top = new Topology(700, 0, 235);
		top.setHiddenLayerSize();
		nn.setTopology(top);
		nn.configureNetwork();
		nn.startTraining();
		
		NeuralNetworkFunctions nnf = new NeuralNetworkFunctions(nn);
		nnf.startTests();
	}
}