package it.unibs.ing.elaborato;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExchangeProposals implements Readable, Writable, Serializable {

	private static final long serialVersionUID = 1L;
	private List<ExchangeProposal> exchangeProposals;

	public ExchangeProposals() 
	{
		this.exchangeProposals = new ArrayList<>();
	}

	public List<ExchangeProposal> getExchangeProposals() 
	{
		return exchangeProposals;
	}

	public void add(ExchangeProposal exchangeProposal) 
	{
		exchangeProposals.add(exchangeProposal); 
	}

	public List<ExchangeProposal> activeProposalBelongOneConsumer(Consumer consumer) 
	{
		return exchangeProposals.stream().filter(s -> s.getOwner().getUsername().equals(consumer.getUsername()) && s.getState().equals(State.OPEN)).toList();
	}
	
	public List<ExchangeProposal> closedProposalBelongOneConsumer(Consumer consumer) 
	{
		return exchangeProposals.stream().filter(s -> s.getOwner().getUsername().equals(consumer.getUsername()) && s.getState().equals(State.CLOSED)).toList();
	}
	
	public List<ExchangeProposal> withdrawnProposalBelongOneConsumer(Consumer consumer) 
	{
		return exchangeProposals.stream().filter(s -> s.getOwner().getUsername().equals(consumer.getUsername()) && s.getState().equals(State.WITHDRAWN)).toList();
	}

	public boolean contains(ExchangeProposal exchangeProposal) 
	{
		return exchangeProposals.contains(exchangeProposal);
	}

	public boolean verifyClosedProposals(ExchangeProposal exchangeProposal, ExchangeProposal fixed, ExchangeProposals closedSet) 
	{
		for (ExchangeProposal elem : exchangeProposals) 
		{
			if (exchangeProposal.getCouple().getFirstLeaf().equals(elem.getCouple().getSecondLeaf()) &&
					exchangeProposal.getHoursRequest() == elem.getHoursOffered() &&
					!(exchangeProposal.getOwner().getUsername().equals(elem.getOwner().getUsername())) &&
					exchangeProposal.getOwner().getDistrict().equals(elem.getOwner().getDistrict()) &&
					exchangeProposal.getState().equals(State.OPEN) &&
					elem.getState().equals(State.OPEN))
			{
				closedSet.add(exchangeProposal);
				if (elem.getCouple().getFirstLeaf().equals(fixed.getCouple().getSecondLeaf()) &&
						elem.getHoursRequest() == fixed.getHoursOffered() &&
						elem.getOwner().getDistrict().equals(fixed.getOwner().getDistrict()) &&
						exchangeProposal.getOwner().getDistrict().equals(elem.getOwner().getDistrict()) &&
						exchangeProposal.getState().equals(State.OPEN) &&
						elem.getState().equals(State.OPEN)) 
				{
					closedSet.add(elem);
					closedSet.changeStatusToClosed();
					return true;
				}
				if (verifyClosedProposals(elem, fixed, closedSet)) { // Se la chiamata ricorsiva trova una corrispondenza, restituisci true
					return true;
				}
			}
		}
		closedSet.clear();
		return false; // Nessuna corrispondenza trovata, restituisci false
	}

	private void clear() 
	{
		exchangeProposals.clear();
	}

	public void changeStatusToClosed() 
	{
		for(ExchangeProposal elem : exchangeProposals) 
			elem.changeStatusToClosed();
	}

	@Override
	public void read(String filepath) 
	{
		try {
			File file = new File(filepath);
			if (!file.createNewFile()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				exchangeProposals = (List<ExchangeProposal>) ois.readObject();
				ois.close();
			}
		} catch (EOFException e) {

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String filepath) throws FileNotFoundException, IOException 
	{
		File file = new File(filepath);
		if (file.createNewFile()) 
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(exchangeProposals);
			oos.close();
		} 
		else 
		{
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(exchangeProposals);
			oos.close();
		}
	}
}
