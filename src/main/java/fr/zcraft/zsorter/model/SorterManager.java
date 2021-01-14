package fr.zcraft.zsorter.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.tasks.SortTask;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * The class {@code SorterManager} is used to manage sorters.
 * @author Lucas
 */
public class SorterManager implements Serializable{

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1782855927147248287L;
	
	private Map<String, Sorter> nameToSorter;
	private transient Map<InventoryHolder, Sorter> holderToSorter;
	private transient Map<Player, Sorter> playerToSorter;
	
	/**
	 * Constructor of a sorter manager object.
	 */
	public SorterManager() {
		this.holderToSorter = new HashMap<InventoryHolder, Sorter>();
		this.nameToSorter = new TreeMap<String, Sorter>();
		this.playerToSorter = new HashMap<Player, Sorter>();
	}
	
	/**
	 * Returns the map linking a name to a sorter.
	 * @return The name to sorter map.
	 */
	public Map<String, Sorter> getNameToSorter() {
		return nameToSorter;
	}
	
	/**
	 * Returns the map linking a player to a sorter.
	 * @return The player to sorter map.
	 */
	public Map<Player, Sorter> getPlayerToSorter() {
		return playerToSorter;
	}
	
	/**
	 * Returns the map linking a holder to a sorter.<br><br>
	 * Do not use this method if you need to add or remove a sorter.
	 * Use the {@code addSorter} and {@code deleteSorter} methods instead.
	 * @return The sorters of the plugin.
	 */
	public Map<InventoryHolder, Sorter> getInventoryToSorter() {
		return holderToSorter;
	}
	
	/**
	 * Add a new sorter to the manager.<br><br>
	 * If the sorter already exists, the description is updated.
	 * @param name - Name of the sorter.
	 * @param description - Short description of the sorter.
	 * @return The created sorter object.
	 * @throws ZSorterException if a sorter with this name already exists.
	 */
	public Sorter createSorter(String name, String description) throws ZSorterException {
		Sorter sorter = new Sorter(name, description);										//Create a new sorter
		if(nameToSorter.putIfAbsent(name, sorter) != null)										//If a sorter with this name already exists
			throw new ZSorterException(I.t("A sorter with this name already exists"));
		return sorter;
	}
	
	/**
	 * Remove a sorter from the manager.
	 * @param name - Name of the sorter.
	 * @return The removed sorter object, {@code null} if no sorter has this name.
	 * @throws ZSorterException  if no sorter with this name exists.
	 */
	public Sorter deleteSorter(String name) throws ZSorterException {
		Sorter sorter = nameToSorter.remove(name);										//Get the existing sorter
		if(sorter == null)															//If no sorter has this name
			throw new ZSorterException(I.t("There is no sorter with this name."));		//Display error message
		
		for(Input input:sorter.getInventoryToInput().values())							//For each input of the sorter
			holderToSorter.remove(input.getHolder());								//Remove the input from the holder to sorter map
		for(Input input:sorter.getInventoryToInput().values())							//For each ouput of the sorter
			holderToSorter.remove(input.getHolder());								//Remove the output from the holder to sorter map
		return sorter;
	}
	
	/**
	 * Sets a new sorter input.
	 * @param name - Name of the sorter.
	 * @param holder - Input holder.
	 * @param priority - Priority of the input.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	public void setInput(String name, InventoryHolder holder, int priority) throws ZSorterException {
		Sorter sorter = nameToSorter.get(name);
		if(sorter == null)
			throw new ZSorterException(I.t("There is no sorter with this name."));

		Sorter existingSorter = holderToSorter.putIfAbsent(holder, sorter);												//Get the sorter with this input
		if(existingSorter != null && !sorter.equals(existingSorter))															//If the sorter is not this one
			throw new ZSorterException(I.t("This holder is already in use by the sorter {0}.", sorter.getName()));			//Display error messsage
		
		sorter.setInput(holder, priority);
	}
	
	/**
	 * Remove an input from a sorter.
	 * @param name - Name of the sorter.
	 * @param holder - Holder of the input.
	 * @return The removed input object, {@code null} if no input found for this holder.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	public Input removeInput(String name, InventoryHolder holder) throws ZSorterException {
		Sorter sorter = nameToSorter.get(name);
		if(sorter == null)
			throw new ZSorterException(I.t("There is no sorter with this name."));
		
		Input input = sorter.removeInput(holder);
		if(input == null)
			throw new ZSorterException(I.t("This holder is not an input."));
			
		holderToSorter.remove(holder);			//Unkink the sorter
		return input;
	}
	
	/**
	 * Sets a new sorter output.
	 * @param name - Name of the sorter.
	 * @param holder - Output holder.
	 * @param priority - Priority of the output.
	 * @param materials - Materials of the output.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	public void setOutput(String name, InventoryHolder holder, int priority, List<Material> materials) throws ZSorterException {
		Sorter sorter = nameToSorter.get(name);
		if(sorter == null)
			throw new ZSorterException(I.t("There is no sorter with this name."));

		Sorter existingSorter = holderToSorter.putIfAbsent(holder, sorter);											//Get the sorter with this input
		if(existingSorter != null && !sorter.equals(existingSorter))															//If the sorter is not this one
			throw new ZSorterException(I.t("This holder is already in use by the sorter {0}.", sorter.getName()));			//Display error messsage
		
		sorter.setOutput(holder, priority, materials);
	}
	
	/**
	 * Remove an output from a sorter.
	 * @param name - Name of the sorter.
	 * @param holder - Holder of the output.
	 * @return The removed output object, {@code null} if no output found for this holder.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	public Output removeOutput(String name, InventoryHolder holder) throws ZSorterException {
		Sorter sorter = nameToSorter.get(name);
		if(sorter == null)
			throw new ZSorterException(I.t("There is no sorter with this name."));
		
		Output output = sorter.removeOutput(holder);
		if(output == null)
			throw new ZSorterException(I.t("This holder is not an output."));
		
		holderToSorter.remove(holder);									//Unkink the sorter
		return output;
	}
	
	/**
	 * Returns a list of sorters to compute.
	 * @return List of sorters to compute.
	 */
	public List<Sorter> canCompute(){
		return nameToSorter
				.values()
				.stream()
				.filter(b -> b.isToCompute())
				.collect(Collectors.toList());
	}
	
	/**
	 * Compute the sorter associated with this holder.
	 * Don't do anything if the holder is not an input or an output.
	 * @param holder - Holder of the sorter to compute.
	 * @param triggerPlayer - The player who triggered this event. {@code null} if it is not a player.
	 * @param checkContent - Defines the rule to apply for the output full flag. {@code true} to define the flag regarding the output content, {@code false} to set it to {@code false} anyway.
	 * @return {@code true} if the sorter has been computed, {@code false} otherwise.
	 */
	public boolean computeSorter(InventoryHolder holder, HumanEntity triggerPlayer, boolean checkContent) {
		boolean computed = false;
		Sorter sorter = holderToSorter.get(holder);							//Get the sorter associated with this holder
		if(sorter != null && sorter.isEnable()) {							//If sorter found and enable
			Input input = sorter.getInventoryToInput().get(holder);				//Get the input linked to this holder
			if(input != null) {													//If input found
				
				if(triggerPlayer != null) {																	//If player triggered this event
					Set<Material> alreadyWarned = new HashSet<Material>();
	    			for(ItemStack itemStack: input.getHolder().getInventory().getContents()) {					//For each item in the input inventory
	    				if(itemStack != null) {																		//If the item is not null
	    					if(sorter.getCloggingUpMaterials().contains(itemStack.getType())) {							//If the material is clogging up the input
	    						if(!alreadyWarned.contains(itemStack.getType())) {											//If the material has not already been warned
	    							alreadyWarned.add(itemStack.getType());														//Add it to the warned list
	    							triggerPlayer.sendMessage("§c" + I.t("The following material cannot be sorted  : {0}", itemStack.getType().name().toLowerCase()));
	    						}
	    					}
	    					else {																						//If the material is not clogging up yet
	    						//Add it to the materialToPlayer list
	    						Set<HumanEntity> players = sorter.getMaterialToPlayers().get(itemStack.getType());				//Get the current list of players to warn in this case
	    						if(players == null) {																		//If no players to warn yet
	    							players = new HashSet<HumanEntity>();															//Create a new set
	    							players.add(triggerPlayer);																	//Add the trigger player
	    							sorter.getMaterialToPlayers().put(itemStack.getType(), players);							//Add the set for the given material
	    						}
	    						else {																						//If some players to warn
	    							players.add(triggerPlayer);																	//Add the player to the set
	    						}
	    					}
	    				}
	    			}
	    		}
				
				sorter.setToCompute(true);										//Set the sorter to compute
				SortTask.getInstance().start();									//Start the task
				computed = true;
			}
			else {															//If the holder is not an input
				Output output = sorter.getInventoryToOutput().get(holder);		//Get the output linked to this holder
				if(output != null) {											//If the holder is an output
					
					boolean clogging = output.getMaterials()
							.stream()
							.filter(sorter.getCloggingUpMaterials()::contains)
							.count() > 0;
					
					boolean state = false;
					if(checkContent)
						state = InventoryUtils.isFull(output.getHolder());
					output.setFull(state);
					
					if(clogging || output.isOverflow()) {						//If one of the output material was clogging up the inputs or if it is an overflow
						for(Material material:output.getMaterials())				//For each material
							sorter.getCloggingUpMaterials().remove(material);			//Not clogging up anymore
						
						sorter.setToCompute(true);									//Set the sorter to compute
						SortTask.getInstance().start();								//Start the task
						computed = true;
					}
				}
			}
		}
		return computed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((holderToSorter == null) ? 0 : holderToSorter.hashCode());
		result = prime * result + ((nameToSorter == null) ? 0 : nameToSorter.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SorterManager other = (SorterManager) obj;
		if (holderToSorter == null) {
			if (other.holderToSorter != null)
				return false;
		} else if (!holderToSorter.equals(other.holderToSorter))
			return false;
		if (nameToSorter == null) {
			if (other.nameToSorter != null)
				return false;
		} else if (!nameToSorter.equals(other.nameToSorter))
			return false;
		return true;
	}
}
