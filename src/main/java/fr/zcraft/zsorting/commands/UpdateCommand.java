package fr.zcraft.zsorting.commands;


import java.util.StringJoiner;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to create a bank.
 * @author Lucas
 */
@CommandInfo (name = "update", usageParameters = "<name> <description>")
public class UpdateCommand extends ZSortingCommands{
	
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

        //Get the bank from the name
        Bank bank = ZSorting.getInstance().getBankManager().getNameToBank().get(name);
        bank.setDescription(description.toString());
        success(I.t("The bank description has been updated."));
    }
}
