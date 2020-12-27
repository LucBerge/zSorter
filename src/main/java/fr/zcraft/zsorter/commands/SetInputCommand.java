package fr.zcraft.zsorter.commands;

import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * Command triggered to remove a sorter.
 * @author Lucas
 */
@CommandInfo (name = "set_input", usageParameters = "<name> <priority>")
public class SetInputCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();
    	
    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A sorter name and an input priority are required."));
        
        //Get the name
        String name = args[0];
        
        //Get the priority
        int priority = 0;
        try {
        	priority = Integer.parseInt(args[1]);
            if (priority < 1)
                throwInvalidArgument(I.t("The input priority must be higher or equal to 1."));
        }catch(NumberFormatException e) {
            throwInvalidArgument(I.t("The input priority must be an integer."));
        }

        //Get the inventory from location
        Block block = playerSender().getTargetBlock((Set<Material>) null, 15);
        
        try {
        	InventoryHolder inventory = InventoryUtils.findInventoryFromBlock(block);
        
        	//Try to add the input to the sorter
        	ZSorter.getInstance().getSorterManager().setInput(name, inventory, priority);
        	success(I.t("This holder is now an input of priority {0}.", priority));
        }
        catch(ZSorterException e) {
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
