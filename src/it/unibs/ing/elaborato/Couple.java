package it.unibs.ing.elaborato;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
/**
 * Classe che sintetizza l'idea di coppia di categorie foglia.
 */
public class Couple implements Serializable, Cloneable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private LeafCategory firstLeaf;
	private LeafCategory secondLeaf;
	
	public Couple(LeafCategory leaf1, LeafCategory leaf2)
	{
		this.firstLeaf = leaf1;
		this.secondLeaf = leaf2;
	}

	public LeafCategory getFirstLeaf()
	{
		return firstLeaf;
	}

	public LeafCategory getSecondLeaf()
	{
		return secondLeaf;
	}
	
	@Override
    public boolean equals(Object o)
	{
        if (this == o) return true;
        if (!(o instanceof Couple couple)) return false;
        return this.getFirstLeaf().equals(couple.getFirstLeaf()) && this.getSecondLeaf().equals(couple.getSecondLeaf());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(firstLeaf, secondLeaf);
    }
    
    public Couple clone() throws CloneNotSupportedException
    {
    	Couple cloned = (Couple) super.clone();
    	
    	cloned.firstLeaf = (LeafCategory) firstLeaf.clone();
    	cloned.secondLeaf = (LeafCategory) secondLeaf.clone();
    	
    	return cloned;
    }

}
