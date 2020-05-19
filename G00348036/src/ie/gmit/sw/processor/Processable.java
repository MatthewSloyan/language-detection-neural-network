package ie.gmit.sw.processor;

public interface Processable {
	
	public void processFile(String fileName);
	public void processLine(String line);
}
