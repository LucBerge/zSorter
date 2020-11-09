package fr.zcraft.zsorting.commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.ZSortingException;

/**
 * Command triggered to remove a bank.
 * @author Lucas
 */
@CommandInfo (name = "set_input", usageParameters = "<name> <priority>")
public class SetInputBankCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();
    	
    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A bank name and an input priority are required."));
        
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
        if(!(block.getState() instanceof InventoryHolder))
        	throwInvalidArgument(I.t("An input must be a holder."));
        Inventory inventory = ((InventoryHolder) block.getState()).getInventory();
        
        //Try to add the input to the bank
        try {
        	ZSorting.getInstance().getBankManager().setInput(name, inventory, priority);
        	success(I.t("This holder is now an input of priority {0}.", priority));
        }
        catch(ZSortingException e) {
        	error(e.getMessage());
        }
    }
}
