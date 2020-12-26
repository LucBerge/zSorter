package fr.zcraft.zsorter.model.serializer;

import java.lang.reflect.Type;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.InventoryHolder;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.utils.InventoryUtils;

/**
 * Gson adapter used to define how to serialize an inventory.
 * @author Lucas
 *
 */
public class InventoryAdapter implements JsonSerializer<InventoryHolder>, JsonDeserializer<InventoryHolder>{
	
	@Override
	public JsonElement serialize(InventoryHolder src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonInputOutput = new JsonObject();
		jsonInputOutput.addProperty("world", src.getInventory().getLocation().getWorld().getName());
		jsonInputOutput.addProperty("x", new Integer(src.getInventory().getLocation().getBlockX()));
		jsonInputOutput.addProperty("y", new Integer(src.getInventory().getLocation().getBlockY()));
		jsonInputOutput.addProperty("z", new Integer(src.getInventory().getLocation().getBlockZ()));
        return jsonInputOutput;
	}

	@Override
	public InventoryHolder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		
		JsonObject jsonObject = json.getAsJsonObject();
		String worldName = jsonObject.get("world").getAsString();
		int x = jsonObject.get("x").getAsInt();
		int y = jsonObject.get("y").getAsInt();
		int z = jsonObject.get("z").getAsInt();
		
		if(worldName == null)
			throw new JsonParseException("The inventory world name is null. The input/output will be removed.");
		
		World world = ZSorter.getInstance().getServer().getWorld(worldName);
		
		if(world == null)
			throw new JsonParseException(String.format("The world %s does not exist anymore. The input/output in this world will be removed.", worldName));

		Block block = world.getBlockAt(x, y, z);
		
		if(block == null)
        	throw new JsonParseException(String.format("The block at the location x=%d, y=%d, z=%d in the world %s does not exist. The input/output at this location will be removed.", x, y, z, worldName));

		try {
			return InventoryUtils.findInventoryFromBlock(block);
		} catch (ZSorterException e) {
			throw new JsonParseException(e);
		}
	}
}
