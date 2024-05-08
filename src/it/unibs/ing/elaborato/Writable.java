package it.unibs.ing.elaborato;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Writable {
	
	public void write(String filepath) throws FileNotFoundException, IOException;

}
