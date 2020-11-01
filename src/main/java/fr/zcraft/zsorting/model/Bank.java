package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.components.rawtext.RawTextPart;

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
	
	private String name;
	private String description;	
	private boolean state;
	
	private Map<Location, Input> inputs;
	private Map<Location, Output> outputs;
	private Map<Material, List<Output>> materialToOutput;
	private List<Output> overflows;
	
	/**
	 * Constructor of a bank.
	 * @param name - Name of the bank.
	 * @param description - Short description of the bank.
	 */
	public Bank(String name, String description) {
		super();
		
		if(name == null)
			throw new IllegalArgumentException("The bank name cannot be null.");
		
		if(description == null)
			throw new IllegalArgumentException("The bank description cannot be null.");
		
		this.name = name;
		this.description = description;
		this.state = false;
		this.inputs = new HashMap<Location, Input>();
		this.outputs = new HashMap<Location, Output>();
		this.materialToOutput = new HashMap<Material, List<Output>>();
		this.overflows = new ArrayList<Output>();      
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
	public boolean getState() {
		return state;
	}

	/**
	 * Sets the state of the bank.
	 * @param state - {@code true} to enable the bank, {@code false} to disable it.
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * Returns the bank inputs.
	 * @return The bank inputs.
	 */
	public Map<Location, Input> getInputs() {
		return inputs;
	}

	/**
	 * Returns the bank outputs.
	 * @return The bank outputs.
	 */
	public Map<Location, Output> getOutputs() {
		return outputs;
	}

	/**
	 * Returns the material to output map.
	 * @return The material to output map.
	 */
	public Map<Material, List<Output>> getMaterialToOutputs() {
		return materialToOutput;
	}

	/**
	 * Returns the bank overflows.
	 * @return The bank overflows.
	 */
	public List<Output> getOverflows() {
		return overflows;
	}
	
	/**
	 * Sets the location has an output. Replace the existing one if it exists.
	 * @param location - Location of the output.
	 * @param priority - Priority of the output.
	 * @param materials - Sorted materials of the output.
	 */
	public void setOutput(Location location, int priority, List<Material> materials) {
		Output existingOutput = outputs.get(location);					//Get the existing output
    	if(existingOutput == null) {									//If no existing output
    		existingOutput = new Output(this, location, priority);			//Create a new output
    		outputs.put(location, existingOutput);							//Add the new output
    	}
    	else {															//If the output exists
    		existingOutput.setPriority(priority);							//Set the new priority
    		for(Material material:existingOutput.getMaterials()) {			//For each previous material
    			materialToOutput.get(material).remove(existingOutput);			//Remove the output from the material map
    		}
    	}
		for(Material material:materials) {								//For each new material
			List<Output> existingOutputs = materialToOutput.get(material);	//Get the existing list
			if(existingOutputs == null) {									//If no existing list
				existingOutputs = new ArrayList<Output>();						//Create a new list
				existingOutputs.add(existingOutput);							//Add the new output to the list
				materialToOutput.put(material, existingOutputs);				//Add the list to the map
			}
			else {															//If the list exists
				existingOutputs.add(existingOutput);							//Add the output to the map
			}
		}
		existingOutput.setMaterials(materials);							//Update the materials of the output
		if(existingOutput.isOverflow())									//If output is an overflow
			overflows.add(existingOutput);									//Add it to the overflow list
    	sortOutputs();													//Sort the outputs
	}
	
	/**
	 * Remove an output from a bank.
	 * @param location - Location of the output.
	 * @return {@code true} if the output has been removed, {@code false} if no output found at this location.
	 */
	public boolean removeOutput(Location location) {
		Output output = outputs.remove(location);								//Get the existing output
		if(output != null) {													//If the output exists
			for(Material material:output.getMaterials())							//For each material of the output
				materialToOutput.get(material).remove(output);							//Remove the output from the material map
			if(output.isOverflow())													//If output is an overflow
				overflows.remove(output);												//Remove it to the overflow list
		}
		return output != null;
	}
	
	/**
	 * Find all the possible outputs for a given material
	 * @param material - Material to sort.
	 * @return List of possible outputs sorted by priority.
	 */
	public List<Output> findOutputs(Material material){
		List<Output> possibleOutputs = new ArrayList<Output>();			//Define a new list containing the possible outputs
		List<Output> materialOutputs = materialToOutput.get(material);	//Find the outputs for this item
		if(materialOutputs != null)										//If other outputs are possible
			possibleOutputs.addAll(materialOutputs);						//Add the outputs
		possibleOutputs.addAll(overflows);								//Add the overflows
		return possibleOutputs											//Sort the output by priority
				.stream()
				.sorted()
				.collect(Collectors.toList());
	}
	
	/**
	 * Sorts the inputs by priority.
	 */
	public void sortInputs() {
		inputs = inputs.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue())
		  .collect(Collectors.toMap(
				    Map.Entry::getKey, 
				    Map.Entry::getValue, 
				    (oldValue, newValue) -> oldValue, HashMap::new));
	}
	
	/**
	 * Sorts the outputs by priority.
	 */
	public void sortOutputs() {
		outputs = outputs.entrySet()
		  .stream()
		  .sorted(Map.Entry.comparingByValue())
		  .collect(Collectors.toMap(
				    Map.Entry::getKey, 
				    Map.Entry::getValue, 
				    (oldValue, newValue) -> oldValue, HashMap::new));
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
		if(getState()) {																						//If the bank is ON
    		for(Input input:getInputs().values()) {																	//For each input in the bank
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
		}
	}
	
	/**
	 * Returns the bank as a RawText to display it.
	 * @return Bank as RawText.
	 */
	public RawText toRawText() {
		RawTextPart<?> text = new RawText("")
    			.then(getName())
    			.style(ChatColor.GOLD)
    			.then(" (" + getDescription() + ") ")
    			.color(ChatColor.GRAY)
    			.then(getState() ? "ON" : "OFF")
    			.color(getState() ? ChatColor.GREEN : ChatColor.RED)
    			.then("\n  " + inputs.size() + " inputs");
		
    	for(Input input:inputs.values()) {
    		text = text.then(I.t("\n    {0},{1},{2} ({3})", input.getLocation().getX(), input.getLocation().getY(), input.getLocation().getZ(), input.getPriority()));
    	}

    	text = text.then(I.t("\n  {0} outputs", outputs.size()));
    	
    	for(Output output:outputs.values()) {
    		String materials = "*";
    		if(!output.getMaterials().isEmpty()) {
            	StringJoiner joiner = new StringJoiner(" ");
            	for(Material material:output.getMaterials())
            		joiner.add(material.name().toLowerCase());
            	materials = joiner.toString();
    		}
    		text = text.then(I.t("\n    {0},{1},{2} ({3}) ({4})", output.getLocation().getX(), output.getLocation().getY(), output.getLocation().getZ(), output.getPriority(), materials));
    	}
    	return text.build();
	}
}
