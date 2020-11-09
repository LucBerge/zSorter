package fr.zcraft.zsorting.commands;

import org.bukkit.command.CommandSender;

import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zsorting.Permissions;
import fr.zcraft.zsorting.ZSorting;

/**
 * Abstract ZSorting commands.<br><br>
 * This class provides usefull method to the children commands.
 * @author Lucas
 */
abstract public class ZSortingCommands extends Command{
	
	/**
	 * Checks whether the plugin is disable.<br>
	 * Throw a CommandException if it is.
	 * @throws CommandException if reason of the 
	 */
	public void checkEnable() throws CommandException {
		if(!ZSorting.getInstance().isEnable())
			error("This plugin has been disabled for the following reason:\n"
				+ String.format("Cannot read the content of the file %s. The file might be corrupted.\n", ZSorting.getInstance().getDataPath())
				+ "This security is here to prevent all lose of data.\n"
				+ "To enable the plugin, you can either :\n"
				+ "- Fix the file content (and keep your data)\n"
				+ "- Remove the file (and loose your data)");
	}
	
    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.ADMIN.grantedTo(sender);
    }
}
