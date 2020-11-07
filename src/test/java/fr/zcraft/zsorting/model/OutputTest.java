package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorting.ZSortingTest;

/**
 * Tests the {@code Output.class} methods.
 * @author Lucas
 */
public class OutputTest  extends ZSortingTest{
	
	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(null, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(inventory0, null);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(inventory0, -45);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(inventory0, -1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(inventory0, 0);
		});

		new Output(inventory0, 1);
	}
	
	/**
	 * Tests if a list of output is order by priority.
	 */
	@Test
	public void sortingTest(){		
		//Create a list of outputs and test if they are ordered by priority
		Output o0 = new Output(inventory0, 1);
		Output o1 = new Output(inventory1, 2);
		Output o2 = new Output(inventory2, 45);
		Output o3 = new Output(inventory3, 72);
		List<Output> list = Arrays.asList(o3,o1,o2,o0);
		Collections.sort(list);
		Assert.assertEquals(Arrays.asList(o0,o1,o2,o3), list);
	}
	
	/**
	 * Test if a list of materials is ordered by name when set to an output
	 */
	@Test
	public void materialsTest() {
		//Create an output, add materials and test if they are order by name
		Output output = new Output(inventory0, 1);
		output.setMaterials(Arrays.asList(Material.CARROT, Material.PAPER, Material.ICE));
		Assert.assertEquals(Arrays.asList(Material.CARROT, Material.ICE, Material.PAPER), output.getMaterials());
	}
	
	/**
	 * Checks if the isOverflow method returns the expected value.
	 */
	@Test
	public void isOverflowTest() {		
		//Create an output and test if it is an overflow
		Output output = new Output(inventory0, 1);
		Assert.assertEquals(true, output.isOverflow());

		//Set materials and test if it is not an overflow
		output.setMaterials(Arrays.asList(Material.ICE, Material.PAPER));
		Assert.assertEquals(false, output.isOverflow());

		//Set materials and test if it is an overflow
		output.setMaterials(new ArrayList<Material>());
		Assert.assertEquals(true, output.isOverflow());
		
	}
}
