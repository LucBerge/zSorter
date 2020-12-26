package fr.zcraft.zsorter.events;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;
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
			try {
				InventoryHolder inventory = InventoryUtils.findInventoryFromBlock(e.getBlock());					//Get the inventory
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
			catch(ZSorterException ex) {
				//The removed block is not a holder.
			}
		}
	}
}
