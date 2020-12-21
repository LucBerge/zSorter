package fr.zcraft.zsorter.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import fr.zcraft.quartzlib.components.commands.Command;
import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.zsorter.Permissions;
import fr.zcraft.zsorter.ZSorter;

/**
 * Abstract ZSorter commands.<br><br>
 * This class provides usefull method to the children commands.
 * @author Lucas
 */
abstract public class ZSorterCommands extends Command{
	
	/**
	 * Checks whether the plugin is disable.<br>
	 * Throw a CommandException if it is.
	 * @throws CommandException if reason of the 
	 */
	public void checkEnable() throws CommandException {
		if(!ZSorter.getInstance().isEnable())
			error("This plugin has been disabled for the following reason:\n"
				+ String.format("Cannot read the content of the file %s. The file might be corrupted.\n", ZSorter.getInstance().getDataPath())
				+ "This security is here to prevent all lose of data.\n"
				+ "To enable the plugin, you can either :\n"
				+ "- Fix the file content (and keep your data)\n"
				+ "- Remove the file (and loose your data)");
	}
	
	/**
	 * Complete the sorter name when typing a command.
	 * @param arg Current type value.
	 * @return List of possible sorters.
	 */
	public List<String> completeSorterName(String arg){
		return ZSorter.getInstance().getSorterManager().getNameToSorter().keySet()
				.stream()
				.filter(s -> s.startsWith(arg))
				.collect(Collectors.toList());
	}
	
    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.ADMIN.grantedTo(sender);
    }
}
