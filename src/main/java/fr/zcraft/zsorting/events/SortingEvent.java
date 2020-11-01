package fr.zcraft.zsorting.events;

import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

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
    	for(Bank bank:ZSorting.getInstance().getBankManager().getBanks().values()) {	//For each bank
    		bank.computeSorting();															//COmpute sorting
    	}
	}
}
