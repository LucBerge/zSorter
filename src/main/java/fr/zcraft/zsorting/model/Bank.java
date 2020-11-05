package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
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
	
	private Map<Location, Input> locationToInput;
	private Map<Location, Output> locationToOutput;
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
		this.locationToInput = new HashMap<Location, Input>();
		this.locationToOutput = new HashMap<Location, Output>();
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
	public Map<Location, Input> getLocationToInput() {
		return locationToInput;
	}

	/**
	 * Returns the bank outputs.
	 * Do not use this method if you need to add or remove an output.
	 * Use the {@code setOutput} and {@code removeOutput} methods instead.
	 * @return The bank outputs.
	 */
	public Map<Location, Output> getLocationToOutput() {
		return locationToOutput;
	}

	/**
	 * Returns the material to output map.
	 * Do not use this method if you need to add or remove an output.
	 * Use the {@code setOutput} and {@code removeOutput} methods instead.
	 * @return The material to output map.
	 */
	public Map<Material, List<Output>> getMaterialToOutputs() {
		return materialToOutputs;
	}

	/**
	 * Returns the bank overflows.
	 * @return The bank overflows.
	 */
	public List<Output> getOverflows() {
		return overflows;
	}

	/**
	 * Sets the location has an input.<br><br>
	 * If the input already exists, the priority is updated.
	 * @param location - Location of the input.
	 * @param priority - Priority of the input.
	 * @return The created input object.
	 * @throws ZSortingException if the input is already linked to an other bank.
	 */
	public Input setInput(Location location, int priority) throws ZSortingException {
		Bank bank = manager.getLocationToBank().putIfAbsent(location, this);										//Get the bank with this input
		if(bank != null && !equals(bank))																			//If the bank is not this one
			throw new ZSortingException(I.t("This holder is already an input of the bank {0}.", bank.getName()));		//Display error messsage
		
		Input input = locationToInput.get(location);																//Get the existing input
    	if(input == null) {																							//If no input exists
    		input = new Input(this, location, priority);																//Create a new input
    		locationToInput.put(location, input);																		//Add the new input
    		manager.getLocationToBank().put(location, this);															//Add the new input location
    	}
    	else {																										//If the input exists
    		input.setPriority(priority);																				//Set the new priority
    	}
    	sortInputs();																								//Sort the inputs
		return input;
	}

	/**
	 * Remove an input from a bank.
	 * @param location - Location of the input.
	 * @return The removed input object, {@code null} if no input found at this location.
	 */
	public Input removeInput(Location location) {
		return locationToInput.remove(location);			//Remove the input
	}
	
	/**
	 * Sorts the inputs by priority.
	 */
	private void sortInputs() {
		List<Input> values = new ArrayList<Input>(locationToInput.values());
		Collections.sort(values);
		locationToInput = new HashMap<Location, Input>();
		for (Input value : values)
			locationToInput.put(value.getLocation(), value);
	}
	
	/**
	 * Sets the location has an output.<br><br>
	 * If the output already exists, the priority and the materials are updated.
	 * @param location - Location of the output.
	 * @param priority - Priority of the output.
	 * @param materials - Sorted materials of the output.
	 * @return The created output object.
	 */
	public Output setOutput(Location location, int priority, List<Material> materials) {
		Output existingOutput = locationToOutput.get(location);					//Get the existing output
    	if(existingOutput == null) {									//If no existing output
    		existingOutput = new Output(this, location, priority);			//Create a new output
    		locationToOutput.put(location, existingOutput);							//Add the new output
    	}
    	else {															//If the output exists
    		existingOutput.setPriority(priority);							//Set the new priority
    		for(Material material:existingOutput.getMaterials()) {			//For each previous material
    			materialToOutputs.get(material).remove(existingOutput);			//Remove the output from the material map
    		}
    	}
		for(Material material:materials) {								//For each new material
			List<Output> existingOutputs = materialToOutputs.get(material);	//Get the existing list
			if(existingOutputs == null) {									//If no existing list
				existingOutputs = new ArrayList<Output>();						//Create a new list
				existingOutputs.add(existingOutput);							//Add the new output to the list
				materialToOutputs.put(material, existingOutputs);				//Add the list to the map
			}
			else {															//If the list exists
				existingOutputs.add(existingOutput);							//Add the output to the map
			}
		}
		existingOutput.setMaterials(materials);							//Update the materials of the output
		if(existingOutput.isOverflow())									//If output is an overflow
			overflows.add(existingOutput);									//Add it to the overflow list
    	sortOutputs();													//Sort the outputs
		return existingOutput;
	}
	
	/**
	 * Remove an output from a bank.
	 * @param location - Location of the output.
	 * @return The removed output object, {@code null} if no output found at this location.
	 */
	public Output removeOutput(Location location) {
		Output output = locationToOutput.remove(location);						//Get the existing output
		if(output != null) {													//If the output exists
			for(Material material:output.getMaterials())							//For each material of the output
				materialToOutputs.get(material).remove(output);							//Remove the output from the material map
			if(output.isOverflow())													//If output is an overflow
				overflows.remove(output);												//Remove it to the overflow list
		}
		return output;
	}
	
	/**
	 * Sorts the outputs by priority.
	 */
	public void sortOutputs() {
		List<Output> values = new ArrayList<Output>(locationToOutput.values());
		Collections.sort(values);
		locationToOutput = new HashMap<Location, Output>();
		for (Output value : values)
			locationToOutput.put(value.getLocation(), value);
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
    		for(Input input:locationToInput.values()) {																//For each input in the bank
    			InventoryHolder inputHolder = (InventoryHolder) input.getLocation().getBlock().getState();				//Get the input holder
    			for(ItemStack itemStack: inputHolder.getInventory().getContents()) {									//For each item in the input
    				if(itemStack != null) {																					//If the item is not null
    					List<Output> outputs = findOutputs(itemStack.getType());												//Find the outputs for this item
    					for(Output output:outputs) {																				//For each possible output
    						InventoryHolder outputHolder = (InventoryHolder) output.getLocation().getBlock().getState();				//Get the output holder
    						ItemStack itemStackToTransfer = itemStack.clone();															//Clone the item to keep the meta data
    						itemStackToTransfer.setAmount(1);																			//Transfer only one item
    						HashMap<Integer, ItemStack> couldntTransfer = outputHolder.getInventory().addItem(itemStackToTransfer);		//Add the item to the output
    						if(couldntTransfer.isEmpty()) {																				//If it has been transfered
    							inputHolder.getInventory().removeItem(itemStackToTransfer);													//Remove the item from the input
    							return;																										//Exit
    						}
    					}
    				}
    			}
    		}
    		
    		//Run only if all the inputs are empty
    		toCompute = false;
		}
	}
	
	/**
	 * Returns the bank as a RawText to display it.
	 * @return Bank as RawText.
	 */
	public RawText toRawText() {
		RawTextPart text = new RawText("")
    			.then(name)
    				.style(ChatColor.GOLD)
    			.then(" (" + description + ") ")
    				.color(ChatColor.GRAY)
    			.then(isEnable() ? "ON" : "OFF")
    				.color(enable ? ChatColor.GREEN : ChatColor.RED)
        		.then(I.t("\n  {0} inputs", locationToInput.size()))
    				.color(ChatColor.GRAY);

		for(Input input:locationToInput.values()) {
			text
				.then("\n    (" + input.getPriority() + ")")
				.color(ChatColor.AQUA);
				/*.hover(
					new RawText()
						.then(String.format("X=%d\nY=%d\nZ=%d", input.getLocation().getX(), input.getLocation().getY(), input.getLocation().getZ()))
							.color(ChatColor.AQUA)
				);*/
    	}

    	text
    		.then(I.t("\n  {0} outputs", locationToOutput.size()))
    			.color(ChatColor.GRAY);
    	
    	for(Output output:locationToOutput.values()) {
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
    				/*.hover(
    	    			new RawText()
        					.then(String.format("{aqua}X=%d\nY=%d\nZ=%d", output.getLocation().getX(), output.getLocation().getY(), output.getLocation().getZ()))))
        						.color(ChatColor.AQUA)
        			)*/
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
