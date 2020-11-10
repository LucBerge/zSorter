package fr.zcraft.zsorter.tasks;

import java.util.List;

import org.bukkit.scheduler.BukkitTask;

import fr.zcraft.zlib.tools.runners.RunTask;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Event called 
 * @author Lucas
 *
 */
public class SortTask implements Runnable{

	private static SortTask instance;
	
	/**
	 * Returns the instance of the {SortingTask.class}
	 * @return Instance of the class.
	 */
	public static SortTask getInstance() {
		if(instance == null)
			instance = new SortTask();
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
	 * Constructs a sort task.
	 */
	private SortTask(){}
	
	/**
	 * The current bukkit task.
	 */
	private BukkitTask task;
	
	/**
	 * Start the sort task. Does nothing if already started.<br>
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
    	List<Sorter> sorters = ZSorter.getInstance().getSorterManager().canCompute();		//Get the sorters that need computation
    	if(sorters.isEmpty()) {															//If no sorter needs computation
    		task.cancel();																	//Cancel the task
    	}
    	else {																			//If some sorters need computation
	    	for(Sorter sorter:sorters) {														//For each sorter that needs to compute sorting
	    		sorter.computeSorting();															//Compute sorting
	    	}
    	}
	}
}
