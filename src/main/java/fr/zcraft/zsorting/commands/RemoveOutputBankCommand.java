package fr.zcraft.zsorting.commands;

import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to remove an input.
 * @author Lucas
 */
@CommandInfo (name = "remove_output")
public class RemoveOutputBankCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {

    	if (args.length < 1)
    		throwInvalidArgument(I.t("A bank name is required."));

    	Bank bank = ZSorting.getInstance().getBankManager().getNameToBank().get(args[0]);
    	if(bank != null) {
        	Block block = playerSender().getTargetBlock((Set<Material>) null, 15);
    		if (block.getState() instanceof InventoryHolder) {
    			if(bank.removeOutput(block.getLocation()) != null)
    				success(I.t("This holder is no longer an output."));
    			else
    				error(I.t("This holder is not an output."));
    		}
    		else {
    			error(I.t("An output must be a holder."));
    		}
    	}
    	else {
    		error(I.t("There is no bank with this name."));
    	}
    }
}
