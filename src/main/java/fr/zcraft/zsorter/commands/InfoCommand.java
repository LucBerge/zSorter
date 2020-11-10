package fr.zcraft.zsorter.commands;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
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
        int mode = 0;
        if(args.length > 1) {
        	try {
        		mode = Integer.parseInt(args[1]);
                if (mode < 0 || 1 < mode)
                    throwInvalidArgument(I.t("The display mode must be 0 or 1."));
            }catch(NumberFormatException e) {
                throwInvalidArgument(I.t("The display mode must be an integer."));
            }
        }
        
        //Get the sorter from the name
        Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);
        
        if(sorter == null) {
            error(I.t("There is no sorter with this name."));
        }
        else {
        	send(sorter.toRawText(mode > 0));
        }
    }
}
