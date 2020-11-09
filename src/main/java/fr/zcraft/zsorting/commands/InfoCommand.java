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
@CommandInfo (name = "info", usageParameters = "<name> <mode>")
public class InfoCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {
    	//Check the number of arguments
        if (args.length < 1)
            throwInvalidArgument(I.t("A bank name is required."));

        //Get the name
        String name = args[0];
        
        //Get the mode
        int mode = 0;
        if(args.length > 2) {
        	try {
        		mode = Integer.parseInt(args[1]);
                if (mode < 0 || 1 < mode)
                    throwInvalidArgument(I.t("The display mode must be 0 or 1."));
            }catch(NumberFormatException e) {
                throwInvalidArgument(I.t("The display mode must be an integer."));
            }
        }
        
        //Get the bank from the name
        Bank bank = ZSorting.getInstance().getBankManager().getNameToBank().get(name);
        
        if(bank == null) {
            error(I.t("There is no bank with this name."));
        }
        else {
        	send(bank.toRawText(mode > 0));
        }
    }
}
