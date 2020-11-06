package fr.zcraft.zsorting.model;

import java.util.Arrays;
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
	}
	
	/**
	 * Tests if a list of input is order by priority.
	 */
	@Test
	public void sortingTest(){
		
		//Create a list of inputs and test if they are ordered by priority
		Bank bank = new Bank(new BankManager(), "sortingTestBank", "");
		Input i0 = new Input(bank, inventory0, 1);
		Input i1 = new Input(bank, inventory1, 2);
		Input i2 = new Input(bank, inventory2, 45);
		Input i3 = new Input(bank, inventory3, 72);
		List<Input> list = Arrays.asList(i3,i1,i2,i0);
		Collections.sort(list);
		Assert.assertEquals(Arrays.asList(i0,i1,i2,i3), list);
	}
}
