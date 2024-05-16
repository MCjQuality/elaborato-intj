package it.unibs.ing.elaborato;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * il file ha 3 colonne:
 * - string username
 * - string criptata psw
 * - boolean first access o meno
 * si prevede l'estensione ad un ulteriore colonna che presenti la differenziazione tra fruitore e config
 */ 


/**
 * La classe mira a rappresentare la lista di utenti correttamente registrati.
 * Contiene inoltre metodi che svolgono controlli, modifiche, letture e salvataggi al file che contiene la lista delle utenze.
 */
public class Users implements Readable, Writable{

	private List<User> users;

	public Users()
	{
		users = new ArrayList<>();
	}
	
	public void add(User user)
	{
		users.add(user);
	}

	public boolean checkUser(String username, String psw)
	{
		return users.stream().anyMatch(user -> user.checkUser(username, psw));
	}

	public boolean contains(String username)
	{
		return users.stream().anyMatch(user -> user.checkUser(username));
	}

	public User getUser(String username, String psw)
	{
		for(User user : users)
			if(user.checkUser(username, psw))
				return user;
		return null;
	}

	@Override
	public void read(String filepath)
	{
		try
		{
			Scanner scanner = new Scanner(new File(filepath));
			while (scanner.hasNextLine()) 
			{
				String line = scanner.nextLine();
				String[] parts = line.split(":");
				String username = parts[0].trim();
				String psw = parts[1].trim();
				boolean firstAccess = Boolean.parseBoolean(parts[2].trim());
				String district = parts[3].trim();
				String email = parts[4].trim();
				if(!district.equals("null"))
					users.add(new Consumer(username, psw, district, email));
				else
					users.add(new Configurator(username, psw, firstAccess));
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void write(String filepath) throws IOException
	{
		File file = new File(filepath);
		FileWriter out = new FileWriter(file);
		for(User user : users) {
			if(user instanceof Configurator configurator)
			{
                out.write(user.getUsername() + ":" + user.getPsw() + ":" + configurator.getFirstAccess() + ":" + "null" + ":" + "null" + Constants.NEW_LINE);
			}
			else
			{
				Consumer consumer = (Consumer) user;
				out.write(consumer.getUsername() + ":" + consumer.getPsw() + ":" + "null" + ":" + consumer.getDistrict() + ":" + consumer.getEmail() + Constants.NEW_LINE);
			}
		}
		out.close();
	}

}
