package fr.zcraft.zsorter.model.serializer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import fr.zcraft.zsorter.model.Input;
import fr.zcraft.zsorter.model.Output;
import fr.zcraft.zsorter.model.Sorter;
import fr.zcraft.zsorter.model.SorterManager;

/**
 * Gson adapter used to define how to serialize a sorter manager.
 * @author Lucas
 *
 */
public class SorterManagerAdapter implements JsonSerializer<SorterManager>, JsonDeserializer<SorterManager>{
	
	@Override
	public JsonElement serialize(SorterManager src, Type typeOfSrc, JsonSerializationContext context) {
		Type type = new TypeToken<Collection<Sorter>>() {}.getType();
		JsonObject jsonSorterManager = new JsonObject();
		jsonSorterManager.add("sorters", context.serialize(src.getNameToSorter().values(), type));
        return jsonSorterManager;
	}

	@Override
	public SorterManager deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		Type type = new TypeToken<Collection<Sorter>>() {}.getType();
		Collection<Sorter> sorters = context.deserialize(json.getAsJsonObject().get("sorters"), type);
		SorterManager manager = new SorterManager();
		Iterator<Sorter> iterator = sorters.iterator();
		
		while (iterator.hasNext()) {
			Sorter sorter = iterator.next();
			manager.getNameToSorter().putIfAbsent(sorter.getName(), sorter);
			for(Input input:sorter.getInventoryToInput().values()) {
				manager.getInventoryToSorter().putIfAbsent(input.getInventory(), sorter);
			}
			for(Output output:sorter.getInventoryToOutput().values()) {
				manager.getInventoryToSorter().putIfAbsent(output.getInventory(), sorter);
			}
	    }
		
		return manager;
	}
	
}
