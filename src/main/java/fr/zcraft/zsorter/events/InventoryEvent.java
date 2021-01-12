package fr.zcraft.zsorter.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

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
    	if(ZSorter.getInstance().isEnable()) {																		//If the plugin is not enable
    		if(e.getInventory().getHolder() != null){																	//If the destination inventory has holder
	    		InventoryHolder holder = InventoryUtils.doubleHolderToSimpleHolder(e.getInventory().getHolder());			//Get the holder if double chest
				ZSorter.getInstance().getSorterManager().computeSorter(holder, e.getPlayer(), true);							//Try to compute the sorter with this holder
    		}
    	}
	}
}
