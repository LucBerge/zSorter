package fr.zcraft.zsorter.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import fr.zcraft.zsorter.ZSorter;

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
		ZSorter.getInstance().getSorterManager().computeSorter(e.getInventory());
	}
}
