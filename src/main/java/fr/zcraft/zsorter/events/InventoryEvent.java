package fr.zcraft.zsorter.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * Event called when a block is broken.
 * @author Lucas
 *
 */
public class InventoryEvent implements Listener{

    /**
     * Event called when an inventory is closed.
     * @param e - Event.
     */
	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent e) {
        Inventory inventory = InventoryUtils.doubleInventoryToSimpleInventory(e.getInventory());		//Get the inventory if double chest
		ZSorter.getInstance().getSorterManager().computeSorter(inventory);
	}
}
