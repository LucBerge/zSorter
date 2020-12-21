package fr.zcraft.zsorter.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * Command triggered to remove an input.
 * @author Lucas
 */
@CommandInfo (name = "remove_output")
public class RemoveOutputCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();
    	
    	//Check the number of arguments
    	if (args.length < 1)
    		throwInvalidArgument(I.t("A sorter name is required."));

        //Get the name
        String name = args[0];

        //Get the inventory from location
        Block block = playerSender().getTargetBlock((Set<Material>) null, 15);
        
        try {
        	Inventory inventory = InventoryUtils.findInventoryFromBlock(block);
        
        	//Try to remove the output from the sorter
			ZSorter.getInstance().getSorterManager().removeOutput(name, inventory);
			success(I.t("This holder is no longer an output."));
		} catch (ZSorterException e) {
        	error(e.getMessage());
		}
    }
    
    @Override
    protected List<String> complete() throws CommandException{
    	if(args.length <= 1) {
    		return completeSorterName(args[0]);
    	}
    	return null;
    }
}
