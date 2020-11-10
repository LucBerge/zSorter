package fr.zcraft.zsorter.commands;


import java.util.StringJoiner;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Command triggered to create a sorter.
 * @author Lucas
 */
@CommandInfo (name = "update", usageParameters = "<name> <description>")
public class UpdateCommand extends ZSorterCommands{
	
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

        //Get the sorter from the name
        Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);
        sorter.setDescription(description.toString());
        success(I.t("The sorter description has been updated."));
    }
}
