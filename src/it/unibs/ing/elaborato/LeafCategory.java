package it.unibs.ing.elaborato;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Una categoria foglia che non presenta sotto-categorie.
 */
public class LeafCategory extends Category implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;

	public LeafCategory(String name, String domain, String description) {
		super(name, domain, description);
	}

	public LeafCategory clone() throws CloneNotSupportedException {
		LeafCategory cloned = (LeafCategory) super.clone();
 
		return cloned;
	}

	@Override
	List<Category> getLeaves() {
		List<Category> leaf = new ArrayList<>();
		leaf.add(this);
		
		return leaf;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	/**
	 * Fornisce la lista di figli, che pero' e' vuota, essendo una categoria foglia.
	 * @return una lista di categorie vuota.
	 */
	@Override
	List<Category> getChildren()
	{
		return new ArrayList<Category>();
	}

	@Override
	boolean contains(String leafName)
	{
		return this.getName().equals(leafName);
	}

	@Override
	public boolean contains(LeafCategory leaf)
	{
		return this.equals(leaf);
	}

//	@Override
//    public boolean equals(Object o)
//	{
//        if (this == o) return true;
//        if (!(o instanceof LeafCategory)) return false;
//        LeafCategory leaf = (LeafCategory) o;
//        return this.getName().equals(leaf.getName()) && this.getDomain().equals(leaf.getDomain());
//    }
//
//    @Override
//    public int hashCode()
//    {
//        return Objects.hash(getName(), getDomain(), getDescription());
//    }

}
