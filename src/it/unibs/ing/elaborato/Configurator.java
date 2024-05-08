package it.unibs.ing.elaborato;

import java.io.Serializable;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe che estende la classe Utente e punta a rappresentare la figura del configuratore.
 */
public class Configurator extends User implements Serializable{

	private static final long serialVersionUID = 1L;
	private boolean firstAccess;
	
	public Configurator(String username, String psw, boolean firstAccess) {
		super(username, psw);
		this.firstAccess = firstAccess;
	}
	
	public boolean getFirstAccess() {
		return firstAccess;
	}

	public boolean hasValidCredential(String newUsername, String newPsw) {
		return super.hasValidCredential(newUsername, newPsw);
	}

	@Override
	public void updateCredentials(String newUsername, String newPsw) {
		super.updateCredentials(newUsername, BCrypt.hashpw(newPsw, BCrypt.gensalt()));
		firstAccess = false;
	}

	@Override
	boolean isConsumer() {
		return false;
	}

}
