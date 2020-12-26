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
    	if(ZSorter.getInstance().isEnable()) {															//If the plugin is not enable
    		System.out.println("onInventoryCloseEvent : " + e.getInventory().getHolder().toString());
    		InventoryUtils.displayInventoryContent(e.getInventory());
    		//InventoryHolder inventory = InventoryUtils.doubleInventoryToSimpleInventory(e.getInventory()).getHolder();		//Get the inventory if double chest
			ZSorter.getInstance().getSorterManager().computeSorter(e.getInventory().getHolder());					//Try to compute the sorter with this inventory
    	}
	}
}
