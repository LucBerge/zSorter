package fr.zcraft.zsorter.commands;


import java.util.StringJoiner;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;

/**
 * Command triggered to create a sorter.
 * @author Lucas
 */
@CommandInfo (name = "create", usageParameters = "<name> <description>")
public class CreateCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();

    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A sorter name and a description are required."));

        //Get the name
        String name = args[0];
        
        //Get the sorter description
        final StringJoiner description = new StringJoiner(" ");
        for (int i = 1; i < args.length; i++)
        	description.add(args[i]);

        try {
        	ZSorter.getInstance().getSorterManager().createSorter(name, description.toString());
        	success(I.t("The sorter has been created."));
        } catch (ZSorterException e) {
        	error(e.getMessage());
        }
    }
}
