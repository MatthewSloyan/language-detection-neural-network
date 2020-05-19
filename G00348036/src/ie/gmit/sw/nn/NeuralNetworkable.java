package ie.gmit.sw.nn;

import org.encog.neural.networks.BasicNetwork;

public interface NeuralNetworkable {
	
	public void startTraining();
	public void startTests();
	public void viewTopology();
	public String predict(double[] vector);
	
	public void setInputSize(int inputSize);
	public BasicNetwork getNetwork();
	public void setNetwork(BasicNetwork network);
}
