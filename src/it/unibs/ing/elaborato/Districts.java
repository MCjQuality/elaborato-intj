package it.unibs.ing.elaborato;

import java.io.*;
import java.util.ArrayList;

/**
 * Classe che rappresenta la lista dei vari comprensori aggiunti all'applicazione.
 */
public class Districts implements Readable, Writable {

	private ArrayList<District> districts;

	public Districts() {
		this.districts = new ArrayList<>();
	}

	public void addDistrict(District district) {
		districts.add(district);
	}

	public ArrayList<District> getDistricts() {
		return districts;
	}
	
	public boolean isDistrictDuplicate(String name) {
		return districts.stream().anyMatch(district -> district.getName().equals(name));
	}

	public boolean isMunicipalityDuplicate(String name) {
		return districts.stream().anyMatch(district -> district.isMunicipalityDuplicate(name));
	}

	@Override
	public void read(String filepath) {
		try 
	    {
	    	File file = new File(filepath);
	    	if (!file.createNewFile()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                districts = (ArrayList<District>) ois.readObject();
                ois.close();
            }
	    } catch (EOFException e) {
	    	
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }	
	}
	
	public void write(String filepath) throws IOException {
		File file = new File(filepath);
		if (file.createNewFile()) {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(districts);
			oos.close();
		} else {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(districts);
			oos.close();
		}
	}

}
