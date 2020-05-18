package ie.gmit.sw.nn;

public class HiddenLayerCalculator {
	
	public int CalculationOne(int inputs, int outputs) {
		return inputs + outputs;
	}

	
	public int CalculationTwo(int inputs, int outputs) {
		return (int) ((inputs * 0.66) + outputs);
	}

	
	public int UpperBoundScaling(int inputs, int outputs) {
		double scalingFactor = 0.015;
		
		return (int) (11750 / (scalingFactor * (inputs + outputs)));
	}

	
	public int GeometricPyramidRule(int inputs, int outputs) {
		return (int) Math.sqrt(inputs * outputs);
	}
}
