package ie.gmit.sw.processor;

/**
* Interface used for processing user, testing or training data.
* More could be added easily.
*/
public interface Processable {
	
	public void processFile(String fileName);
	public void processLine(String line);
}
