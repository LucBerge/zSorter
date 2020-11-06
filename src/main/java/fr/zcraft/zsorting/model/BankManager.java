package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.inventory.Inventory;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSortingException;

/**
 * The class {@code BankManager} is used to manage banks.
 * @author Lucas
 */
public class BankManager implements Serializable{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1782855927147248287L;
	
	private Map<String, Bank> nameToBank;
	private Map<Inventory, Bank> locationToBank;
	
	/**
	 * Constructor of a bank manager object.
	 */
	public BankManager() {
		this.locationToBank = new HashMap<Inventory, Bank>();
		this.nameToBank = new TreeMap<String, Bank>();
	}
	
	/**
	 * Returns the map linking a name to a banks.
	 * @return The banks of the plugin.
	 */
	public Map<String, Bank> getNameToBank() {
		return nameToBank;
	}
	
	/**
	 * Returns the map linking a location to a bank.<br><br>
	 * Do not use this method if you need to add or remove a bank.
	 * Use the {@code addBank} and {@code deleteBank} methods instead.
	 * @return The banks of the plugin.
	 */
	public Map<Inventory, Bank> getInventoryToBank() {
		return locationToBank;
	}
	
	/**
	 * Add a new bank to the manager.<br><br>
	 * If the bank already exists, the description is updated.
	 * @param name - Name of the bank.
	 * @param description - Short description of the bank.
	 * @return The created bank object.
	 * @throws ZSortingException if a bank with this name already exists.
	 */
	public Bank addBank(String name, String description) throws ZSortingException {
		Bank bank = new Bank(this, name, description);									//Create a new bank
		if(nameToBank.putIfAbsent(name, bank) != null)									//If a bank with this name already exists
			throw new ZSortingException(I.t("A bank with this name already exists"));
		return bank;
	}
	
	/**
	 * Remove a bank from the manager.
	 * @param name - Name of the bank.
	 * @return The removed bank object, {@code null} if no bank has this name.
	 * @throws ZSortingException  if no bank with this name exists.
	 */
	public Bank deleteBank(String name) throws ZSortingException {
		Bank bank = nameToBank.remove(name);										//Get the existing bank
		if(bank == null)															//If no bank with has this name
			throw new ZSortingException(I.t("There is no bank with this name."));		//Display error message
		
		for(Input input:bank.getLocationToInput().values())							//For each input of the bank
			locationToBank.remove(input.getInventory());								//Remove the input from the location map
		return bank;
	}
	
	/**
	 * Returns a list of banks on which compute sorting.
	 * @return List of banks to compute.
	 */
	public List<Bank> canCompute(){
		return nameToBank
				.values()
				.stream()
				.filter(b -> b.isToCompute())
				.collect(Collectors.toList());
	}
}
