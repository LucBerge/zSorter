package fr.zcraft.zsorting.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
		Collections.sort(materials);
		this.materials = materials;
	}
	
	/**
	 * Checks whether an output is an overflow.
	 * @return {@code true} if the output is an overflow, {@code false} otherwise.
	 */
	public boolean isOverflow() {
		return materials.isEmpty();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((materials == null) ? 0 : materials.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Output other = (Output) obj;
		if (materials == null) {
			if (other.materials != null)
				return false;
		} else if (!materials.equals(other.materials))
			return false;
		return true;
	}
}
