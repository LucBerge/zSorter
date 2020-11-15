package fr.zcraft.zsorter.commands;

import java.util.List;
import java.util.stream.Collectors;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;

/**
 * Command triggered to remove a sorter.
 * @author Lucas
 */
@CommandInfo (name = "delete", usageParameters = "<name>")
public class DeleteCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();

    	//Check the number of arguments
    	if (args.length < 1)
    		throwInvalidArgument(I.t("A sorter name is required."));

        //Get the name
        String name = args[0];
        
    	try {
    		ZSorter.getInstance().getSorterManager().deleteSorter(name);
    		success(I.t("The sorter has been removed."));
    	} catch (ZSorterException e) {
    		error(e.getMessage());
    	}
    }
    
    @Override
    protected List<String> complete() throws CommandException{
    	if(args.length <= 1) {
    		return ZSorter.getInstance().getSorterManager().getNameToSorter().keySet()
    				.stream()
    				.filter(s -> s.startsWith(args[0]))
    				.collect(Collectors.toList());
    	}
    	return null;
    }
}
