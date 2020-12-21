package fr.zcraft.zsorter.events;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.model.Sorter;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * Event called when a block is broken.
 * @author Lucas
 */
public class LeftClickEvent implements Listener{

    /**
     * Event called when a player interact with a block.
     * @param e - Event.
     */
    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent e) {
    	if(e.getAction() == Action.LEFT_CLICK_BLOCK) {														//The player left click on a block
    		Sorter sorter = ZSorter.getInstance().getSorterManager().getPlayerToSorter().get(e.getPlayer());	//Get the sorter the player
    		if(sorter != null){																					//If sorter found
        		int priority = 1;																					//Defines the priority of the output
    	        try {
    	        	Inventory inventory = InventoryUtils.findInventoryFromBlock(e.getClickedBlock());

    	        	//Try to add the output to the sorter
    	        	ZSorter.getInstance().getSorterManager().setOutput(sorter.getName(), inventory, priority, Arrays.asList(e.getItem().getType()));
    	        	e.getPlayer().sendMessage(ChatColor.GREEN + I.t("This holder is now an output of priority {0}.", priority));
    	        	e.setCancelled(true);
    	        }
    	        catch(ZSorterException ex) {
    	        	e.getPlayer().sendMessage(ChatColor.RED + ex.getMessage());
    	        }
    		}
    	}
    }
}
