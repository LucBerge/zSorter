package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * The class {@code BankManager} is used to manage banks.
 * @author Lucas
 */
public class BankManager implements Serializable{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1782855927147248287L;
	
	private Map<String, Bank> banks;
	
	/**
	 * Constructor of a bank manager object.
	 */
	public BankManager() {
		this.banks = new TreeMap<String, Bank>();
	}
	
	/**
	 * Returns the banks of the plugin.
	 * @return The banks of the plugin.
	 */
	public Map<String, Bank> getBanks() {
		return banks;
	}
}
