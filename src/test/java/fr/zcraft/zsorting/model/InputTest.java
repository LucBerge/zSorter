package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@code Input.class} methods.
 * @author Lucas
 */
public class InputTest {

	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){
		Bank bank = new Bank("illegalTestBank", "");
		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(null, new Location(null, 0, 0, 0), 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, null, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, new Location(null, 0, 0, 0), null);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, new Location(null, 0, 0, 0), -45);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, new Location(null, 0, 0, 0), -1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, new Location(null, 0, 0, 0), 0);
		});

		new Input(bank, new Location(null, 0, 0, 0), 1);
		new Input(bank, new Location(null, 0, 0, 0), 5);
	}
	
	/**
	 * Tests if a list of input is order by priority.
	 */
	@Test
	public void sortingTest(){
		Bank bank = new Bank("sortingTestBank", "");
		
		Input i1 = new Input(bank, new Location(null, 0, 0, 0), 1);
		Input i2 = new Input(bank, new Location(null, 0, 0, 0), 2);
		Input i3 = new Input(bank, new Location(null, 0, 0, 0), 45);
		Input i4 = new Input(bank, new Location(null, 0, 0, 0), 72);
		
		List<Input> unsortedList = new ArrayList<Input>();
		unsortedList.add(i4);
		unsortedList.add(i2);
		unsortedList.add(i1);
		unsortedList.add(i3);
		
		List<Input> sortedList = new ArrayList<Input>();
		sortedList.add(i1);
		sortedList.add(i2);
		sortedList.add(i3);
		sortedList.add(i4);
		
		Collections.sort(unsortedList);
		
		Assert.assertEquals(sortedList, unsortedList);
	}
}
