package fr.zcraft.zsorting.commands;


import java.util.Collection;

import org.bukkit.ChatColor;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.components.rawtext.RawTextPart;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;

/**
 * Command triggered to list all the banks in the world.
 * @author Lucas
 */
@CommandInfo (name = "list")
public class ListCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {
        
        Collection<Bank> banks = ZSorting.getInstance().getBankManager().getBanks().values();
        if(banks.isEmpty()) {
        	send(new RawText()
	    			.then(I.t("No bank found."))
	    			.color(ChatColor.GOLD)
	    			.build()
        	);
        }
        else {
        	RawTextPart text = new RawText()
        			.then(I.t("{0} bank(s) found :", banks.size()))
        				.color(ChatColor.GOLD);
        	
        	for(Bank bank:banks) {
        		text
        			.then("\n     " + bank.getName())
        				.color(ChatColor.GOLD)
            			/*.hover(new RawText()
            					.then(I.t("Show the informations of the bank {0}", bank.getName())))
            			.command(InfoBankCommand.class, bank.getName())*/
        			.then(" (" + bank.getDescription() + ") ")
        				.color(ChatColor.GRAY)
            		.then(bank.getState() ? "ON" : "OFF")
            			.color(bank.getState() ? ChatColor.GREEN : ChatColor.RED);
        	}
        	send(text.build());
        }
    }
}
