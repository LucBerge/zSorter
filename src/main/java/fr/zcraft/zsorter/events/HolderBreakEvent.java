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

    	if(e.getBlock().getState() instanceof InventoryHolder) {
    		for(Sorter sorter:ZSorter.getInstance().getSorterManager().getNameToSorter().values()) {
    			if(e.getBlock().getState() instanceof InventoryHolder) {
    				
    				InventoryHolder holder = (InventoryHolder) e.getBlock().getState();
    		        Inventory inventory = InventoryUtils.doubleInventoryToSimpleInventory(holder.getInventory());
    				
        			if(sorter.removeInput(inventory) != null){
        				e.getPlayer().sendMessage(ChatColor.RED + I.t("This holder was an input of the sorter {0}. It has been removed from it.", sorter.getName()));
        			}
        			if(sorter.removeOutput(inventory) != null){
        				e.getPlayer().sendMessage(ChatColor.RED + I.t("This holder was an output of the sorter {0}. It has been removed from it.", sorter.getName()));
        			}
    			}
    		}
    	}
    }
}
