package fr.zcraft.zsorting.commands;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to display a bank informations.
 * @author Lucas
 */
@CommandInfo (name = "info", usageParameters = "<name>")
public class InfoCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {

        if (args.length < 1)
            throwInvalidArgument(I.t("A bank name is required."));

        Bank bank = ZSorting.getInstance().getBankManager().getNameToBank().get(args[0]);
        
        if(bank == null) {
            error(I.t("There is no bank with this name."));
        }
        else {
        	send(bank.toRawText());
        }
    }
}
