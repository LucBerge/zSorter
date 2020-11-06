package fr.zcraft.zsorting.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

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
    		for(Bank bank:ZSorting.getInstance().getBankManager().getNameToBank().values()) {
    			if(e.getBlock().getState() instanceof InventoryHolder) {
    				InventoryHolder holder = (InventoryHolder) e.getBlock().getState();
        			if(bank.removeInput(holder.getInventory()) != null){
        				e.getPlayer().sendMessage(ChatColor.RED + I.t("This holder was an input of the bank {0}. It has been removed from it.", bank.getName()));
        			}
        			if(bank.removeOutput(holder.getInventory()) != null){
        				e.getPlayer().sendMessage(ChatColor.RED + I.t("This holder was an output of the bank {0}. It has been removed from it.", bank.getName()));
        			}
    			}
    		}
    	}
    }
}
