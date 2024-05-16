package it.unibs.ing.elaborato;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ExchangeProposal implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private Couple couple;
	private int hoursRequest;
	private int hoursOffered;
	private Consumer owner;
	private State currentState;
	private List<State> pastStates;
	
	public ExchangeProposal(Couple couple, int hoursRequest, Consumer owner, ConversionElements conversionElements) 
	{
		this.couple = couple;
		this.hoursRequest = hoursRequest;
		this.hoursOffered = calculateHoursOffered(conversionElements);
		this.owner = owner;
		this.currentState = State.OPEN;
		this.pastStates = new ArrayList<>();
	}
	
	public void addTransition(State state)
	{
		pastStates.add(state);
	}
	
	public int calculateHoursOffered(ConversionElements conversionElements) 
	{
		double factConv = conversionElements
				.getConversionElements()
				.stream()
				.filter(elem -> elem.getCouple().equals(couple))
				.toList()
				.getFirst()
				.getConversionFactor();
		return (int) Math.round(hoursRequest * factConv);
	}
	
	public Couple getCouple() 
	{
		return couple;
	}

	public int getHoursRequest() 
	{
		return hoursRequest;
	}

	public int getHoursOffered() 
	{
		return hoursOffered;
	}

	public Consumer getOwner() 
	{
		return owner;
	}

	public State getState() 
	{
		return currentState;
	}
	
	public void changeStatusToWithdrawn() 
	{
		addTransition(currentState);
		this.currentState = State.WITHDRAWN;
	}


	public void changeStatusToClosed() 
	{
		addTransition(currentState);
		this.currentState = State.CLOSED;
	}

	@Override
    public boolean equals(Object o) 
	{
        if (this == o) return true;
        if (!(o instanceof ExchangeProposal proposal)) return false;
        return this.getCouple().equals(proposal.getCouple()) && this.getHoursOffered() == proposal.getHoursOffered() && this.getHoursRequest() == proposal.getHoursRequest() && this.getOwner().equals(proposal.getOwner()) && this.getState().equals(proposal.getState());
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(getCouple(), getHoursOffered(), getHoursRequest(), getOwner(), getState());
    }
    
}
