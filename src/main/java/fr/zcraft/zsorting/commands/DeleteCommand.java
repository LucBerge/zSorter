package fr.zcraft.zsorting.commands;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.ZSortingException;

/**
 * Command triggered to remove a bank.
 * @author Lucas
 */
@CommandInfo (name = "delete", usageParameters = "<name>")
public class DeleteCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();

    	//Check the number of arguments
    	if (args.length < 1)
    		throwInvalidArgument(I.t("A bank name is required."));

        //Get the name
        String name = args[0];
        
    	try {
    		ZSorting.getInstance().getBankManager().deleteBank(name);
    		success(I.t("The bank has been removed."));
    	} catch (ZSortingException e) {
    		error(e.getMessage());
    	}
    }
}
