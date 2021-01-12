package fr.zcraft.zsorter.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.quartzlib.components.rawtext.RawTextPart;
import fr.zcraft.quartzlib.tools.commands.PaginatedTextView;
import fr.zcraft.quartzlib.tools.text.RawMessage;
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
        if (args.length < 2)
            throwInvalidArgument(I.t("A sorter name and a mode is required."));

        //Get the name
        String name = args[0];
        
        //Get the mode
        DisplayMode mode = DisplayMode.ITEMS;
        try {
        	mode = DisplayMode.valueOf(args[1].toUpperCase());
        }
        catch(IllegalArgumentException e) {
        	throwInvalidArgument(I.t("The display mode must be either OUTPUTS or ITEMS."));
        }
        
        //Get the page
        int page = 1;
        if(args.length > 2 && args[2].startsWith("--page=")) {
                try {
                	page = Integer.valueOf(args[2].split("=")[1]);
                } catch (NumberFormatException e) {
                	//Default page set to 1;
                }
        }

        //Get the sorter from the name
        Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);
        
        if(sorter == null) {
            error(I.t("There is no sorter with this name."));
        }
        else {
        	List<RawTextPart> sorterRawText = sorter.toRawText(mode);
        	new SorterPagination(sorter, mode)
        		.setData(sorterRawText.toArray(new RawTextPart[sorterRawText.size()]))
        		.setCurrentPage(page)
        		.setItemsPerPage(15)
        		.display(playerSender());
        }
    }
    
    @Override
    protected List<String> complete() throws CommandException{
    	if(args.length <= 1) {
    		return completeSorterName(args[0]);
    	}
    	else if(args.length <= 2) {
    		return Arrays.asList(DisplayMode.ITEMS, DisplayMode.OUTPUTS)
    				.stream()
    				.map(dm -> dm.toString().toLowerCase())
    				.filter(dm -> dm.startsWith(args[1]))
    				.collect(Collectors.toList());
    	}
    	return null;
    }
    
    private class SorterPagination extends PaginatedTextView<RawTextPart> {

    	private Sorter sorter;
    	private DisplayMode mode;
    	
    	public SorterPagination(Sorter sorter, DisplayMode mode) {
    		super();
    		this.sorter = sorter;
    		this.mode = mode;
    	}
    	
        @Override
        protected void displayItem(CommandSender receiver, RawTextPart textPart) {
            RawMessage.send(receiver, textPart.build());
        }

        @Override
        protected String getCommandToPage(int page) {
            return build(this.sorter.getName(), this.mode.toString(), "--page=" + page);
        }
    }
}
