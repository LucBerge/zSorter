package fr.zcraft.zsorting.commands;


import java.util.StringJoiner;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.ZSortingException;

/**
 * Command triggered to create a bank.
 * @author Lucas
 */
@CommandInfo (name = "add", usageParameters = "<name> <description>")
public class AddCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();

    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A bank name and a description are required."));

        //Get the name
        String name = args[0];
        
        //Get the bank description
        final StringJoiner description = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++)
        	description.add(args[i]);

        try {
        	ZSorting.getInstance().getBankManager().addBank(name, description.toString());
        	success(I.t("The bank has been created."));
        } catch (ZSortingException e) {
        	error(e.getMessage());
        }
    }
}
