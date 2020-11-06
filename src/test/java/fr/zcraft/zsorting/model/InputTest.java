package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorting.ZSortingTest;

/**
 * Tests the {@code Input.class} methods.
 * @author Lucas
 */
public class InputTest extends ZSortingTest{

	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){
		Bank bank = new Bank(new BankManager(), "illegalTestBank", "");
		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(null, inventory0, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, null, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, inventory0, null);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, inventory0, -45);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, inventory0, -1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(bank, inventory0, 0);
		});

		new Input(bank, inventory0, 1);
		new Input(bank, inventory0, 5);
	}
	
	/**
	 * Tests if a list of input is order by priority.
	 */
	@Test
	public void sortingTest(){
		Bank bank = new Bank(new BankManager(), "sortingTestBank", "");
		
		Input i1 = new Input(bank, inventory0, 1);
		Input i2 = new Input(bank, inventory0, 2);
		Input i3 = new Input(bank, inventory0, 45);
		Input i4 = new Input(bank, inventory0, 72);
		
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
