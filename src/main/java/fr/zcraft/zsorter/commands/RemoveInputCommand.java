package fr.zcraft.zsorter.commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;

/**
 * Command triggered to remove an input.
 * @author Lucas
 */
@CommandInfo (name = "remove_input", usageParameters = "<name>")
public class RemoveInputCommand extends ZSorterCommands{
	
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
        if(!(block.getState() instanceof InventoryHolder))
        	throwInvalidArgument(I.t("An input must be a holder."));
        Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
        
        //Try to remove the input from the sorter
        try {
			ZSorter.getInstance().getSorterManager().removeInput(name, inventory);
			success(I.t("This holder is no longer an input."));
		} catch (ZSorterException e) {
        	error(e.getMessage());
		}
    }
}
