package fr.zcraft.zsorting.model;

import java.io.Serializable;

import org.bukkit.inventory.Inventory;

/**
 * The class {@code Input} represents an input of a bank.
 * @author Lucas
 *
 */
public class Input extends InputOutput implements Serializable{
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7628482724409360613L;

	/**
	 * Constructor of an output object.
	 * @param bank - Bank the InputOutput is associated with.
	 * @param inventory - Inventory of the output.
	 * @param priority - Priority of the output.
	 */
	public Input(Bank bank, Inventory inventory, Integer priority) {
		super(bank, inventory, priority);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
