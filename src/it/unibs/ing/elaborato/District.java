package it.unibs.ing.elaborato;

import java.io.*;
import java.util.ArrayList;

/**
 * Astrazione di un comprensorio geografico, il quale e' composto da un nome e una lista dei comuni annessi.
 */
public class District implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name;
	private ArrayList<String> territories;

	public District(String nome) {
		this.name = nome;
		this.territories = new ArrayList<String>();
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getTerritories() {
		return territories;
	}
	
	public boolean isMunicipalityDuplicate(String name) {
		return territories.stream().anyMatch(municipality -> municipality.equals(name));
	}

	public void add(String territorio) {
		territories.add(territorio);
	}

}
