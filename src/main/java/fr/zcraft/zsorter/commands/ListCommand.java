package fr.zcraft.zsorter.commands;


import java.util.Collection;

import org.bukkit.ChatColor;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zlib.components.rawtext.RawText;
import fr.zcraft.zlib.components.rawtext.RawTextPart;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Command triggered to list all the sorters in the world.
 * @author Lucas
 */
@CommandInfo (name = "list")
public class ListCommand extends ZSorterCommands{
	
    @Override
    protected void run() throws CommandException {
    	checkEnable();

        //Get the sorters
        Collection<Sorter> sorters = ZSorter.getInstance().getSorterManager().getNameToSorter().values();
        
        if(sorters.isEmpty()) {
        	send(new RawText()
	    			.then(I.t("No sorter found."))
	    			.color(ChatColor.GOLD)
	    			.build()
        	);
        }
        else {
        	RawTextPart text = new RawText()
        			.then(I.t("{0} sorter(s) found :", sorters.size()))
        				.color(ChatColor.GOLD);
        	
        	for(Sorter sorter:sorters) {
        		text
        			.then("\n- " + sorter.getName())
        				.color(ChatColor.GOLD)
            			.hover(new RawText()
            				.then(I.t("Show the informations of the sorter {0}", sorter.getName())))
            			.command(InfoCommand.class, sorter.getName())
            		.then(sorter.isEnable() ? " ON" : " OFF")
            			.color(sorter.isEnable() ? ChatColor.GREEN : ChatColor.RED)
		    			.hover(new RawText()
		        				.then(I.t("Toggle the sorter {0}", sorter.getName())))
		        			.command(ToggleCommand.class, sorter.getName())
		        	.then(sorter.isToCompute() ? " RUNNING" : "")
		        		.color(ChatColor.AQUA)
        			.then(" (" + sorter.getDescription() + ") ")
        				.color(ChatColor.GRAY).hover(new RawText()
    	        				.then(I.t("Update the description")))
    	        			.suggest(UpdateCommand.class, sorter.getName());
        	}
        	send(text.build());
        }
    }
}
