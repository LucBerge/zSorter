package fr.zcraft.zsorter.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorter.ZSorterTest;
import fr.zcraft.zsorter.model.Input;

/**
 * Tests the {@code Input.class} methods.
 * @author Lucas
 */
public class InputTest extends ZSorterTest{

	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(null, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(holder0, null);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(holder0, -45);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(holder0, -1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Input(holder0, 0);
		});

		new Input(holder0, 1);
	}
	
	/**
	 * Tests if a list of input is order by priority.
	 */
	@Test
	public void sortTest(){
		
		//Create a list of inputs and test if they are ordered by priority
		Input i0 = new Input(holder0, 1);
		Input i1 = new Input(holder1, 2);
		Input i2 = new Input(holder2, 45);
		Input i3 = new Input(holder3, 72);
		List<Input> list = Arrays.asList(i3,i1,i2,i0);
		Collections.sort(list);
		Assert.assertEquals(Arrays.asList(i0,i1,i2,i3), list);
	}
}
