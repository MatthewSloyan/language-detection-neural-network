package ie.gmit.sw.nn;

import org.encog.neural.networks.BasicNetwork;

/**
 * Interface designed to be used when creating instances of different types of neural networks in factory.
 * This is passed into number of classes so the type could easily be changed.
 * @author Matthew
 */
public interface NeuralNetworkable {
	
	// Methods that define behavior of class.
	public void configureNetwork();
	public void startTraining();
	
	// Might not be ideal to have gets/sets as they define state, but it allows resusability.
	public BasicNetwork getNetwork();
	public void setNetwork(BasicNetwork network);
	public Topology getTopology();
	public void setTopology(Topology topology);
}
