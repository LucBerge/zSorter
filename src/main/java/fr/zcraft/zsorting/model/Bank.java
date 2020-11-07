package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.components.rawtext.RawTextPart;
import fr.zcraft.zsorting.ZSortingException;

/**
 * The class {@code Bank} represents a bank in the game.<br><br>
 * Each bank has a name, a short description, inputs and outputs.
 * 
 * @author Lucas
 */
public class Bank implements Serializable{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7893484799034097907L;

	private BankManager manager;
	private String name;
	private String description;	
	private boolean enable;
	private boolean toCompute;
	
	private Map<Inventory, Input> inventoryToInput;
	private Map<Inventory, Output> inventoryToOutput;
	
	private List<Input> inputs;
	private Map<Material, List<Output>> materialToOutputs;
	private List<Output> overflows;
	
	/**
	 * Constructor of a bank.
	 * @param manager - The bank manager.
	 * @param name - Name of the bank.
	 * @param description - Short description of the bank.
	 */
	public Bank(BankManager manager, String name, String description) {
		super();
		
		if(manager == null)
			throw new IllegalArgumentException("The bank manager cannot be null.");
		
		if(name == null)
			throw new IllegalArgumentException("The bank name cannot be null.");
		
		if(description == null)
			throw new IllegalArgumentException("The bank description cannot be null.");

		this.manager = manager;
		this.name = name;
		this.description = description;
		this.enable = false;
		this.toCompute = false;
		
		this.inventoryToInput = new HashMap<Inventory, Input>();
		this.inventoryToOutput = new HashMap<Inventory, Output>();
		
		this.inputs = new ArrayList<Input>();
		this.materialToOutputs = new TreeMap<Material, List<Output>>();
		this.overflows = new ArrayList<Output>();
	}
	
	/**
	 * Returns the bank manager.
	 * @return The bank manager.
	 */
	public BankManager getManager() {
		return manager;
	}
	
	/**
	 * Sets the bank manager.
	 * @param manager - Bank manager.
	 */
	public void setManager(BankManager manager) {
		this.manager = manager;
	}
	
	/**
	 * Returns the name of the bank.
	 * @return The name of the bank.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the bank.
	 * @param name - Name of the bank.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the description of the bank.
	 * @return Short description of the bank.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description of the bank.
	 * @param description - Short description of the bank.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the state of the bank.
	 * @return {@code true} if the bank is enabled, {@code false} otherwise.
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * Sets the state of the bank.
	 * @param state - {@code true} to enable the bank, {@code false} to disable it.
	 */
	public void setEnable(boolean state) {
		if(state)		//If enabling the bank
			commit();		//Commit the bank
		this.enable = state;
	}

	/**
	 * Checks whether the bank needs be computed.
	 * @return {@code true} if the bank has items to sort, {@code false} otherwise.
	 */
	public boolean isToCompute() {
		return toCompute;
	}

	/**
	 * Sets the compute state of the bank.
	 * @param toCompute - {@code true} to specify that the bank has items to be sorted, {@code false} otherwise.
	 */
	public void setToCompute(boolean toCompute) {
		this.toCompute = toCompute;
	}

	/**
	 * Returns the bank inputs.
	 * Do not use this method if you need to add or remove an input.
	 * Use the {@code setInput} and {@code removeInput} methods instead.
	 * @return The bank inputs.
	 */
	public Map<Inventory, Input> getInventoryToInput() {
		return inventoryToInput;
	}

	/**
	 * Returns the bank outputs.
	 * Do not use this method if you need to add or remove an output.
	 * Use the {@code setOutput} and {@code removeOutput} methods instead.
	 * @return The bank outputs.
	 */
	public Map<Inventory, Output> getInventoryToOutput() {
		return inventoryToOutput;
	}

	/**
	 * Returns the inputs of the bank.
	 * @return List of inputs sorted by priority.
	 */
	public List<Input> getInputs() {
		return inputs;
	}

	/**
	 * Returns the map linking a material with all its possible outputs.
	 * @return Material to outputs mapping.
	 */
	public Map<Material, List<Output>> getMaterialToOutputs() {
		return materialToOutputs;
	}

	/**
	 * Returns the overflows of the system. An overflow is an output without any material specified.
	 * @return List of overflow sorted by priority.
	 */
	public List<Output> getOverflows() {
		return overflows;
	}

	/**
	 * Sorts the input outputs by order of priority.
	 */
	public void commit() {
		inputs = inventoryToInput.values().stream().collect(Collectors.toList());
		Collections.sort(inputs);											//Sort the inputs

		overflows = inventoryToOutput.values().stream().filter(o -> o.isOverflow()).collect(Collectors.toList());
		Collections.sort(overflows);										//Sort the overflows
		
		materialToOutputs = new HashMap<Material, List<Output>>();
		for(Output output:inventoryToOutput.values()) {						//For each output
			for(Material material:output.getMaterials()) {						//For each material
				List<Output> possibleOutputs = materialToOutputs.get(material);		//Get the possible outputs for the given material
				if(possibleOutputs == null) {										//If none have been found
					possibleOutputs = new ArrayList<Output>();							//Create a new one
					materialToOutputs.put(material, possibleOutputs);					//Add it to the map
				}
				possibleOutputs.add(output);										//Add the output
			}
		}
		for(List<Output> outputs:materialToOutputs.values()) {		//For each material outputs
			Collections.sort(outputs);									//Sort the outputs
		}
	}
	
	/**
	 * Sets the inventory has an input.<br><br>
	 * If the input already exists, the priority is updated.
	 * @param inventory - Inventory of the input.
	 * @param priority - Priority of the input.
	 * @return The created input object.
	 * @throws ZSortingException if the input is already linked to an other bank.
	 */
	public Input setInput(Inventory inventory, int priority) throws ZSortingException {
		setEnable(false);																							//Disable the bank
		
		Bank bank = manager.getInventoryToBank().putIfAbsent(inventory, this);										//Get the bank with this input
		if(bank != null && !equals(bank))																			//If the bank is not this one
			throw new ZSortingException(I.t("This holder is already an input of the bank {0}.", bank.getName()));		//Display error messsage
		
		Input existingInput = inventoryToInput.get(inventory);															//Get the existing input
    	if(existingInput == null) {																						//If no input exists
    		existingInput = new Input(this, inventory, priority);															//Create a new input
    		inventoryToInput.put(inventory, existingInput);																	//Add the new input
    	}
    	else {																										//If the input exists
    		existingInput.setPriority(priority);																				//Set the new priority
    	}
		return existingInput;
	}

	/**
	 * Remove an input from a bank.
	 * @param inventory - Inventory of the input.
	 * @return The removed input object, {@code null} if no input found for this inventory.
	 */
	public Input removeInput(Inventory inventory) {
		setEnable(false);																//Disable the bank
		
		Input input = inventoryToInput.remove(inventory);								//Get the existing input
		if(input != null) {																//If the input has been removed
			manager.getInventoryToBank().remove(inventory);									//Unkink the bank
		}
		return input;
	}
	
	/**
	 * Sets the inventory has an output.<br><br>
	 * If the output already exists, the priority and the materials are updated.
	 * @param inventory - Inventory of the output.
	 * @param priority - Priority of the output.
	 * @param materials - Sorted materials of the output.
	 * @return The created output object.
	 * @throws ZSortingException if the output is already linked to an other bank.
	 */
	public Output setOutput(Inventory inventory, int priority, List<Material> materials) throws ZSortingException {
		setEnable(false);																							//Disable the bank

		Bank bank = manager.getInventoryToBank().putIfAbsent(inventory, this);										//Get the bank with this input
		if(bank != null && !equals(bank))																			//If the bank is not this one
			throw new ZSortingException(I.t("This holder is already an output of the bank {0}.", bank.getName()));		//Display error messsage
		
		Output existingOutput = inventoryToOutput.get(inventory);													//Get the existing output
    	if(existingOutput == null) {																				//If no existing output
    		existingOutput = new Output(this, inventory, priority);														//Create a new output
    		existingOutput.setMaterials(materials); 																	//Add the materials
    		inventoryToOutput.put(inventory, existingOutput);															//Add the new output
    	}
    	else {																										//If the output exists
    		existingOutput.setPriority(priority);																		//Set the new priority
    		existingOutput.setMaterials(materials); 																	//Set the new materials
    	}
		return existingOutput;
	}
	
	/**
	 * Remove an output from a bank.
	 * @param inventory - Inventory of the output.
	 * @return The removed output object, {@code null} if no output found at this inventory.
	 */
	public Output removeOutput(Inventory inventory) {
		setEnable(false);														//Disable the bank
		
		Output output = inventoryToOutput.remove(inventory);					//Get the existing output
		if(output != null) {													//If the output exists
			manager.getInventoryToBank().remove(inventory);							//Unkink the bank
		}
		return output;
	}
	
	/**
	 * Find all the possible outputs for a given material
	 * @param material - Material to sort.
	 * @return List of possible outputs sorted by priority.
	 */
	public List<Output> findOutputs(Material material){
		List<Output> possibleOutputs = new ArrayList<Output>();			//Define a new list containing the possible outputs
		List<Output> materialOutputs = materialToOutputs.get(material);	//Find the outputs for this item
		if(materialOutputs != null)										//If other outputs are possible
			possibleOutputs.addAll(materialOutputs);						//Add the outputs
		possibleOutputs.addAll(overflows);								//Add the overflows
		Collections.sort(possibleOutputs);								//Sort the output by priority
		return possibleOutputs;
	}
	
	/**
	 * Checks whether a bank has at least one overflow.
	 * @return {@code true} if the bank has an overflow, {@code false} otherwise.
	 */
	public boolean hasOverflow() {
		return overflows.size() > 0;
	}
	
	/**
	 * Compute sorting on the bank.
	 */
	public void computeSorting() {
		if(isEnable()) {																						//If the bank is ON
    		for(Input input:inputs) {																				//For each input in the bank
    			Inventory inputInventory = input.getInventory();														//Get the input inventory
    			for(ItemStack itemStack: inputInventory.getContents()) {												//For each item in the input inventory
    				if(itemStack != null) {																					//If the item is not null
    					List<Output> outputs = findOutputs(itemStack.getType());												//Find the outputs for this item
    					for(Output output:outputs) {																				//For each possible output
    						Inventory outputInventory = output.getInventory();															//Get the output inventory
    						ItemStack itemStackToTransfer = itemStack.clone();															//Clone the item to keep the meta data
    						itemStackToTransfer.setAmount(1);																			//Transfer only one item
    						HashMap<Integer, ItemStack> couldntTransfer = outputInventory.addItem(itemStackToTransfer);					//Add the item to the output
    						if(couldntTransfer.isEmpty()) {																				//If it has been transfered
    							inputInventory.removeItem(itemStackToTransfer);																//Remove the item from the input
    							return;																										//Exit
    						}
    					}
    				}
    			}
    		}
    		
    		//Run only if no item has been sorted. Either because there is nothing to sort or because all the outputs are full.
    		//THIS MIGHT BE AN ISSUE :
    		//	1. Create a bank
    		//	2. Add an input
    		//	3. Add an output of STONE
    		//	4. Completely fill the input of STONE
    		//	5. Completely fill the output of STONE
    		//	6. Toggle the bank
    		//	7. Nothing is sorted and the following line is run. The bank does not need to be computed anymore.
    		//	8. Remove the STONE from the output
    		//	9. The input is full and the output is empty but nothing is sorted
    		
    		toCompute = false;
		}
	}
	
	/**
	 * Returns the bank as a RawText to display it.
	 * @return Bank as RawText.
	 */
	public RawText toRawText() {
		commit();
		RawTextPart text = new RawText("")
    			.then(name)
    				.style(ChatColor.GOLD)
    			.then(" (" + description + ") ")
    				.color(ChatColor.GRAY)
    			.then(isEnable() ? "ON" : "OFF")
    				.color(enable ? ChatColor.GREEN : ChatColor.RED)
        		.then(I.t("\n  {0} inputs", inventoryToInput.size()))
    				.color(ChatColor.GRAY);

		for(Input input:inventoryToInput.values()) {
			text
				.then("\n    (" + input.getPriority() + ")")
				.color(ChatColor.AQUA)
				.hover(
					new RawText()
						.then(String.format("X=%1$,.0f\nY=%1$,.0f\nZ=%1$,.0f", input.getInventory().getLocation().getX(), input.getInventory().getLocation().getY(), input.getInventory().getLocation().getZ()))
							.color(ChatColor.AQUA)
				);
    	}

    	text
    		.then(I.t("\n  {0} outputs", inventoryToOutput.size()))
    			.color(ChatColor.GRAY);
    	
    	for(Output output:inventoryToOutput.values()) {
    		String materials = "*";
    		if(!output.getMaterials().isEmpty()) {
            	StringJoiner joiner = new StringJoiner(" ");
            	for(Material material:output.getMaterials())
            		joiner.add(material.name().toLowerCase());
            	materials = joiner.toString();
    		}
    		text
    			.then(String.format("\n    (%d)", output.getPriority()))
    				.color(ChatColor.AQUA)
    				.hover(
    	    			new RawText()
        					.then(String.format("X=%1$,.0f\nY=%1$,.0f\nZ=%1$,.0f", output.getInventory().getLocation().getX(), output.getInventory().getLocation().getY(), output.getInventory().getLocation().getZ()))
        						.color(ChatColor.AQUA)
        			)
    			.then(String.format(" (%s)",materials))
    				.color(ChatColor.GRAY);
    	}
    	return text.build();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bank other = (Bank) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
