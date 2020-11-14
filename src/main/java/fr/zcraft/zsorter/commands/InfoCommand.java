package fr.zcraft.zsorter.commands;

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
        		mode = DisplayMode.valueOf(args[1]);
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
}
