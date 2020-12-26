package fr.zcraft.zsorter.utils;

import org.bukkit.block.Block;
import org.bukkit.inventory.DoubleChestInventory;
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
	 * Find the inventory of a block.
	 * @param block - Block from which get the inventory.
	 * @return The inventory corresponding to the block location.
	 * @throws ZSorterException if the block is not an instance of {@code InventoryHolder};
	 */
	public static InventoryHolder findInventoryFromBlock(Block block) throws ZSorterException {
		if(!(block.getState() instanceof InventoryHolder))
			throw new ZSorterException(I.t("This block must be a holder."));

		InventoryHolder holder = (InventoryHolder) block.getState();
		holder = doubleHolderToSimpleHolder(holder);
		return holder;
	}
}
