package fr.zcraft.zsorter.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.components.rawtext.RawTextPart;
import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.commands.SpeedCommand;
import fr.zcraft.zsorter.commands.ToggleCommand;
import fr.zcraft.zsorter.commands.UpdateCommand;

/**
 * The class {@code Sorter} represents a sorter in the game.<br><br>
 * Each sorter has a name, a short description, inputs and outputs.
 * 
 * @author Lucas
 */
public class Sorter implements Serializable{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 7893484799034097907L;

	/**
	 * The default speed of a sorter.
	 */
	public static final int DEFAULT_SPEED = 1;
	
	private String name;
	private String description;	
	private boolean enable;
	private boolean toCompute;
	private int speed;
	
	private transient Map<Inventory, Input> inventoryToInput;
	private transient Map<Inventory, Output> inventoryToOutput;
	
	private List<Input> inputs;
	private Map<Material, List<Output>> materialToOutputs;
	private List<Output> overflows;
	private List<Material> cloggingUpMaterials;
	
	/**
	 * Constructor of a sorter.
	 * @param manager - The sorter manager.
	 * @param name - Name of the sorter.
	 * @param description - Short description of the sorter.
	 */
	public Sorter(String name, String description) {
		super();
		
		if(name == null)
			throw new IllegalArgumentException("The sorter name cannot be null.");
		
		if(description == null)
			throw new IllegalArgumentException("The sorter description cannot be null.");

		this.name = name;
		this.description = description;
		this.enable = false;
		this.toCompute = false;
		this.speed = DEFAULT_SPEED;
		
		this.inventoryToInput = new HashMap<Inventory, Input>();
		this.inventoryToOutput = new HashMap<Inventory, Output>();
		
		this.inputs = new ArrayList<Input>();
		this.materialToOutputs = new TreeMap<Material, List<Output>>();
		this.overflows = new ArrayList<Output>();
		this.cloggingUpMaterials = new ArrayList<Material>();
	}
	
	/**
	 * Returns the name of the sorter.
	 * @return The name of the sorter.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the sorter.
	 * @param name - Name of the sorter.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the description of the sorter.
	 * @return Short description of the sorter.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description of the sorter.
	 * @param description - Short description of the sorter.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the state of the sorter.
	 * @return {@code true} if the sorter is enabled, {@code false} otherwise.
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * Sets the state of the sorter.
	 * @param state - {@code true} to enable the sorter, {@code false} to disable it.
	 */
	public void setEnable(boolean state) {
		if(state)		//If enabling the sorter
			commit();		//Commit the sorter
		else
			this.toCompute = false;
		this.enable = state;
	}

	/**
	 * Checks whether the sorter needs be computed.
	 * @return {@code true} if the sorter has items to sort, {@code false} otherwise.
	 */
	public boolean isToCompute() {
		return toCompute;
	}

	/**
	 * Sets the compute state of the sorter.
	 * @param toCompute - {@code true} to specify that the sorter has items to be sorted, {@code false} otherwise.
	 */
	public void setToCompute(boolean toCompute) {
		this.toCompute = toCompute;
	}
	
	/**
	 * Returns the sorter speed of the sorter.
	 * @return Sorter speed, between 1 and 64.
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets the sorter speed of the sorter.
	 * @param speed - Speed sorter of the sorter.
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Returns the sorter inputs.
	 * Do not use this method if you need to add or remove an input.
	 * Use the {@code setInput} and {@code removeInput} methods instead.
	 * @return The sorter inputs.
	 */
	public Map<Inventory, Input> getInventoryToInput() {
		return inventoryToInput;
	}

	/**
	 * Returns the sorter outputs.
	 * Do not use this method if you need to add or remove an output.
	 * Use the {@code setOutput} and {@code removeOutput} methods instead.
	 * @return The sorter outputs.
	 */
	public Map<Inventory, Output> getInventoryToOutput() {
		return inventoryToOutput;
	}

	/**
	 * Returns the inputs of the sorter.
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
	 * Returns the materials that are clogging up the inputs.
	 * @return List of materials that clog up the inputs.
	 */
	public List<Material> getCloggingUpMaterials() {
		return cloggingUpMaterials;
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
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	public Input setInput(Inventory inventory, int priority) throws ZSorterException {
		Output output = inventoryToOutput.get(inventory);															//Get the existing output
		if(output != null)																							//If exists
			throw new ZSorterException(I.t("This holder is already an output."));										//Display error message
		
		setEnable(false);																							//Disable the sorter
		
		Input existingInput = inventoryToInput.get(inventory);															//Get the existing input
    	if(existingInput == null) {																						//If no input exists
    		existingInput = new Input(inventory, priority);																	//Create a new input
    		inventoryToInput.put(inventory, existingInput);																	//Add the new input
    	}
    	else {																											//If the input exists
    		existingInput.setPriority(priority);																			//Set the new priority
    	}
		return existingInput;
	}

	/**
	 * Remove an input from a sorter.
	 * @param inventory - Inventory of the input.
	 * @return The removed input object, {@code null} if no input found for this inventory.
	 */
	public Input removeInput(Inventory inventory) {
		setEnable(false);
		return inventoryToInput.remove(inventory);
	}
	
	/**
	 * Sets the inventory has an output.<br><br>
	 * If the output already exists, the priority and the materials are updated.
	 * @param inventory - Inventory of the output.
	 * @param priority - Priority of the output.
	 * @param materials - Sorted materials of the output.
	 * @return The created output object.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	public Output setOutput(Inventory inventory, int priority, List<Material> materials) throws ZSorterException {
		Input input = inventoryToInput.get(inventory);																//Get the existing input
		if(input != null)																							//If exists
			throw new ZSorterException(I.t("This holder is already an input."));										//Display error message
		
		setEnable(false);																							//Disable the sorter
		
		Output existingOutput = inventoryToOutput.get(inventory);													//Get the existing output
    	if(existingOutput == null) {																				//If no existing output
    		existingOutput = new Output(inventory, priority);															//Create a new output
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
	 * Remove an output from a sorter.
	 * @param inventory - Inventory of the output.
	 * @return The removed output object, {@code null} if no output found at this inventory.
	 */
	public Output removeOutput(Inventory inventory) {
		setEnable(false);
		return inventoryToOutput.remove(inventory);
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
	 * Checks whether a sorter has at least one input.
	 * @return {@code true} if the sorter has an input, {@code false} otherwise.
	 */
	public boolean hasInput() {
		return inputs.size() > 0;
	}
	
	/**
	 * Checks whether a sorter has at least one overflow.
	 * @return {@code true} if the sorter has an overflow, {@code false} otherwise.
	 */
	public boolean hasOverflow() {
		return overflows.size() > 0;
	}
	
	/**
	 * Compute sorter on the sorter.
	 */
	public void computeSorting() {
		if(isEnable()) {																						//If the sorter is ON
    		for(Input input:inputs) {																				//For each input in the sorter
    			Inventory inputInventory = input.getInventory();														//Get the input inventory
    			for(ItemStack itemStack: inputInventory.getContents()) {												//For each item in the input inventory
    				if(itemStack != null) {																					//If the item is not null
    					ItemStack itemStackToTransfer = itemStack.clone();														//Clone the item to keep the metadata
    					if(itemStackToTransfer.getAmount() > speed)																//If the number of items in the stack is over the speed limit
							itemStackToTransfer.setAmount(speed);																	//Set to the speed limit
    					List<Output> outputs = findOutputs(itemStack.getType());												//Find the outputs for this item
    					for(Output output:outputs) {																			//For each possible output
    						Inventory outputInventory = output.getInventory();														//Get the output inventory
    						int amountToTransfer = itemStackToTransfer.getAmount();													//Get the amount to transfer
    						HashMap<Integer, ItemStack> couldntTransferMap = outputInventory.addItem(itemStackToTransfer);			//Add the item to the output
    						if(couldntTransferMap.isEmpty()) {																		//If everything has been transfered
    							
    							//Run only if all the item fit in the output
    							
        						inputInventory.removeItem(itemStackToTransfer);															//Remove the item from the input
        						if(input.isCloggedUp())																					//If the input is clogged up
        							input.setCloggedUp(false); 																				//Not clogged up anymore
        						if(output.isFull())																						//If the output was full
        							output.setFull(false); 																					//Not full anymore
    							if(cloggingUpMaterials.contains(itemStack.getType()))													//If the stored item was clogging up the inputs
    								cloggingUpMaterials.remove(itemStack.getType());														//Not clogging up anymore
    							return;																									//Exit
    						}
    						
    						//Run only if the output is full
    						
    						if(!output.isFull())																					//If the output was not full
    							output.setFull(true); 																					//Now is it's full
    						
    						ItemStack itemStackToRemove = itemStackToTransfer.clone();												//Create the stack to remove
    						itemStackToRemove.setAmount(amountToTransfer - itemStackToTransfer.getAmount());						//Set the amount to remove
    						inputInventory.removeItem(itemStackToRemove);															//Remove the item from the input
    						itemStackToTransfer.setAmount(itemStackToTransfer.getAmount());											//Define the new amount to transfer
    					}
    					
    					//Run only if this item is clogging up.

						if(!input.isCloggedUp())																	//If the input is not clogging up
							input.setCloggedUp(true); 																	//Set the input to clogged up
    					if(!cloggingUpMaterials.contains(itemStack.getType()))										//If the material is not in the list
    						cloggingUpMaterials.add(itemStack.getType());												//Add the material to the list
    				}
    			}
    		}
    		
    		//Run only if no item has been sorted. Either because there is nothing to sort or because all the outputs are full.
    		
    		toCompute = false;
		}
	}
	
	/**
	 * Returns the sorter as a RawText to display it.
	 * @param byOutput - {@code true} to display by outputs, {@code false} to display be items.
	 * @return Sorter as RawText.
	 */
	public RawText toRawText(boolean byOutput) {
		commit();
		RawTextPart text = new RawText("")
    			.then(name)
    				.style(ChatColor.GOLD)
    			.then(isEnable() ? " ON" : " OFF")
    				.color(enable ? ChatColor.GREEN : ChatColor.RED)
	    			.hover(new RawText()
	        				.then(I.t("Toggle the sorter {0}", name)))
	        			.command(ToggleCommand.class, name)
		        .then(toCompute ? " RUNNING" : "")
		        	.color(ChatColor.AQUA)
    			.then(" (" + description + ") ")
    				.color(ChatColor.GRAY)
    				.hover(new RawText()
	        				.then(I.t("Update the description")))
	        			.suggest(UpdateCommand.class, name)
    			.then("\n  " + I.t("speed: {0}", speed))
					.color(ChatColor.GRAY)
					.hover(new RawText()
	        				.then(I.t("Change the sorter speed")))
	        			.suggest(SpeedCommand.class, name)
        		.then("\n  " + I.t("{0} input(s):", inventoryToInput.size()) + "\n  ")
    				.color(ChatColor.GRAY);
		
		List<Input> inputs = inventoryToInput
				.values()
				.stream()
				.sorted()
				.collect(Collectors.toList());				
				
		for(Input input:inputs) {
			text
				.then("  " + input.getPriority())
					.color(input.isCloggedUp() ? ChatColor.RED : ChatColor.AQUA)
					.hover(
						new RawText()
							.then(String.format("X=%1$,.0f\nY=%2$,.0f\nZ=%3$,.0f", input.getInventory().getLocation().getX(), input.getInventory().getLocation().getY(), input.getInventory().getLocation().getZ()))
								.color(input.isCloggedUp() ? ChatColor.RED : ChatColor.AQUA)
					);
    	}

    	text
    		.then("\n  " + I.t("{0} overflow(s):", overflows.size()) + "\n  ")
    			.color(ChatColor.GRAY);
				
		for(Output overflow:overflows) {
			text
				.then("  " + overflow.getPriority())
					.color(overflow.isFull() ? ChatColor.RED : ChatColor.AQUA)
					.hover(
						new RawText()
							.then(String.format("X=%1$,.0f\nY=%2$,.0f\nZ=%3$,.0f", overflow.getInventory().getLocation().getX(), overflow.getInventory().getLocation().getY(), overflow.getInventory().getLocation().getZ()))
								.color(overflow.isFull() ? ChatColor.RED : ChatColor.AQUA)
					);
		}
		
		//if display by output
		if(byOutput) {
			List<Output> outputs = inventoryToOutput
					.values()
					.stream()
					.filter(o -> !o.isOverflow())
					.sorted()
					.collect(Collectors.toList());
			
	    	text
	    		.then("\n  " + I.t("{0} output(s):", outputs.size()))
	    			.color(ChatColor.GRAY);	
	    	
	    	for(Output output:outputs) {
	    		text
	    			.then("\n    " + output.getPriority())
						.color(output.isFull() ? ChatColor.RED : ChatColor.AQUA)
	    				.hover(
	    	    			new RawText()
	        					.then(String.format("X=%1$,.0f\nY=%2$,.0f\nZ=%3$,.0f", output.getInventory().getLocation().getX(), output.getInventory().getLocation().getY(), output.getInventory().getLocation().getZ()))
	        						.color(output.isFull() ? ChatColor.RED : ChatColor.AQUA)
	        			);
	    		
	    		List<Material> materials = output.getMaterials()
	    											.stream()
	    											.sorted()
	    											.collect(Collectors.toList());
	    		
	    		for(Material material:materials) {
	    			text
	    				.then(" " + material.name().toLowerCase())
	    					.color(cloggingUpMaterials.contains(material) ? ChatColor.RED : ChatColor.GREEN);
	    			if(cloggingUpMaterials.contains(material)) {
	    				text
		    				.hover(
		        	    			new RawText()
		            					.then(I.t("This material is clogging up one of the inputs"))
		            						.color(ChatColor.RED)
		            			);
	    			}
	    		}
			}
    	}
    	else{	//if display by items
    		List<Material> sortedMaterials = inventoryToOutput
														.values()
														.stream()
														.filter(o -> !o.isOverflow())
														.map(o -> o.getMaterials())
														.flatMap(List::stream)
														.distinct()
														.sorted((m1, m2) -> m1.name().compareTo(m2.name()))
														.collect(Collectors.toList());
    		
	    	text
	    		.then("\n  " + I.t("{0} material(s):", sortedMaterials.size()))
	    			.color(ChatColor.GRAY);
	    	
	    	for(Material material:sortedMaterials) {
	    		
	    		text
				.then("\n    " + material.name().toLowerCase())
					.color(cloggingUpMaterials.contains(material) ? ChatColor.RED : ChatColor.GREEN);
				if(cloggingUpMaterials.contains(material)) {
					text
	    				.hover(
	        	    			new RawText()
	            					.then(I.t("This material is clogging up one of the inputs"))
	            						.color(ChatColor.RED)
	            			);
				}
				
				for(Output output: materialToOutputs.get(material)) {
					text
	    			.then("  " + output.getPriority())
						.color(output.isFull() ? ChatColor.RED : ChatColor.AQUA)
	    				.hover(
	    	    			new RawText()
	        					.then(String.format("X=%1$,.0f\nY=%2$,.0f\nZ=%3$,.0f", output.getInventory().getLocation().getX(), output.getInventory().getLocation().getY(), output.getInventory().getLocation().getZ()))
	        						.color(output.isFull() ? ChatColor.RED : ChatColor.AQUA)
	        			);
				}
	    	}
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
		Sorter other = (Sorter) obj;
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

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
	{
		ois.defaultReadObject();
		inventoryToInput = new HashMap<Inventory, Input>();
		for(Input input:inputs) {
			inventoryToInput.putIfAbsent(input.getInventory(), input);
		}
		inventoryToOutput = new HashMap<Inventory, Output>();
		for(List<Output> outputs:materialToOutputs.values()) {
			for(Output output:outputs) {
				inventoryToOutput.putIfAbsent(output.getInventory(), output);
			}
		}
		for(Output overflow:overflows) {
			inventoryToOutput.putIfAbsent(overflow.getInventory(), overflow);
		}
		if(enable)
			toCompute = true;
	}
}
