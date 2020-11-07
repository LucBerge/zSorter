package fr.zcraft.zsorting.tasks;

import java.util.List;

import org.bukkit.scheduler.BukkitTask;

import fr.zcraft.zlib.tools.runners.RunTask;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Event called 
 * @author Lucas
 *
 */
public class SortingTask implements Runnable{

	private static SortingTask instance;
	
	/**
	 * Returns the instance of the {SortingTask.class}
	 * @return Instance of the class.
	 */
	public static SortingTask getInstance() {
		if(instance == null)
			instance = new SortingTask();
		return instance;
	}
	
	/**
	 * Delay before the first call in ticks.
	 */
	public static final int DELAY = 0;
	
	/**
	 * Delay between each call in ticks.
	 */
	public static final int PERIOD = 8;
	
	/**
	 * COnstructs a sorting task.
	 */
	private SortingTask(){}
	
	/**
	 * The current bukkit task.
	 */
	private BukkitTask task;
	
	/**
	 * Start the sorting task. Does nothing if already started.<br>
	 * This task will automatically stop when there's nothing left to sort.
	 */
	public void start() {
		if(task == null)
			task = RunTask.timer(this, DELAY, PERIOD);
		else if(task.isCancelled())
			task = RunTask.timer(this, DELAY, PERIOD);
	}
	
    /**
     * Event called every 8 ticks
     */
    public void run() {
    	List<Bank> banks = ZSorting.getInstance().getBankManager().canCompute();		//Get the banks that need computation
    	if(banks.isEmpty()) {															//If no bank needs computation
    		task.cancel();																	//Cancel the task
    	}
    	else {																			//If some banks need computation
	    	for(Bank bank:banks) {															//For each bank that needs to compute sorting
	    		bank.computeSorting();															//Compute sorting
	    	}
    	}
	}
}
