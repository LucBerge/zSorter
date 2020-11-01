package fr.zcraft.zsorting.commands;

import org.bukkit.command.CommandSender;

import fr.zcraft.zlib.components.commands.Command;
import fr.zcraft.zsorting.Permissions;

/**
 * Abstract ZSorting commands.<br><br>
 * This class provides usefull method to the children commands.
 * @author Lucas
 */
abstract public class ZSortingCommands extends Command{
	
    @Override
    public boolean canExecute(CommandSender sender)
    {
        return Permissions.ADMIN.grantedTo(sender);
    }
}
