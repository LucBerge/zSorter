package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSortingException;
import fr.zcraft.zsorting.tasks.SortingTask;

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
	private Map<Inventory, Bank> inventoryToBank;
	
	/**
	 * Constructor of a bank manager object.
	 */
	public BankManager() {
		this.inventoryToBank = new HashMap<Inventory, Bank>();
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
	 * Returns the map linking a inventory to a bank.<br><br>
	 * Do not use this method if you need to add or remove a bank.
	 * Use the {@code addBank} and {@code deleteBank} methods instead.
	 * @return The banks of the plugin.
	 */
	public Map<Inventory, Bank> getInventoryToBank() {
		return inventoryToBank;
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
		Bank bank = new Bank(name, description);										//Create a new bank
		if(nameToBank.putIfAbsent(name, bank) != null)										//If a bank with this name already exists
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
		if(bank == null)															//If no bank has this name
			throw new ZSortingException(I.t("There is no bank with this name."));		//Display error message
		
		for(Input input:bank.getInventoryToInput().values())							//For each input of the bank
			inventoryToBank.remove(input.getInventory());								//Remove the input from the inventory to bank map
		for(Input input:bank.getInventoryToInput().values())							//For each ouput of the bank
			inventoryToBank.remove(input.getInventory());								//Remove the output from the inventory to bank map
		return bank;
	}
	
	/**
	 * Sets a new bank input.
	 * @param name - Name of the bank.
	 * @param inventory - Input inventory.
	 * @param priority - Priority of the input.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	public void setInput(String name, Inventory inventory, int priority) throws ZSortingException {
		Bank bank = nameToBank.get(name);
		if(bank == null)
			throw new ZSortingException(I.t("There is no bank with this name."));

		Bank existingBank = inventoryToBank.putIfAbsent(inventory, bank);											//Get the bank with this input
		if(existingBank != null && !bank.equals(existingBank))															//If the bank is not this one
			throw new ZSortingException(I.t("This holder is already in use by the bank {0}.", bank.getName()));			//Display error messsage
		
		bank.setInput(inventory, priority);
	}
	
	/**
	 * Remove an input from a bank.
	 * @param name - Name of the bank.
	 * @param inventory - Inventory of the input.
	 * @return The removed input object, {@code null} if no input found for this inventory.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	public Input removeInput(String name, Inventory inventory) throws ZSortingException {
		Bank bank = nameToBank.get(name);
		if(bank == null)
			throw new ZSortingException(I.t("There is no bank with this name."));
		
		Input input = bank.removeInput(inventory);
		if(input == null)
			throw new ZSortingException(I.t("This holder is not an input."));
			
		inventoryToBank.remove(inventory);			//Unkink the bank
		return input;
	}
	
	/**
	 * Sets a new bank output.
	 * @param name - Name of the bank.
	 * @param inventory - Output inventory.
	 * @param priority - Priority of the output.
	 * @param materials - Materials of the output.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	public void setOutput(String name, Inventory inventory, int priority, List<Material> materials) throws ZSortingException {
		Bank bank = nameToBank.get(name);
		if(bank == null)
			throw new ZSortingException(I.t("There is no bank with this name."));

		Bank existingBank = inventoryToBank.putIfAbsent(inventory, bank);											//Get the bank with this input
		if(existingBank != null && !bank.equals(existingBank))															//If the bank is not this one
			throw new ZSortingException(I.t("This holder is already in use by the bank {0}.", bank.getName()));			//Display error messsage
		
		bank.setOutput(inventory, priority, materials);
	}
	
	/**
	 * Remove an output from a bank.
	 * @param name - Name of the bank.
	 * @param inventory - Inventory of the output.
	 * @return The removed output object, {@code null} if no output found for this inventory.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	public Output removeOutput(String name, Inventory inventory) throws ZSortingException {
		Bank bank = nameToBank.get(name);
		if(bank == null)
			throw new ZSortingException(I.t("There is no bank with this name."));
		
		Output output = bank.removeOutput(inventory);
		if(output == null)
			throw new ZSortingException(I.t("This holder is not an output."));
		
		inventoryToBank.remove(inventory);									//Unkink the bank
		return output;
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
	
	/**
	 * Compute the bank associated with this inventory.
	 * Don't do anything if the inventory is not an input.
	 * @param inventory - Inventory of the bank to compute.
	 * @return {@code true} if the bank has been computed, {@code false} otherwise.
	 */
	public boolean computeBank(Inventory inventory) {
		boolean computed = false;
		Bank bank = inventoryToBank.get(inventory);						//Get the bank associated with this inventory
		if(bank != null && bank.isEnable()) {							//If bank found and enable
			Input input = bank.getInventoryToInput().get(inventory);		//Get the input linked to this inventory
			if(input != null) {												//If input found
				bank.setToCompute(true);										//Set the bank to compute
				SortingTask.getInstance().start();								//Start the task
				computed = true;
			}
			else {
				Output output = bank.getInventoryToOutput().get(inventory);		//Get the output linked to this inventory
				if(output != null) {											//If output found
					
					boolean clogging = output.getMaterials()
							.stream()
							.filter(bank.getCloggingUpMaterials()::contains)
							.count() > 0;

					if(clogging || output.isOverflow()) {							//If one of the output material was clogging up the inputs or if it is an overflow
						bank.setToCompute(true);										//Set the bank to compute
						SortingTask.getInstance().start();								//Start the task
						computed = true;
					}
				}
			}
		}
		return computed;
	}
}
