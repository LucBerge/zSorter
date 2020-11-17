package fr.zcraft.zsorter.commands;


import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Command triggered to creSate a sorter.
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
    
    @Override
    protected List<String> complete() throws CommandException{
    	if(args.length <= 1) {
    		return completeSorterName(args[0]);
    	}
    	return null;
    }
}
