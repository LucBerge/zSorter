package fr.zcraft.zsorting.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import fr.zcraft.zsorting.ZSorting;

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
		ZSorting.getInstance().getBankManager().computeBank(e.getInventory());
	}
}
