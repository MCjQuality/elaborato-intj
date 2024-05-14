package it.unibs.ing.elaborato;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
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

	public ConversionElements()
	{
		list = new ArrayList<>();
	}

	public void addFactConv(ConversionElement elem)
	{
		list.add(elem);
	}

	public boolean isCouplePresent(Couple couple)
	{
		return list.stream().anyMatch(s -> s.getCouple().equals(couple));
	}

	public int size()
	{
		return list.size();
	}

	public boolean isFactConvPresent(Double factConv)
	{
		return list.stream().anyMatch(s -> s.getConversionFactor() == factConv);
	}

	public void replace(Couple coupleToFind, double l)
	{
		list.stream().filter(s -> s.getCouple().equals(coupleToFind)).forEach(s -> s.setConversionFactor(l));
	}

	public List<ConversionElement> getRemainingConversionElements()
	{
		return list.stream().filter(s -> s.getConversionFactor() == 0).toList();
	}

	public double[] getConversionFactorRange(Couple elem)
	{
		double [] x = new double [] {2.1, 0.4};
		ArrayList<ConversionElement> lista = (ArrayList<ConversionElement>)getConversionElementsCompatible(elem);

		if(lista.isEmpty())
		{
			x[0] = 0.5;
			x[1] = 2.0;
			return x;
		}
		else
		{
			for(ConversionElement conv : lista)
			{
				if(conv.getConversionFactor() < x[0])
					x[0] = conv.getConversionFactor();
				if(conv.getConversionFactor() > x[1])
					x[1] = conv.getConversionFactor();
			}
			x[0] = Math.ceil((0.5 / x[0]) * 100) / 100.;
			x[1] = Math.floor((2 / x[1]) * 100) / 100.;
			if(x[0] < 0.5)
				x[0] = 0.5;
			if(x[1] > 2.0)
				x[1] = 2.0;

			return x;
		}
	}

	public List<ConversionElement> getConversionElementsCompatible(Couple elem)
	{
		ArrayList<ConversionElement> lista = new ArrayList<>();
		for(ConversionElement conv : list) {
			if(conv.getConversionFactor() != 0.0 && (elem.getFirstLeaf().equals(conv.getCouple().getSecondLeaf()) 
					|| elem.getSecondLeaf().equals(conv.getCouple().getFirstLeaf())))
			{
					lista.add(conv);
			}
		}
		return lista;
	}
	
	public List<ConversionElement> getConversionElements()
	{
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
	public void automaticConvFactCalculate()
	{
		for(ConversionElement elem : list)
			if (elem.getConversionFactor() == 0)
			{
				ConversionElements compatibles = getCompatibleElements(elem);

				if(compatibles.size() > 1)
					for (ConversionElement elem2 : compatibles.getConversionElements())
						for(ConversionElement elem3 : compatibles.getConversionElements())
							if (hasLeafInCommon(elem2, elem3))
							{
								double newConvFact = (elem2.getConversionFactor() * elem3.getConversionFactor());
								Couple couple = elem.getCouple();
								replace(couple, newConvFact);
								replace(new Couple(elem.getCouple().getSecondLeaf(), elem.getCouple().getFirstLeaf()), (1 / newConvFact));
							}
			}
	}

	public ConversionElements getCompatibleElements(ConversionElement conversionElement)
	{
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

	public boolean hasLeafInCommon(ConversionElement elem1, ConversionElement elem2)
	{
		return (elem1.getCouple().getSecondLeaf().equals(elem2.getCouple().getFirstLeaf()) || elem1.getCouple().getFirstLeaf().equals(elem2.getCouple().getSecondLeaf()));
	}


	public void update(Hierarchies hierarchies) throws CloneNotSupportedException
	{
		for(LeafCategory leaf : hierarchies.getLeaves())
			if(hierarchies.isLeafDuplicated(leaf))
				for(ConversionElement elem : list)
				{

					if(elem.getCouple().getFirstLeaf().equals(leaf) && elem.getCouple().getSecondLeaf().equals(leaf))
					{
						LeafCategory cloned1 = elem.getCouple().getFirstLeaf().clone();
						LeafCategory cloned2 = elem.getCouple().getSecondLeaf().clone();
						cloned1.setName(leaf.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(leaf).getName() + Constants.SQUARE_BRACKETS2);
						cloned2.setName(leaf.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(leaf).getName() + Constants.SQUARE_BRACKETS2);
						elem.setCouple(new Couple(cloned1, cloned2));
					}
					else if(elem.getCouple().getFirstLeaf().equals(leaf))
					{
						LeafCategory cloned = elem.getCouple().getFirstLeaf().clone();
						cloned.setName(leaf.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(leaf).getName() + Constants.SQUARE_BRACKETS2);
						elem.setCouple(new Couple(cloned, elem.getCouple().getSecondLeaf()));
					}
					else if (elem.getCouple().getSecondLeaf().equals(leaf))
					{
						LeafCategory cloned = elem.getCouple().getSecondLeaf().clone();
						cloned.setName(leaf.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(leaf).getName() + Constants.SQUARE_BRACKETS2);
						elem.setCouple(new Couple(elem.getCouple().getFirstLeaf(), cloned));
					}
				}
	}

	public void initialize(Hierarchies hierarchies) throws CloneNotSupportedException
	{
		for (int i = 0; i < hierarchies.getLeaves().size(); i++)
			for (int j = 0; j < hierarchies.getLeaves().size(); j++)
				if(!hierarchies.getLeaves().get(i).getName().equals(hierarchies.getLeaves().get(j).getName()))
				{
					Couple couple;
					if(hierarchies.isLeafDuplicated(hierarchies.getLeaves().get(i)) && hierarchies.isLeafDuplicated(hierarchies.getLeaves().get(j)))
					{
						LeafCategory leaf1 = hierarchies.getLeaves().get(i).clone();
						LeafCategory leaf2 = hierarchies.getLeaves().get(j).clone();
						leaf1.setName(leaf1.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(hierarchies.getLeaves().get(i)).getName() + Constants.SQUARE_BRACKETS2);
						leaf2.setName(leaf2.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(hierarchies.getLeaves().get(j)).getName() + Constants.SQUARE_BRACKETS2);
						couple = new Couple(leaf1, leaf2);
					}
					else if (hierarchies.isLeafDuplicated(hierarchies.getLeaves().get(i)))
					{
						//se e' la prima foglia e' duplicata devo controllare gli elementi di conv in cui compare come PRIMA o SECONDA componente
						// (e quindi aggiungerci la radice)
						//oppure se non e' presente in nessun elemento di conv crearne uno
						LeafCategory leaf = hierarchies.getLeaves().get(i).clone();
						leaf.setName(leaf.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(hierarchies.getLeaves().get(i)).getName() + Constants.SQUARE_BRACKETS2);
						couple = new Couple(leaf, hierarchies.getLeaves().get(j));

					}
					else if(hierarchies.isLeafDuplicated(hierarchies.getLeaves().get(j)))
					{
						LeafCategory leaf = hierarchies.getLeaves().get(j).clone();
						leaf.setName(leaf.getName() + Constants.SQUARE_BRACKETS1 + hierarchies.getRoot(hierarchies.getLeaves().get(j)).getName() + Constants.SQUARE_BRACKETS2);
						couple = new Couple(hierarchies.getLeaves().get(i), leaf);
					}
					else
						couple = new Couple(hierarchies.getLeaves().get(i), hierarchies.getLeaves().get(j));
					if (!isCouplePresent(couple))
						addFactConv(new ConversionElement(couple, 0.));
				}
	}

	public ConversionElements clone() throws CloneNotSupportedException
	{
		ConversionElements cloned = (ConversionElements) super.clone();

		cloned.list = new ArrayList<>();
		for(ConversionElement elem : list)
			cloned.addFactConv(elem.clone());

		return cloned;
	}

	@Override
	public void write(String filepath) throws IOException
	{
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
	public void read(String filepath)
	{
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
