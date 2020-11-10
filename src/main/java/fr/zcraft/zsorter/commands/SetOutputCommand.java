package fr.zcraft.zsorter.commands;

import java.util.ArrayList;
import java.util.List;
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
 * Command triggered to remove a sorter.
 * @author Lucas
 */
@CommandInfo (name = "set_output", usageParameters = "<name> <priority> (<items>)")
public class SetOutputCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();
    	
    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A sorter name and an output priority are required."));

        //Get the name
        String name = args[0];

        //Get the priority
        int priority = 0;
        try {
        	priority = Integer.parseInt(args[1]);
            if (priority < 1)
                throwInvalidArgument(I.t("The output priority must be higher or equal to 1."));
        }catch(NumberFormatException e) {
            throwInvalidArgument(I.t("The output priority must be an integer."));
        }

        //Get the materials
        List<Material> materials = new ArrayList<Material>();
        if (args.length > 2) {
        	for (int i = 2; i < args.length; i++) {
        		Material material = Material.getMaterial(args[i].toUpperCase());
        		if(material != null)
        			materials.add(material);
        		else
        			error(I.t("Could not found material {0} : Ignored.", args[i]));
        	}
        }
        
        //Get the inventory from location
        Block block = playerSender().getTargetBlock((Set<Material>) null, 15);
        if(!(block.getState() instanceof InventoryHolder))
        	throwInvalidArgument(I.t("An output must be a holder."));
        Inventory inventory = ((InventoryHolder) block.getState()).getInventory();

        //Try to add the output to the sorter
        try {
        	ZSorter.getInstance().getSorterManager().setOutput(name, inventory, priority, materials);
        	success(I.t("This holder is now an output of priority {0}.", priority));
        }
        catch(ZSorterException e) {
        	error(e.getMessage());
        }
    }
}
