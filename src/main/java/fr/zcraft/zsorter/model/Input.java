package fr.zcraft.zsorter.model;

import java.io.Serializable;

import org.bukkit.inventory.Inventory;

/**
 * The class {@code Input} represents an input of a sorter.
 * @author Lucas
 *
 */
public class Input extends InputOutput implements Serializable{
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7628482724409360613L;
	
	/**
	 * List of materials clogging up the input.
	 */
	private boolean cloggedUp;

	/**
	 * Constructor of an output object.
	 * @param inventory - Inventory of the output.
	 * @param priority - Priority of the output.
	 */
	public Input(Inventory inventory, Integer priority) {
		super(inventory, priority);
		this.cloggedUp = false;
	}
	
	/**
	 * Defines whether the input is clogged up or not.
	 * @param cloggedUp - {@code true} if the input is clogged up, {@code false} otherwise.
	 */
	public void setCloggedUp(boolean cloggedUp) {
		this.cloggedUp = cloggedUp;
	}
	
	/**
	 * Checks whether the input is clogged up or not.
	 * @return {@code true} if the input is clogged up, {@code false} otherwise.
	 */
	public boolean isCloggedUp() {
		return cloggedUp;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
