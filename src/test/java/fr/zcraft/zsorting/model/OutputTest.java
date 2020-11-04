package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@code Output.class} methods.
 * @author Lucas
 */
public class OutputTest {

	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){
		Bank bank = new Bank("illegalTestBank", "");
		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(null, new Location(null, 0, 0, 0), 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, null, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, new Location(null, 0, 0, 0), null);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, new Location(null, 0, 0, 0), -45);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, new Location(null, 0, 0, 0), -1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, new Location(null, 0, 0, 0), 0);
		});

		new Output(bank, new Location(null, 0, 0, 0), 1);
		new Output(bank, new Location(null, 0, 0, 0), 5);
	}
	
	/**
	 * Tests if a list of output is order by priority.
	 */
	@Test
	public void sortingTest(){
		Bank bank = new Bank("sortingTestBank", "");
		
		Output i1 = new Output(bank, new Location(null, 0, 0, 0), 1);
		Output i2 = new Output(bank, new Location(null, 0, 0, 0), 2);
		Output i3 = new Output(bank, new Location(null, 0, 0, 0), 45);
		Output i4 = new Output(bank, new Location(null, 0, 0, 0), 72);
		
		List<Output> unsortedList = new ArrayList<Output>();
		unsortedList.add(i4);
		unsortedList.add(i2);
		unsortedList.add(i1);
		unsortedList.add(i3);
		
		List<Output> sortedList = new ArrayList<Output>();
		sortedList.add(i1);
		sortedList.add(i2);
		sortedList.add(i3);
		sortedList.add(i4);
		
		Collections.sort(unsortedList);
		
		Assert.assertEquals(sortedList, unsortedList);
	}
	
	/**
	 * Test if a list of materials is ordered by name when set to an output
	 */
	@Test
	public void materialsTest() {
		Bank bank = new Bank("materialsTestBank", "");

		Output output = new Output(bank, new Location(null, 0, 0, 0), 1);
		
		List<Material> unsortedList = new ArrayList<Material>();
		unsortedList.add(Material.CARROT);
		unsortedList.add(Material.PAPER);
		unsortedList.add(Material.ICE);
		
		List<Material> sortedList = new ArrayList<Material>();
		sortedList.add(Material.CARROT);
		sortedList.add(Material.ICE);
		sortedList.add(Material.PAPER);
		
		output.setMaterials(unsortedList);

		Assert.assertEquals(sortedList, output.getMaterials());
	}
	
	/**
	 * Checks if the isOverflow method returns the expected value.
	 */
	@Test
	public void isOverflowTest() {
		Bank bank = new Bank("isOverflowTestBank", "");
		
		Output output = new Output(bank, new Location(null, 0, 0, 0), 1);
		Assert.assertEquals(true, output.isOverflow());

		List<Material> materials = new ArrayList<Material>();
		materials.add(Material.CARROT);
		
		output.setMaterials(materials);
		Assert.assertEquals(false, output.isOverflow());
		

		materials.add(Material.ICE);
		materials.add(Material.PAPER);
		output.setMaterials(materials);
		Assert.assertEquals(false, output.isOverflow());
		
		output.setMaterials(new ArrayList<Material>());
		Assert.assertEquals(true, output.isOverflow());
		
	}
}
