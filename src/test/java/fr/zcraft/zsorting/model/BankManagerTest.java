package fr.zcraft.zsorting.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorting.ZSortingException;
import fr.zcraft.zsorting.ZSortingTest;

/**
 * Tests the {@code BankManager.class} methods.
 * @author Lucas
 */
public class BankManagerTest extends ZSortingTest{
	
	/**
	 * Tests if the banks are correctly added and removed from the manager.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void addRemoveTest() throws ZSortingException{
		//Create a manager, add a bank and test if the bank has been added
		BankManager manager = new BankManager();
		manager.addBank("addRemoveTestBank", "Description");
		Assert.assertEquals(1, manager.getNameToBank().values().size());
		
		//Add a bank and check if exception thrown
		Assert.assertThrows(ZSortingException.class, () -> {
			manager.addBank("addRemoveTestBank", "Description");
		});

		//Remove a bank and check if one removed
		manager.deleteBank("addRemoveTestBank");
		Assert.assertEquals(0,manager.getNameToBank().values().size());
		
		//Remove a bank and check if exception thrown
		Assert.assertThrows(ZSortingException.class, () -> {
			manager.deleteBank("addRemoveTestBank");
		});
	}
	
	/**
	 * Tests if a bank can correctly be found from its input inventory.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void findFromInventoryTest() throws ZSortingException {
		
		//Create a bank, add 2 inputs and test if the bank if found
		Bank bank1 = manager.addBank("findFromInventoryTestBank1", "Description");
		bank1.setInput(inventory0, 1);
		bank1.setInput(inventory1, 1);
		Assert.assertEquals(bank1, manager.getInventoryToBank().get(inventory0));
		Assert.assertEquals(bank1, manager.getInventoryToBank().get(inventory1));

		//Create a bank, add 1 input and test if the bank if found
		Bank bank2 = manager.addBank("findFromInventoryTestBank2", "Description");
		bank2.setInput(inventory2, 1);
		Assert.assertEquals(bank2, manager.getInventoryToBank().get(inventory2));

		//Add an input and test if exception thrown
		Assert.assertThrows(ZSortingException.class, () -> {
			bank2.setInput(inventory1, 1);
		});
		
		//Remove the bank1 and test if the inputs have been removed from the inventory map
		manager.deleteBank(bank1.getName());
		Assert.assertEquals(null, manager.getInventoryToBank().get(inventory0));
		Assert.assertEquals(null, manager.getInventoryToBank().get(inventory1));
		
		//Remove the bank2 and test if the inputs have been removed from the inventory map
		manager.deleteBank(bank2.getName());
		Assert.assertEquals(null, manager.getInventoryToBank().get(inventory2));
	}
	
	/**
	 * Tests if the banks with canCompute flag set are found.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void canComputeTest() throws ZSortingException {
		BankManager manager = new BankManager();
		
		//Add two banks and tests if no bank is found
		Bank bank1 = manager.addBank("canComputeTestBank1", "Description");
		Bank bank2 = manager.addBank("canComputeTestBank2", "Description");
		Assert.assertEquals(Arrays.asList(), manager.canCompute());
		
		//Set the bank1 canCompute flag to true and tests if the bank1 is found
		bank1.setToCompute(true);
		Assert.assertEquals(Arrays.asList(bank1), manager.canCompute());
		
		//Set the bank2 canCompute flag to true and tests if the bank1 and bank2 are found
		bank2.setToCompute(true);
		Assert.assertEquals(Arrays.asList(bank1, bank2), manager.canCompute());
		
		//Set the bank1 canCompute flag to false and tests if the bank2 is found
		bank1.setToCompute(false);
		Assert.assertEquals(Arrays.asList(bank2), manager.canCompute());
		
		//Set the bank2 canCompute flag to false and tests if no bank found
		bank2.setToCompute(false);
		Assert.assertEquals(Arrays.asList(), manager.canCompute());
	}
	
	/**
	 * Tests if a bankManage object is correctly serialized and deserialized.
	 * @throws ZSortingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void serializationTest() throws ZSortingException, FileNotFoundException, IOException, ClassNotFoundException {
		BankManager manager = new BankManager();
		Bank bank = manager.addBank("test", "Simple test bank");
		manager.setInput("test", inventory0, 1);
		manager.setOutput("test", inventory1, 1, Arrays.asList(Material.IRON_INGOT));
		manager.setOutput("test", inventory2, 1, Arrays.asList());

		bank.setSpeed(64);
		bank.commit();

		//Serialization
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("serializationTest.dat")));
		oos.writeObject(manager);
		oos.close();

		//Dezerialisation
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("serializationTest.dat")));
		BankManager deserializedManager = (BankManager) ois.readObject();
		ois.close();
		
		Assert.assertEquals(manager, deserializedManager);
	}
}
