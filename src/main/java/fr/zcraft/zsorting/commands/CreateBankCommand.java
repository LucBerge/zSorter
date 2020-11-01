package fr.zcraft.zsorting.commands;


import java.util.StringJoiner;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to create a bank.
 * @author Lucas
 */
@CommandInfo (name = "create", usageParameters = "<name> <description>")
public class CreateBankCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {

        if (args.length < 2)
            throwInvalidArgument(I.t("A bank name and a description are required."));

        Bank bank = ZSorting.getInstance().getBankManager().getBanks().get(args[0]);
       
        if(bank == null) {
            final StringJoiner description = new StringJoiner(" ");
            for (int i = 1; i < args.length; i++)
            	description.add(args[i]);
            
            ZSorting.getInstance().getBankManager().getBanks().put(args[0], new Bank(args[0], description.toString()));
            success(I.t("The bank has been created."));
        }
        else
            error(I.t("A bank with this name already exists."));
    }
    
    
}
