package fr.zcraft.zsorter.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zsorter.ZSorter;

/**
 * The class {@code InputOutput} represents an input or an output of a sorter.
 * @author Lucas
 *
 */
public abstract class InputOutput implements Serializable, Comparable<InputOutput>{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3179098898886096938L;
	
	/**
	 * Inventory of the InputOutput.
	 */
	private transient Inventory inventory;
	
	/**
	 * Priority of the InputOutput.
	 */
	private int priority;

	/**
	 * Constructor of an InputOutput object.
	 * @param inventory - Inventory of the InputOutput.
	 * @param priority - Priority of the InputOutput.
	 */
	public InputOutput(Inventory inventory, Integer priority) {
		super();
		
		if(inventory == null)
			throw new IllegalArgumentException("An InputOutput inventory cannot be null");
		
		if(priority == null)
			throw new IllegalArgumentException("An InputOutput priority cannot be null");
		
		if(priority < 1)
			throw new IllegalArgumentException("An InputOutput priority cannot be less than 1");
		
		this.inventory = inventory;
		this.priority = priority;
	}

	/**
	 * Returns the priority of the InputOutput;
	 * @return Priority of the InputOutput
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Sets the priority of the InputOutput.
	 * @param priority - Prority of the InputOutput.
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * Returns the inventory of the InputOutput.
	 * @return Inventory of the InputOutput.
	 */
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public int compareTo(InputOutput io) {
		return this.priority - io.getPriority();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + priority;
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
		InputOutput other = (InputOutput) obj;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (priority != other.priority)
			return false;
		return true;
	}

	private void writeObject(ObjectOutputStream oos) throws IOException
	{
		oos.defaultWriteObject();
		oos.writeObject(inventory.getLocation().getWorld().getName());
		oos.writeInt(inventory.getLocation().getBlockX());
		oos.writeInt(inventory.getLocation().getBlockY());
		oos.writeInt(inventory.getLocation().getBlockZ());
	}

	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
	{
		ois.defaultReadObject();
		String worldName = (String)ois.readObject();
		int x = ois.readInt();
		int y = ois.readInt();
		int z = ois.readInt();
		
		if(worldName == null)
			throw new IOException("The inventory world name is null. The input/output will be removed.");
		
		World world = ZSorter.getInstance().getServer().getWorld(worldName);
		
		if(world == null)
			throw new IOException(String.format("The world %s does not exist anymore. The input/output in this world will be removed.", worldName));

		Block block = world.getBlockAt(x, y, z);
		
		if(block == null)
        	throw new IOException(String.format("The block at the location x=%d, y=%d, z=%d in the world %s does not exist. The input/output at this location will be removed.", x, y, z, worldName));
        
		if(!(block.getState() instanceof InventoryHolder))
        	throw new IOException(String.format("The block at the location x=%d, y=%d, z=%d in the world %s is not a holder. The input/output at this location will be removed.", x, y, z, worldName));
        
		inventory = ((InventoryHolder) block.getState()).getInventory();
	}
}
