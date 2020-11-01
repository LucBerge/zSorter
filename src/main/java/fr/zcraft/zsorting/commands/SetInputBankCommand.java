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
import fr.zcraft.zsorting.model.Input;

/**
 * Command triggered to remove a bank.
 * @author Lucas
 */
@CommandInfo (name = "set_input", usageParameters = "<name> <priority>")
public class SetInputBankCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {     
    	
        if (args.length < 2)
            throwInvalidArgument(I.t("A bank name and an input priority are required."));
        
        int priority = 0;
        try {
        	priority = Integer.parseInt(args[1]);
            if (priority < 1)
                throwInvalidArgument(I.t("The input priority must be higher or equal to 1."));
        }catch(NumberFormatException e) {
            throwInvalidArgument(I.t("The input priority must be an integer."));
        }

        Bank bank = ZSorting.getInstance().getBankManager().getBanks().get(args[0]);
        if(bank != null) {
            Block block = playerSender().getTargetBlock((Set<Material>) null, 15);
            if(block.getState() instanceof InventoryHolder) {
            	Input input = bank.getInputs().get(block.getLocation());
            	if(input == null) {
            		input = new Input(bank, block.getLocation(), priority);
            		bank.getInputs().put(block.getLocation(), input);
            	}
            	else {
            		input.setPriority(priority);
            	}
            	bank.sortInputs();
        		success(I.t("This holder is now an input of priority {0}.", priority));
            }
            else {
            	error(I.t("An input must be a holder."));
            }
        }
        else {
        	error(I.t("There is no bank with this name."));
        }
    }
}
