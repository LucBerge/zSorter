package fr.zcraft.zsorter.utils;

import org.bukkit.block.Block;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorterException;

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
	
	/**
	 * Find the inventory of a block.
	 * @param block - Block from which get the inventory.
	 * @return The inventory corresponding to the block location.
	 * @throws ZSorterException if the block is not an instance of {@code InventoryHolder};
	 */
	public static Inventory findInventoryFromBlock(Block block) throws ZSorterException {
        if(!(block.getState() instanceof InventoryHolder))
        	throw new ZSorterException(I.t("Thi block must be an hodler."));
        
		InventoryHolder holder = (InventoryHolder) block.getState();
        return InventoryUtils.doubleInventoryToSimpleInventory(holder.getInventory());
	}
}
