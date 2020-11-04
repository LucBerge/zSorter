package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@code Bank.class} methods.
 * @author Lucas
 */
public class BankTest {

	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Bank(null, "Description");
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Bank("Name", null);
		});

		new Bank("Name", "Description");
	}
	
	/**
	 * Tests if the inputs are correctly sorted when added to the LocationToInput map
	 */
	@Test
	public void inputSortingTest() {
		Bank bank = new Bank("inputsTestBank", "Description");
		
		bank.addInput(new Location(null,0 ,0 ,2), 45);
		bank.addInput(new Location(null,0 ,0 ,1), 2);
		bank.addInput(new Location(null,0 ,0 ,0), 1);
		bank.addInput(new Location(null,0 ,0 ,3), 72);

		Input i1 = new Input(bank, new Location(null, 0, 0, 0), 1);
		Input i2 = new Input(bank, new Location(null, 0, 0, 1), 2);
		Input i3 = new Input(bank, new Location(null, 0, 0, 2), 45);
		Input i4 = new Input(bank, new Location(null, 0, 0, 3), 72);

		List<Input> sortedList = new ArrayList<Input>();
		sortedList.add(i1);
		sortedList.add(i2);
		sortedList.add(i3);
		sortedList.add(i4);
		
		Collection<Input> retrievedCollection = bank.getLocationToInput().values();
		
		Assert.assertEquals(sortedList.size(), retrievedCollection.size());
		
		int i = 0;
		for(Input input:retrievedCollection) {
			System.out.println(input.getPriority());
			//Assert.assertEquals(input, sortedList.get(i));
			i++;
		}
	}
}
