package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Location;

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
	
	/**
	 * Finds a bank from an input location.
	 * @param location - Location of the chest.
	 * @return The bank corresponding to the input, {@code null} if none.
	 */
	public Bank findBankFromInputLocation(Location location) {
		for(Bank bank:banks.values()) {
			if(bank.getInputs().get(location) != null)
				return bank;
		}
		return null;
	}
	
	/**
	 * Finds a bank from an output location.
	 * @param location - Location of the chest.
	 * @return The bank corresponding to the output, {@code null} if none.
	 */
	public Bank findBankFromOutputLocation(Location location) {
		for(Bank bank:banks.values()) {
			if(bank.getOutputs().get(location) != null)
				return bank;
		}
		return null;
	}
}
