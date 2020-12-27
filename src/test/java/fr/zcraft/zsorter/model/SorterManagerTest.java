package fr.zcraft.zsorter.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.ZSorterTest;
import fr.zcraft.zsorter.model.serializer.InventoryHolderAdapter;
import fr.zcraft.zsorter.model.serializer.PostProcessAdapterFactory;
import fr.zcraft.zsorter.model.serializer.SorterManagerAdapter;

/**
 * Tests the {@code SorterManager.class} methods.
 * @author Lucas
 */
public class SorterManagerTest extends ZSorterTest{
	
	/**
	 * Tests if the sorters are correctly added and removed from the manager.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	@Test
	public void addRemoveTest() throws ZSorterException{
		//Create a manager, add a sorter and test if the sorter has been added
		SorterManager manager = new SorterManager();
		manager.createSorter("addRemoveTestSorter", "Description");
		Assert.assertEquals(1, manager.getNameToSorter().values().size());
		
		//Add a sorter and check if exception thrown
		Assert.assertThrows(ZSorterException.class, () -> {
			manager.createSorter("addRemoveTestSorter", "Description");
		});

		//Remove a sorter and check if one removed
		manager.deleteSorter("addRemoveTestSorter");
		Assert.assertEquals(0,manager.getNameToSorter().values().size());
		
		//Remove a sorter and check if exception thrown
		Assert.assertThrows(ZSorterException.class, () -> {
			manager.deleteSorter("addRemoveTestSorter");
		});
	}
	
	/**
	 * Tests if a sorter can correctly be found from its input inventory.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	/*@Test
	public void findFromInventoryTest() throws ZSorterException {
		
		//Create a sorter, add 2 inputs and test if the sorter if found
		Sorter sorter1 = manager.createSorter("findFromInventoryTestSorter1", "Description");
		sorter1.setInput(inventory0, 1);
		sorter1.setInput(inventory1, 1);
		Assert.assertEquals(sorter1, manager.getInventoryToSorter().get(inventory0));
		Assert.assertEquals(sorter1, manager.getInventoryToSorter().get(inventory1));

		//Create a sorter, add 1 input and test if the sorter if found
		Sorter sorter2 = manager.createSorter("findFromInventoryTestSorter2", "Description");
		sorter2.setInput(inventory2, 1);
		Assert.assertEquals(sorter2, manager.getInventoryToSorter().get(inventory2));

		//Add an input and test if exception thrown
		Assert.assertThrows(ZSorterException.class, () -> {
			sorter2.setInput(inventory1, 1);
		});
		
		//Remove the sorter1 and test if the inputs have been removed from the inventory map
		manager.deleteSorter(sorter1.getName());
		Assert.assertEquals(null, manager.getInventoryToSorter().get(inventory0));
		Assert.assertEquals(null, manager.getInventoryToSorter().get(inventory1));
		
		//Remove the sorter2 and test if the inputs have been removed from the inventory map
		manager.deleteSorter(sorter2.getName());
		Assert.assertEquals(null, manager.getInventoryToSorter().get(inventory2));
	}*/
	
	/**
	 * Tests if the sorters with canCompute flag set are found.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	@Test
	public void canComputeTest() throws ZSorterException {
		SorterManager manager = new SorterManager();
		
		//Add two sorters and tests if no sorter is found
		Sorter sorter1 = manager.createSorter("canComputeTestSorter1", "Description");
		Sorter sorter2 = manager.createSorter("canComputeTestSorter2", "Description");
		Assert.assertEquals(Arrays.asList(), manager.canCompute());
		
		//Set the sorter1 canCompute flag to true and tests if the sorter1 is found
		sorter1.setToCompute(true);
		Assert.assertEquals(Arrays.asList(sorter1), manager.canCompute());
		
		//Set the sorter2 canCompute flag to true and tests if the sorter1 and sorter2 are found
		sorter2.setToCompute(true);
		Assert.assertEquals(Arrays.asList(sorter1, sorter2), manager.canCompute());
		
		//Set the sorter1 canCompute flag to false and tests if the sorter2 is found
		sorter1.setToCompute(false);
		Assert.assertEquals(Arrays.asList(sorter2), manager.canCompute());
		
		//Set the sorter2 canCompute flag to false and tests if no sorter found
		sorter2.setToCompute(false);
		Assert.assertEquals(Arrays.asList(), manager.canCompute());
	}
	
	/**
	 * Tests if a sorterManage object is correctly serialized and deserialized.
	 * @throws ZSorterException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void gsonSerializationTest() throws ZSorterException, FileNotFoundException, IOException, ClassNotFoundException {
		SorterManager manager = new SorterManager();
		Sorter sorter = manager.createSorter("test", "Simple test sorter");
		manager.setInput("test", inventory0, 1);
		manager.setOutput("test", inventory1, 1, Arrays.asList(Material.IRON_INGOT));
		manager.setOutput("test", inventory2, 1, Arrays.asList());
		
		sorter.setSpeed(64);

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapterFactory(new PostProcessAdapterFactory());
		gsonBuilder.registerTypeHierarchyAdapter(Inventory.class, new InventoryHolderAdapter());
		gsonBuilder.registerTypeAdapter(SorterManager.class, new SorterManagerAdapter());
		
		Gson customGson = gsonBuilder.create();
		
		//Serialization
		FileWriter fr = new FileWriter("serializationTest.dat");
		fr.write(customGson.toJson(manager));
		fr.close();

		//Dezerialisation
		BufferedReader br = new BufferedReader(new FileReader("serializationTest.dat"));
		SorterManager deserializedManager = customGson.fromJson(br, SorterManager.class);
		br.close();
		
		Assert.assertEquals(manager, deserializedManager);
	}
}
