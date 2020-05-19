package ie.gmit.sw.nn;

public class NeuralNetworkFactory {
	
	private final static NeuralNetworkFactory instance = new NeuralNetworkFactory();
	
	private NeuralNetworkFactory() {}
	
	public static NeuralNetworkFactory getInstance() {
        return instance;
    }

	// use getNeuralNetwork method to get object of type NeuralNetworkable
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
