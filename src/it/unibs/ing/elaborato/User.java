package it.unibs.ing.elaborato;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe che simbolizza un possibile utente dell'applicazione.
 * Ricordiamo che l'utente puo' poi "specializzarsi" in configuratore oppure, nelle successive versioni, fruitore.
 */
public abstract class User implements Serializable {
	
	@Serial
	private static final long serialVersionUID = 1L;
	private String username;
	private String psw;
	
	public User(String username, String psw)
	{
		this.username = username;
		this.psw = psw;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPsw()
	{
		return psw;
	}
		
	public void updateCredentials(String newUsername, String newPsw)
	{
		username = newUsername;
		psw = newPsw;
	}

	public boolean checkUser(String username_in, String psw_in)
	{
		return username.equals(username_in) && BCrypt.checkpw(psw_in, psw);
	}
	
	public boolean checkUser(String username_in)
	{
		return username.equals(username_in);
	}

	public boolean hasValidCredential(String newUsername, String newPsw)
	{
		return !username.equals(newUsername) && BCrypt.checkpw(newPsw, psw);
	}

	abstract boolean isConsumer();
	
	@Override
    public boolean equals(Object o)
	{
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return getUsername().equals(user.getUsername()) && getPsw().equals(user.getPsw());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getUsername(), getPsw());
    }
	
}
