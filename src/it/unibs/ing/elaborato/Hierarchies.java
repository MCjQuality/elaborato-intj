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
 * Classe che organizza l'insieme delle gerarchie.
 */
public class Hierarchies implements Readable, Writable {

	private List<NotLeafCategory> hierarchies;

	public Hierarchies() {
		this.hierarchies = new ArrayList<>();
	}

	public void addHierarchy(NotLeafCategory root) {
		hierarchies.add(root);
	}

	public List<NotLeafCategory> getHierarchies() {
		return hierarchies;
	}

	public List<LeafCategory> getLeaves() {
		List<LeafCategory> leavesInAllHierarchies = new ArrayList<>();

		for(NotLeafCategory root : hierarchies)
			for(Category elem : root.getLeaves())
				leavesInAllHierarchies.add((LeafCategory) elem);

		return leavesInAllHierarchies;
	}

	public List<LeafCategory> differentiateLeaves() throws CloneNotSupportedException
	{
		List<LeafCategory> leaves = new ArrayList<>();

		for(LeafCategory elem : getLeaves())
		{
			if(isLeafDuplicated(elem))
			{
				LeafCategory leaf = elem.clone();
				leaf.setName(elem.getName() + Constants.SQUARE_BRACKETS1 + getRoot(elem).getName() + Constants.SQUARE_BRACKETS2);
				leaves.add(leaf);
			}
			else
				leaves.add(elem);
		}
		return leaves;
	}

	public boolean isLeafPresent(String name) {
		return getLeaves().stream().anyMatch(leaf -> leaf.getName().equals(name));
	}

	public boolean containsRoot(String name) {
		return hierarchies.stream().anyMatch(notLeaf -> notLeaf.getName().equals(name));
	}

	public NotLeafCategory getRoot(LeafCategory leaf)
	{
		for(NotLeafCategory root : hierarchies) 
			if(root.contains(leaf))
				return root;
		return null;
	}

	public boolean isLeafDuplicated(LeafCategory leaf) {
		return getLeaves().stream().filter(s -> leaf.getName().equals(s.getName())).count() > 1;
	}
	
	public LeafCategory findLeaf(String name) {
		return getLeaves().stream().filter(leaf -> leaf.getName().contentEquals(name)).toList().getFirst();
	}

	@Override
	public void read(String filepath) {
		try {
			File file = new File(filepath);
			if (!file.createNewFile()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
				hierarchies = (List<NotLeafCategory>) ois.readObject();
				ois.close();
			}
		} catch (EOFException e) {

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String filepath)  throws IOException {
		File file = new File(filepath);
		if (file.createNewFile()) {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(hierarchies);
			oos.close();
		} else {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(hierarchies);
			oos.close();
		}
	}

}
