package fr.zcraft.zsorter.commands;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Command triggered to change the speed of a sorter.
 * @author Lucas
 */
@CommandInfo (name = "speed", usageParameters = "<name> <value>")
public class SpeedCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();
    	
    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A sorter name and a sorter speed are required."));

        //Get the name
        String name = args[0];

        //Get the speed value
        int speed = 0;
        try {
        	speed = Integer.parseInt(args[1]);
            if (speed < 1 || 64 < speed)
                throwInvalidArgument(I.t("The sorter speed must be between 1 and 64."));
        }catch(NumberFormatException e) {
            throwInvalidArgument(I.t("The sorter speed must be an integer."));
        }

        //Get the sorter
        Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);
        if(sorter != null) {
        	sorter.setSpeed(speed);
            success(I.t("The sorter speed of the sorter has been set to {0}.", speed));
        }
        else {
            error(I.t("There is no sorter with this name."));
        }
    }
}
