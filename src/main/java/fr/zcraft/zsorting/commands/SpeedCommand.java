package fr.zcraft.zsorting.commands;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to change the speed of a bank.
 * @author Lucas
 */
@CommandInfo (name = "speed", usageParameters = "<name> <value>")
public class SpeedCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {
    	//Check the number of arguments
        if (args.length < 2)
            throwInvalidArgument(I.t("A bank name and a sorting speed are required."));

        //Get the name
        String name = args[0];

        //Get the speed value
        int speed = 0;
        try {
        	speed = Integer.parseInt(args[1]);
            if (speed < 1 || 64 < speed)
                throwInvalidArgument(I.t("The sorting speed must be between 1 and 64."));
        }catch(NumberFormatException e) {
            throwInvalidArgument(I.t("The sorting speed must be an integer."));
        }

        //Get the bank
        Bank bank = ZSorting.getInstance().getBankManager().getNameToBank().get(name);
        if(bank != null) {
        	bank.setSpeed(speed);
            success(I.t("The sorting speed of the bank has been set to {0}.", speed));
        }
        else {
            error(I.t("There is no bank with this name."));
        }
    }
}
