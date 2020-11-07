package fr.zcraft.zsorting.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import fr.zcraft.zsorting.ZSorting;

/**
 * Event called when a block is broken.
 * @author Lucas
 *
 */
public class ItemMoveEvent implements Listener{
	
    /**
     * Event called when an item is moved from one inventory to another.
     * @param e - Event.
     */
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {
		ZSorting.getInstance().getBankManager().computeBank(e.getDestination());
	}
}
