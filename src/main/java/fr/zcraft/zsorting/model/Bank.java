package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

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
		for(Output output:outputs.values()) {
			if(output.getMaterials().isEmpty());
				return true;
		}
		return false;
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
    			.then("\n  " + getInputs().size() + " inputs");
		
    	for(Input input:getInputs().values()) {
    		text = text.then(I.t("\n    {0},{1},{2} ({3})", input.getLocation().getX(), input.getLocation().getY(), input.getLocation().getZ(), input.getPriority()));
    	}

    	text = text.then(I.t("\n  {0} outputs", getOutputs().size()));
    	
    	for(Output output:getOutputs().values()) {
    		String materials = "*";
    		if(!output.getMaterials().isEmpty()) {
            	StringJoiner joiner = new StringJoiner(" ");
            	for(Material material:output.getMaterials())
            		joiner.add(material.name());
            	materials = joiner.toString();
    		}
    		text = text.then(I.t("\n    {0},{1},{2} ({3}) ({4})", output.getLocation().getX(), output.getLocation().getY(), output.getLocation().getZ(), output.getPriority(), materials));
    	}
    	return text.build();
	}
}
