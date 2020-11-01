package fr.zcraft.zsorting.events;

import java.util.HashMap;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;
import fr.zcraft.zsorting.model.Input;
import fr.zcraft.zsorting.model.Output;

/**
 * Event called 
 * @author Lucas
 *
 */
public class SortingEvent implements Runnable{

	/**
	 * Delay before the first call in ticks.
	 */
	public static final int DELAY = 0;
	
	/**
	 * Delay between each call in ticks.
	 */
	public static final int PERIOD = 8;
	
    /**
     * Event called every 8 ticks
     */
    public void run() {
    	for(Bank bank:ZSorting.getInstance().getBankManager().getBanks().values()) {							//For each bank
    		if(bank.getState()) {																					//If the bank is ON
        		for(Input input:bank.getInputs().values()) {															//For each input in the bank
        			InventoryHolder inputHolder = (InventoryHolder) input.getLocation().getBlock().getState();				//Get the input holder
        			for(ItemStack itemStack: inputHolder.getInventory().getContents()) {									//For each item in the input
        				if(itemStack != null) {																					//If the item is not null
	        				for(Output output:bank.getOutputs().values()) {															//For each output of the bank
	        					if(output.getMaterials().contains(itemStack.getType()) || output.getMaterials().isEmpty()) {			//If the material can be stored here
	        						InventoryHolder outputHolder = (InventoryHolder) output.getLocation().getBlock().getState();			//Get the output holder
	        						ItemStack itemStackToTransfer = itemStack.clone();														//Clone the item to keep the meta data
	        						itemStackToTransfer.setAmount(1);																		//Transfer only one item
	        						HashMap<Integer, ItemStack> couldntTransfer = outputHolder.getInventory().addItem(itemStackToTransfer);	//Add the item to the output
	        						if(couldntTransfer.isEmpty()) {																			//If it couldn't be added because of a lack of space
	        							inputHolder.getInventory().removeItem(itemStackToTransfer);												//Remove the item from the input
	        							return;																									//Exit
	        						}
		        				}
		        			}
        				}
        			}
        		}
    		}
    	}
	}
}
