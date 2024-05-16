package it.unibs.ing.elaborato;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ClosedSets implements Readable, Writable {
	
	private List<ExchangeProposals> closedSets;
	
	public ClosedSets() 
	{
		closedSets = new ArrayList<>();
	}
	
	public void add(ExchangeProposals closedSet) 
	{
		closedSets.add(closedSet);
	}

	public List<ExchangeProposals> getClosedSets()
	{
		return closedSets;
	}
	
	@Override
	public void read(String filepath) 
	{
		try {
			File file = new File(filepath);
			if (!file.createNewFile()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				closedSets = (List<ExchangeProposals>) ois.readObject();
				ois.close();
			}
		} catch (EOFException e) {

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void write(String filepath) throws IOException
	{
		File file = new File(filepath);
		if (file.createNewFile()) 
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(closedSets);
			oos.close();
		} 
		else 
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(closedSets);
			oos.close();
		}
	}

}
