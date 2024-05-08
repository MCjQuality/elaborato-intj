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

/**
 * Astrazione rappresentante la lista degli elementi di conversione.
 */
public class ConversionElements implements Cloneable, Readable, Writable {

	private List<ConversionElement> list;

	public ConversionElements() {
		list = new ArrayList<ConversionElement>();
	}

	public void addFactConv(ConversionElement elem) {
		list.add(elem);
	}

	public boolean isCouplePresent(Couple couple) {
		return list.stream().anyMatch(s -> s.getCouple().equals(couple));
	}

	public int size() {
		return list.size();
	}

	public boolean isFactConvPresent(Double factConv) {
		return list.stream().anyMatch(s -> s.getConversionFactor() == factConv);
	}

	public void replace(Couple coupleToFind, double l) {
		list.stream().filter(s -> s.getCouple().equals(coupleToFind)).forEach(s -> s.setConversionFactor(l));
	}

	public List<ConversionElement> getRemainingConversionElements() {
		return list.stream().filter(s -> s.getConversionFactor() == 0).toList();
	}

	public double[] getConversionFactorRange(Couple elem) {
		double [] x = new double [] {2.1, 0.4};
		ArrayList<ConversionElement> lista = (ArrayList<ConversionElement>)getConversionElementsCompatible(elem);
		if(lista.isEmpty()) {
			x[0] = 0.5;
			x[1] = 2.0;
			return x;
		}
		else {
			for(ConversionElement conv : lista) {
				if(conv.getConversionFactor() < x[0])
					x[0] = conv.getConversionFactor();
				if(conv.getConversionFactor() > x[1])
					x[1] = conv.getConversionFactor();
			}
			x[0] = 0.5/x[0];
			x[1] = 2/x[1];
			if(x[0] < 0.5)
				x[0] = 0.5;
			if(x[1] > 2.0)
				x[1] = 2.0;

			return x;
		}
	}

	public List<ConversionElement> getConversionElementsCompatible(Couple elem) {
		ArrayList<ConversionElement> lista = new ArrayList<>();
		for(ConversionElement conv : list) {
			if(conv.getConversionFactor() != 0.0 && (elem.getFirstLeaf().equals(conv.getCouple().getSecondLeaf()) 
					|| elem.getSecondLeaf().equals(conv.getCouple().getFirstLeaf()))) {
					lista.add(conv);
			}
		}
		return lista;
	}
	
	public List<ConversionElement> getConversionElements() {
		return list;
	}

	/*
	 * scorro gli elementi di conversione
	 * fisso quelli con fattore di conv a 0
	 * trovo gli elementi di conversione compatibili scorrendo la lista ed inserendo gli elementi che:
	 * - hanno fattore di conv diverso da 0
	 * - hanno la prima foglia uguale alla prima foglia ma la seconda diversa 
	 * - oppure hanno la seconda foglia uguale e la prima diversa
	 * una volta trovati gli elementi compatibili controllo che siano piu di 1
	 * in tal caso li scorro due volte e se la coppia del primo ciclo presenta una seconda foglia uguale alla prima foglia del secondo ciclo (oppure prima = seconda) e calcolo il fattore di conversione derivato
	 * ...to be continued
	 * 
	 */
	public void automaticConvFactCalculate() {
		for(ConversionElement elem : list)
			if (elem.getConversionFactor() == 0) {
				ConversionElements compatibles = getCompatibleElements(elem);

				if(compatibles.size() > 1)
					for (ConversionElement elem2 : compatibles.getConversionElements())
						for(ConversionElement elem3 : compatibles.getConversionElements())
							if (hasLeafInCommon(elem2, elem3)) {
								double newConvFact = elem2.getConversionFactor() * elem3.getConversionFactor();
								Couple couple = elem.getCouple();
								replace(couple, newConvFact);
								replace(new Couple(elem.getCouple().getSecondLeaf(), elem.getCouple().getFirstLeaf()), 1 / newConvFact);
							}
			}
	}

	public ConversionElements getCompatibleElements(ConversionElement conversionElement) {
		ConversionElements compatibles = new ConversionElements();
		for(ConversionElement elem : list)
			if(elem.getConversionFactor() != 0)
				if((conversionElement.getCouple().getFirstLeaf().equals(elem.getCouple().getFirstLeaf()) && !conversionElement.getCouple().getFirstLeaf().equals(elem.getCouple().getSecondLeaf()))
						|| 
						(conversionElement.getCouple().getSecondLeaf().equals(elem.getCouple().getSecondLeaf()) && !conversionElement.getCouple().getFirstLeaf().equals(elem.getCouple().getFirstLeaf())))
				{
					compatibles.addFactConv(elem);
				}
		return compatibles;
	}

	public boolean hasLeafInCommon(ConversionElement elem1, ConversionElement elem2) {
		return (elem1.getCouple().getSecondLeaf().equals(elem2.getCouple().getFirstLeaf()) || elem1.getCouple().getFirstLeaf().equals(elem2.getCouple().getSecondLeaf()));
	}

	public void initializeNewConvElements(Hierarchies hierarchies) {
		for (int i = 0; i < hierarchies.getLeaves().size(); i++)
			for (int j = 0; j < hierarchies.getLeaves().size(); j++)
				if(!hierarchies.getLeaves().get(i).getName().equals(hierarchies.getLeaves().get(j).getName()))
				{
					Couple couple = null;
					if (hierarchies.isLeafDuplicated(hierarchies.getLeaves().get(i))) 
					{
						LeafCategory leaf;
						try {
							leaf = hierarchies.getLeaves().get(i).clone();
							leaf.setName(leaf.getName() + " [" + hierarchies.getRoot(leaf).getName() + "]");
							couple = new Couple(leaf, hierarchies.getLeaves().get(j));
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					else if (hierarchies.isLeafDuplicated(hierarchies.getLeaves().get(j))) {
						LeafCategory leaf;
						try {
							leaf = hierarchies.getLeaves().get(j).clone();
							leaf.setName(leaf.getName() + " [" + hierarchies.getRoot(leaf).getName() + "]");
							couple = new Couple(hierarchies.getLeaves().get(i),leaf);
						} catch (CloneNotSupportedException e) {
							e.printStackTrace();
						}
					}
					else {
						couple = new Couple(hierarchies.getLeaves().get(i), hierarchies.getLeaves().get(j));
					}
					if (!isCouplePresent(couple))
						addFactConv(new ConversionElement(couple, 0.));
				}
	}

	public ConversionElements clone() throws CloneNotSupportedException {
		ConversionElements cloned = (ConversionElements) super.clone();

		cloned.list = new ArrayList<>();
		for(ConversionElement elem : list)
			cloned.addFactConv(elem.clone());

		return cloned;
	}

	@Override
	public void write(String filepath) throws FileNotFoundException, IOException {
		File file = new File(filepath);
		if (file.createNewFile()) {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(list);
			oos.close();
		} else {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(list);
			oos.close();
		}
	}

	@Override
	public void read(String filepath) {
		try {
			File file = new File(filepath);
			if (!file.createNewFile()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				list = (List<ConversionElement>) ois.readObject();
				ois.close();
			}
		} catch (EOFException e) {

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
