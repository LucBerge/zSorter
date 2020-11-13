package fr.zcraft.zsorter.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * Event called when a block is broken.
 * @author Lucas
 *
 */
public class HolderBreakEvent implements Listener{

    /**
     * Event called when a block is broken.
     * @param e - Event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
    	if(ZSorter.getInstance().isEnable()) {																//If the plugin is not enable
	    	if(e.getBlock().getState() instanceof InventoryHolder) {											//If the block is an inventory holder
				InventoryHolder holder = (InventoryHolder) e.getBlock().getState();									//Get the holder block
		        Inventory inventory = InventoryUtils.doubleInventoryToSimpleInventory(holder.getInventory());		//Get the inventory
		        Sorter sorter = ZSorter.getInstance().getSorterManager().getInventoryToSorter().get(inventory);		//Get the associated sorter
		        if(sorter != null) {																				//If a sorter has been found
		        	if(sorter.removeInput(inventory) != null){															//Try to remove the input inventory
		        		e.getPlayer().sendMessage(ChatColor.RED + I.t("This holder was an input of the sorter {0}. It has been removed from it.", sorter.getName()));
		        	}
		        	if(sorter.removeOutput(inventory) != null){															//Try to remove the output inventory
		        		e.getPlayer().sendMessage(ChatColor.RED + I.t("This holder was an output of the sorter {0}. It has been removed from it.", sorter.getName()));
		        	}
	    		}
	    	}
    	}
    }
}
