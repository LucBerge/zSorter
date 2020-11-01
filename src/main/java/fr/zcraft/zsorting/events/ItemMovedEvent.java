package fr.zcraft.zsorting.events;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;
import fr.zcraft.zsorting.model.Output;

/**
 * Event called 
 * @author Lucas
 *
 */
public class ItemMovedEvent implements Listener{

    /**
     * Event called when an item is moved from one inventory to another.
     * @param e - Event.
     */
	@EventHandler
	public void onInventoryMoveItem(InventoryMoveItemEvent e) {

		Bank bank = ZSorting.getInstance().getBankManager().findBankFromInputLocation(e.getDestination().getLocation());

		if(bank != null && bank.getState()) {					//If this chest is an input of a bank
			for(Output output:bank.getOutputs().values()) {			//For each output of the bank
				if(output.getMaterials().contains(e.getItem().getType())) {			//If the output sort the item
					Chest chest = (Chest) output.getLocation().getBlock().getState();
					chest.getInventory().addItem(e.getItem().clone());
					e.getItem().setType(Material.AIR);
					e.getItem().setAmount(0);
					e.setCancelled(true);
					//REMOVING IS NOT WORKING
					break;
				}
			};
		}
	}
}
