package ie.gmit.sw.nn;

import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.basic.BasicMLData;

import ie.gmit.sw.processor.Language;

public class NeuralNetworkFunctions {
	
//	public void startTests() {
//		// Step 4: Test the NN
//		double correct = 0;
//		double total = 0;
//		
//		System.out.println("\nTesting started..\n");
//		
//		ConfusionMatrix cm = new ConfusionMatrix();
//		
//		for (MLDataPair pair : trainingSet) {
//			total++;
//			
//			MLData output = network.compute(pair.getInput());
//			
//			double[] expected = output.getData();
//			double[] actual = pair.getIdeal().getData();
//			
//			int expectedIndex = 0;
//			int actualIndex = 0;
//			
//			for (int i = 0; i < expected.length; i++){
//			   if (expected[i] > expected[expectedIndex]) {
//				   expectedIndex = i;
//			   }
//			   
//			   if (actual[i] > actual[actualIndex]) {
//				   actualIndex = i;
//			   }
//			   
//			   // Total up
//			   cm.addToMatrix((int)Math.round(expected[i]), (int)actual[i]);
//			}
//			
//			if (expectedIndex == actualIndex) {
//				correct++;
//			}
//		}
//				
//		System.out.println("Total: 	  " + total);
//		System.out.println("Correct:  " + correct);
//		System.out.println("Accuracy: " + ((correct / total) * 100) + "%");
//		
//		cm.printResults();
//	}
//	
//	public void viewTopology() {
//		System.out.println("\n==== Network Topology ==== \n");
//		System.out.println("Number of layers: 3");
//		System.out.println("Layer 1: Input Layer using a Sigmoid Activation Function with " + inputSize + " nodes.");
//		System.out.println("Layer 2: Hidden Layer using a TanH Activation Function with " + hiddenLayerSize + " nodes.");
//		System.out.println("Layer 3: Output Layer using a Softmax Activation Function with " + outputSize + " nodes.");
//		System.out.println("\nAll layers have a Drop Out Rate of 0.8 and contain biases.");
//	}
//	
//	public String predict(double[] vector) {
//		
//		// Set up data and pass in variables.
//		MLData input = new BasicMLData(vector.length);
//		
//		for (int i = 0; i < vector.length; i++) {
//			input.setData(i, vector[i]);
//		}
//		
//		MLData output = network.compute(input);
//		
//		double[] expected = output.getData();
//		
//		int actualIndex = getMaxIndex(expected);
//		
//		return Language.values()[actualIndex].toString();
//	}
//	
//	private int getMaxIndex(double[] input) {
//		int indexOfMax = 0;
//		
//		for (int i = 0; i < input.length; i++){
//		   if (input[i] > input[indexOfMax]) {
//			   indexOfMax = i;
//		   }
//		}
//		return indexOfMax;
//	}

}
