package fr.zcraft.zsorter.utils;

import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * This class provides useful methods related to inventories.
 * @author Lucas
 *
 */
public class InventoryUtils {

	/**
	 * Find the double chest inventory if the inventory is part of a double chest.<br>
	 * Don't do anything if the inventory is not part of a double chest.
	 * @param inventory - Inventory to check.
	 * @return The double chest inventory.
	 */
	public static Inventory simpleInventoryToDoubleInventory(Inventory inventory) {
		InventoryHolder holder = (InventoryHolder) inventory.getLocation().getBlock().getState();
        if(holder.getInventory() instanceof DoubleChestInventory) {
        	inventory = (DoubleChestInventory) holder.getInventory();
        }        
        return inventory;
	}
	
	/**
	 * Find the left inventory if the inventory is a double chest inventory.<br>
	 * Don't do anything if the inventory is not a double chest inventory.
	 * @param inventory - Inventory to check.
	 * @return The left side inventory.
	 */
	public static Inventory doubleInventoryToSimpleInventory(Inventory inventory) {
        if(inventory instanceof DoubleChestInventory) {
        	DoubleChestInventory dci = (DoubleChestInventory) inventory;
        	inventory = dci.getLeftSide();
        }
        return inventory;
	}
}
