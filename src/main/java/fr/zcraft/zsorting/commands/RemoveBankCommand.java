package fr.zcraft.zsorting.commands;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to remove a bank.
 * @author Lucas
 */
@CommandInfo (name = "remove", usageParameters = "<name>")
public class RemoveBankCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {     
    	
        if (args.length < 1)
            throwInvalidArgument(I.t("A bank name is required."));

        Bank bank = ZSorting.getInstance().getBankManager().getBanks().get(args[0]);
        
        if(bank == null) {
            error(I.t("There is no bank with this name."));
        }
        else {
        	ZSorting.getInstance().getBankManager().getBanks().remove(bank.getName());
            success(I.t("The bank as been removed."));
        }
    }
}
