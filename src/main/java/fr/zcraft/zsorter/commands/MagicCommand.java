package fr.zcraft.zsorter.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import fr.zcraft.quartzlib.components.commands.CommandException;
import fr.zcraft.quartzlib.components.commands.CommandInfo;
import fr.zcraft.quartzlib.components.i18n.I;
import fr.zcraft.zsorter.Config;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.Input;
import fr.zcraft.zsorter.model.Output;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Command triggered to select a sorter.<br><br>
 * A selection is needed in order to manage inputs and outputs.
 * @author Lucas
 */
@CommandInfo (name = "magic", usageParameters = "<name>")
public class MagicCommand extends ZSorterCommands{

	@Override
	protected void run() throws CommandException {
		checkEnable();

		//Check the number of arguments
		if (args.length < 1)
			throwInvalidArgument(I.t("A sorter name is required."));

		//Get the name
		String name = args[0];

		//Get the sorter from the name
		Sorter sorter = ZSorter.getInstance().getSorterManager().getNameToSorter().get(name);

		if(sorter != null) {
			doMagicEffectPlayer(playerSender(), sorter, Config.MAGIC_EFFECT_DURATION.get());
			
			for(Input input:sorter.getInventoryToInput().values()) {
				highlightGlowingBlock(playerSender(), input.getHolder().getInventory().getLocation(), Config.MAGIC_EFFECT_DURATION.get());
			}
			for(Output output:sorter.getInventoryToOutput().values()) {
				highlightGlowingBlock(playerSender(), output.getHolder().getInventory().getLocation(), Config.MAGIC_EFFECT_DURATION.get());
			}
			success(I.t("The magic effect has been applied on the sorter {0} for {1} ticks.", sorter.getName(), Config.MAGIC_EFFECT_DURATION.get()));
		}
		else {
			error(I.t("There is no sorter with this name."));
		}

        
	}

	/**
	 * Do magic effect on the player with the given sorter.<br>
	 * This method add the pair player and sorter to the map. It will be used to find the sorter from the player when this one will left click on a block.
	 * @param player - Player on which apply the effect.
	 * @param sorter - Sorter selected by the player.
	 * @param duration - Duration of the effect.
	 */
	public void doMagicEffectPlayer(Player player, Sorter sorter, Integer duration) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(ZSorter.getInstance(), () -> {
			ZSorter.getInstance().getSorterManager().getPlayerToSorter().put(player, sorter);
			Bukkit.getScheduler().scheduleSyncDelayedTask(ZSorter.getInstance(), () -> {
				ZSorter.getInstance().getSorterManager().getPlayerToSorter().remove(player);
				success(player, I.t("The magic effect has stopped."));
			}, Config.MAGIC_EFFECT_DURATION.get());
		}, 0);
	}
	
	/**
	 * Highlights a given location for a given duration.
	 * @param p - Player on which play the effect.
	 * @param loc - Location of the block to highlight.
	 * @param duration - Duration of the effect.
	 */
	public void highlightGlowingBlock(Player p, Location loc, Integer duration){
		/*Bukkit.getScheduler().scheduleSyncDelayedTask(ZSorter.getInstance(), () -> {
			try {
				Object connection = NMSNetwork.getPlayerConnection(p);
				Object nmsScoreBoard = NMSNetwork.getPlayerHandle(p);	//Ne sert à rien
				
				Object shulkerClass = Reflection.getMinecraftClassByName("CraftShulker");
				Object handle = Reflection.call(loc.getWorld(), "getHandle");
				Object shulker = Reflection.instantiate(shulkerClass.getClass(), handle);

				Reflection.call(shulker, "setLocation", loc.getX(), loc.getY(), loc.getZ(), 0, 0);
				Reflection.call(shulker, "setFlag", 6, true);
				Reflection.call(shulker, "setFlag", 5, true);

				Object spawnPacketClass = Reflection.getMinecraftClassByName("PacketPlayOutSpawnEntityLiving");
				Object spawnPacket = Reflection.instantiate(spawnPacketClass.getClass(), shulker);
				Reflection.call(connection, "sendPacket", spawnPacket);

				Bukkit.getScheduler().scheduleSyncDelayedTask(ZSorter.getInstance(), () -> {
					try {
						Object destroyPacketClass = Reflection.getMinecraftClassByName("PacketPlayOutEntityDestroy");
						Object destroyPacket = Reflection.instantiate(destroyPacketClass.getClass(), Reflection.call(shulker, "getId"));
						Reflection.call(connection, "sendPacket", destroyPacket);
					}catch(InvocationTargetException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
						e.printStackTrace();
					}
				}, duration);
			}catch(InvocationTargetException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
				e.printStackTrace();
			}
		}, 0);*/
	}

	@Override
	protected List<String> complete() throws CommandException{
		if(args.length <= 1) {
			return completeSorterName(args[0]);
		}
		return null;
	}
}
