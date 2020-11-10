package fr.zcraft.zsorter.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import fr.zcraft.zsorter.ZSorter;

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
		boolean computed = ZSorter.getInstance().getSorterManager().computeSorter(e.getDestination());	//Try to compute the sorter with this input
		if(!computed)																				//If no computed
			ZSorter.getInstance().getSorterManager().computeSorter(e.getSource());							//Try to compute the sorter with this output
	}
}
