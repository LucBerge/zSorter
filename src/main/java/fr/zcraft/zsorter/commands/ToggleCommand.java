package fr.zcraft.zsorter.commands;

import java.util.List;
import java.util.stream.Collectors;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;
import fr.zcraft.zsorter.tasks.SortTask;

/**
 * Command triggered to select a sorter.<br><br>
 * A selection is needed in order to manage inputs and outputs.
 * @author Lucas
 */
@CommandInfo (name = "toggle", usageParameters = "<name>")
public class ToggleCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();

    	//Check the number of arguments
        if (args.length < 1)
            throwInvalidArgument(I.t("A sorter name is required."));

        //Get the name
        String name = args[0];

        //Get the sorter from the name
        Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);
        
        if(sorter == null) {
            error(I.t("There is no sorter with this name."));
        }
        else {
        	sorter.setEnable(!sorter.isEnable());
        	if(sorter.isEnable()) {
            	sorter.setToCompute(true);
            	if(!sorter.hasInput())
            		warning(I.t("The sorter does not have any input."));
            	if(!sorter.hasOverflow())
            		warning(I.t("The sorter does not have any overflow. Some items might clog up the inputs."));
            	SortTask.getInstance().start();
            	success(I.t("The sorter has been enabled."));
        	}
        	else
        		success(I.t("The sorter has been disabled."));
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
