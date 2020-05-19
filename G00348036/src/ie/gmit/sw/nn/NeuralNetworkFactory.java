package ie.gmit.sw.nn;

/**
 * Factory design pattern used to create neural network type. In this current build there is only support for 
 * Cross Validation but more could easily be added.
 * 
 * This avoids tight coupling between the creator and the concrete networks.
 * Single Responsibility Principle - You can move the creation code into one place in the program, making the code easier to support.
 * Open/Closed Principle - New types of networks can be introduced into the program without breaking existing code.
 * 
 * Singleton design pattern is also used for Factory instance as there should only be one.
 * 
 * @author Matthew
 */
public class NeuralNetworkFactory {
	
	private final static NeuralNetworkFactory instance = new NeuralNetworkFactory();
	
	private NeuralNetworkFactory() {}
	
	public static NeuralNetworkFactory getInstance() {
        return instance;
    }

	/**
	 * Method that creates the desired neural network instance.
	 */
	public NeuralNetworkable getNeuralNetwork(String networkType) {
		
		if (networkType == null) {
			return null;
		}
		
		if (networkType.equalsIgnoreCase("CV")) {
			return new CrossValidationNeuralNetwork();
		} 
		// Other types could be added easily
//		else if (networkType.equalsIgnoreCase("BP")) {
//			return new
//		} 

		return null;
	}

}
