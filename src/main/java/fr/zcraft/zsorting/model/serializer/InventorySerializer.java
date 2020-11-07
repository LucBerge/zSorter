package fr.zcraft.zsorting.model.serializer;

import java.lang.reflect.Type;

import org.bukkit.inventory.Inventory;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * This class defines the logic behind serialization and deserialization.
 * @author Lucas
 *
 */
public class InventorySerializer implements JsonSerializer<Inventory>, JsonDeserializer<Inventory>{

	@Override
	public JsonElement serialize(Inventory src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonInputOutput = new JsonObject();
		jsonInputOutput.add("location", context.serialize(src.getLocation()));
        return jsonInputOutput;
	}

	@Override
	public Inventory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		return null;
	}
}
