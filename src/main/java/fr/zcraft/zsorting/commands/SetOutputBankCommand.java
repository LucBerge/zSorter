package fr.zcraft.zsorting.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

import fr.zcraft.zlib.components.commands.CommandException;
import fr.zcraft.zlib.components.commands.CommandInfo;
import fr.zcraft.zlib.components.i18n.I;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.Bank;
import fr.zcraft.zsorting.model.Output;

/**
 * Command triggered to remove a bank.
 * @author Lucas
 */
@CommandInfo (name = "set_output", usageParameters = "<name> <priority> (<items>)")
public class SetOutputBankCommand extends ZSortingCommands{
	
    @Override
    protected void run() throws CommandException {     

        if (args.length < 2)
            throwInvalidArgument(I.t("A bank name and an output priority are required."));
        
        int priority = 0;
        try {
        	priority = Integer.parseInt(args[1]);
            if (priority < 1)
                throwInvalidArgument(I.t("The output priority must be higher or equal to 1."));
        }catch(NumberFormatException e) {
            throwInvalidArgument(I.t("The output priority must be an integer."));
        }
        
        List<Material> materials = new ArrayList<Material>();

        if (args.length > 2) {
        	for (int i = 2; i < args.length; i++) {
        		Material material = Material.getMaterial(args[i].toUpperCase());
        		if(material != null)
        			materials.add(material);
        		else
        			error(I.t("Could not found material {0} : Ignored.", args[i]));
        	}
        }

        Bank bank = ZSorting.getInstance().getBankManager().getBanks().get(args[0]);
        if(bank != null) {
            Block block = playerSender().getTargetBlock((Set<Material>) null, 15);
            if (block.getState() instanceof InventoryHolder) {
            	Output output = bank.getOutputs().get(block.getLocation());
            	if(output == null) {
            		output = new Output(bank, block.getLocation(), priority);
            		bank.getOutputs().put(block.getLocation(), output);
            	}
            	else {
            		output.setPriority(priority);
            	}
        		output.setMaterials(materials);
            	bank.sortOutputs();
        		success(I.t("This holder is now an output of priority {0}.", priority));
            }
            else {
            	error(I.t("An output must be a holder."));
            }
        }
        else {
        	error(I.t("There is no bank with this name."));
        }
    }
}
