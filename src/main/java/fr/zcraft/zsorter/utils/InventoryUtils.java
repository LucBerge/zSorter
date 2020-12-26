package fr.zcraft.zsorter.utils;

import org.bukkit.block.Block;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorterException;

/**
 * This class provides useful methods related to inventories.
 * @author Lucas
 *
 */
public class InventoryUtils {

	/**
	 * Find the left holder if the holder is a double chest.<br>
	 * Don't do anything if the holder is not a double chest.
	 * @param holder - Holder to check.
	 * @return The left side holder.
	 */
	public static InventoryHolder doubleHolderToSimpleHolder(InventoryHolder holder) {
		if(holder.getInventory() instanceof DoubleChestInventory) {
			DoubleChestInventory dci = (DoubleChestInventory) holder.getInventory();
			holder = dci.getLeftSide().getHolder();
		}
		return holder;
	}

	/**
	 * Find the holder of a block.
	 * @param block - Block from which get the holder.
	 * @return Holder corresponding to the block location.
	 * @throws ZSorterException if the block is not an instance of {@code InventoryHolder};
	 */
	public static InventoryHolder findInventoryFromBlock(Block block) throws ZSorterException {
		if(!(block.getState() instanceof InventoryHolder))
			throw new ZSorterException(I.t("This block must be a holder."));

		InventoryHolder holder = (InventoryHolder) block.getState();
		holder = doubleHolderToSimpleHolder(holder);
		return holder;
	}
	
	/**
	 * Checks whether a holder is full.
	 * @param holder - Holder to check.
	 * @return {@code true} if the holder is full, {@code false} if it contains at least an empty slot.
	 */
	public static boolean isFull(InventoryHolder holder) {
		Inventory inventory = holder.getInventory().getHolder().getInventory();
		for(ItemStack itemStack: inventory.getContents()) {														//For each item in the input inventory
			if(itemStack == null)
				return false;
		}
		return true;
	}
}
