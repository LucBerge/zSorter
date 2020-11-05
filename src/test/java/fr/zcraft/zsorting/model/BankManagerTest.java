package fr.zcraft.zsorting.model;

import java.util.Arrays;

import org.bukkit.Location;
import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorting.ZSortingException;

/**
 * Tests the {@code BankManager.class} methods.
 * @author Lucas
 */
public class BankManagerTest {

	/**
	 * Tests if the banks are correctly added and removed from the manager.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void addRemoveTest() throws ZSortingException{
		BankManager manager = new BankManager();
		
		//Add a bank and check if the bank is added
		manager.addBank("addRemoveTestBank", "Description");
		Assert.assertEquals(1,manager.getNameToBank().values().size());
		
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
	 * Tests if a bank can correctly be found from its input location.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void findFromLocationTest() throws ZSortingException {
		BankManager manager = new BankManager();
		
		//Create a bank, add 2 inputs and test if the bank if found
		Bank bank1 = manager.addBank("findFromLocationTestBank1", "Description");
		bank1.setInput(new Location(null, 0,0,0), 1);
		bank1.setInput(new Location(null, 0,0,1), 1);
		Assert.assertEquals(bank1, manager.getLocationToBank().get(new Location(null, 0,0,0)));
		Assert.assertEquals(bank1, manager.getLocationToBank().get(new Location(null, 0,0,1)));

		//Create a bank, add 1 input and test if the bank if found
		Bank bank2 = manager.addBank("findFromLocationTestBank2", "Description");
		bank2.setInput(new Location(null, 0,0,2), 1);
		Assert.assertEquals(bank2, manager.getLocationToBank().get(new Location(null, 0,0,2)));

		//Add an input and test if exception thrown
		Assert.assertThrows(ZSortingException.class, () -> {
			bank2.setInput(new Location(null, 0,0,1), 1);
		});
		
		//Remove the bank1 and test if the inputs have been removed from the location map
		manager.deleteBank(bank1.getName());
		Assert.assertEquals(null, manager.getLocationToBank().get(new Location(null, 0,0,0)));
		Assert.assertEquals(null, manager.getLocationToBank().get(new Location(null, 0,0,1)));
		
		//Remove the bank2 and test if the inputs have been removed from the location map
		manager.deleteBank(bank2.getName());
		Assert.assertEquals(null, manager.getLocationToBank().get(new Location(null, 0,0,2)));
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
}
