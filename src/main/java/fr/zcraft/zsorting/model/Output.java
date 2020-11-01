package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * The class {@code Output} represents an output of a bank.
 * @author Lucas
 *
 */
public class Output extends InputOutput implements Serializable{
	
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5377166540962451383L;
	
	/**
	 * List of items that can be sorted here.
	 */
	private List<Material> materials;

	/**
	 * Constructor of an output object.
	 * @param bank - Bank the InputOutput is associated with.
	 * @param location - Location of the output.
	 * @param priority - Priority of the output.
	 */
	public Output(Bank bank, Location location, Integer priority) {
		super(bank, location, priority);
		this.materials = new ArrayList<Material>();
	}

	/**
	 * Returns a list of materials that can be sorted.
	 * @return List of materials that can be sorted.
	 */
	public List<Material> getMaterials() {
		return materials;
	}

	/**
	 * Sets the list of sorted materials.
	 * @param materials - List of sorted materials.
	 */
	public void setMaterials(List<Material> materials) {
		this.materials = materials;
	}
	
	/**
	 * Checks whether an output is an overflow.
	 * @return {@code true} if the output is an overflow, {@code false} otherwise.
	 */
	public boolean isOverflow() {
		return materials.isEmpty();
	}
}
