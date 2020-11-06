package fr.zcraft.zsorting.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;
import fr.zcraft.zsorting.model.Input;

/**
 * Event called when a block is broken.
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
		Bank bank = ZSorting.getInstance().getBankManager().getInventoryToBank().get(e.getDestination());	//Get the bank associated with this inventory
		if(bank != null) {																					//If no bank found
			Input input = bank.getInventoryToInput().get(e.getDestination());									//Get the input inked to this inventory
			if(input != null) {																					//If no input found
				bank.setToCompute(true);																			//Set the bank to compute
				SortingTask.getInstance().start();																	//Start the task
			}
		}
	}
}
