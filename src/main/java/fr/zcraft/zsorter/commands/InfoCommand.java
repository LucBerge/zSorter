package fr.zcraft.zsorter.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.DisplayMode;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Command triggered to display a sorter informations.
 * @author Lucas
 */
@CommandInfo (name = "info", usageParameters = "<name> <mode>")
public class InfoCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();
    	
    	//Check the number of arguments
        if (args.length < 1)
            throwInvalidArgument(I.t("A sorter name is required."));

        //Get the name
        String name = args[0];
        
        //Get the mode
        DisplayMode mode = DisplayMode.OUTPUTS;
        if(args.length > 1) {
        	try {
        		mode = DisplayMode.valueOf(args[1].toUpperCase());
        	}
        	catch(IllegalArgumentException e) {
        		throwInvalidArgument(I.t("The display mode must be either <OUTPUTS> or <ITEMS>."));
        	}
        }

        //Get the sorter from the name
        Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);
        
        if(sorter == null) {
            error(I.t("There is no sorter with this name."));
        }
        else {
        	send(sorter.toRawText(mode));
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
    	else if(args.length <= 2) {
    		return Arrays.asList(DisplayMode.ITEMS, DisplayMode.OUTPUTS)
    				.stream()
    				.map(dm -> dm.toString().toLowerCase())
    				.filter(dm -> dm.startsWith(args[1]))
    				.collect(Collectors.toList());
    	}
    	return null;
    }
}
