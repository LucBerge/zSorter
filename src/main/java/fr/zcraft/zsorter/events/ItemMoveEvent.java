package fr.zcraft.zsorter.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.utils.InventoryUtils;

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
    	if(ZSorter.getInstance().isEnable()) {																					//If the plugin is not enable
    		if(e.getDestination().getHolder() != null){																				//If the destination inventory has holder
    	        InventoryHolder inputInventory = InventoryUtils.doubleHolderToSimpleHolder(e.getDestination().getHolder());				//Get the holder if double chest
    			boolean computed = ZSorter.getInstance().getSorterManager().computeSorter(inputInventory, null, true);						//Try to compute the sorter with this input
    			if(!computed) {																											//If no computed
    				InventoryHolder outputInventory = InventoryUtils.doubleHolderToSimpleHolder(e.getSource().getHolder());					//Get the holder if double chest
    				ZSorter.getInstance().getSorterManager().computeSorter(outputInventory, null, false);										//Try to compute the sorter with this output
    			}
    		}
		}
	}
}
