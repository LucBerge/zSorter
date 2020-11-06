package fr.zcraft.zsorting.model;

import java.util.ArrayList;
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
		Bank bank = new Bank(new BankManager(), "illegalTestBank", "");
		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(null, inventory0, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, null, 1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, inventory0, null);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, inventory0, -45);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, inventory0, -1);
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Output(bank, inventory0, 0);
		});

		new Output(bank, inventory0, 1);
		new Output(bank, inventory0, 5);
	}
	
	/**
	 * Tests if a list of output is order by priority.
	 */
	@Test
	public void sortingTest(){
		Bank bank = new Bank(new BankManager(), "sortingTestBank", "");
		
		Output o1 = new Output(bank, inventory0, 1);
		Output o2 = new Output(bank, inventory0, 2);
		Output o3 = new Output(bank, inventory0, 45);
		Output o4 = new Output(bank, inventory0, 72);
		
		List<Output> unsortedList = new ArrayList<Output>();
		unsortedList.add(o4);
		unsortedList.add(o2);
		unsortedList.add(o1);
		unsortedList.add(o3);
		
		List<Output> sortedList = new ArrayList<Output>();
		sortedList.add(o1);
		sortedList.add(o2);
		sortedList.add(o3);
		sortedList.add(o4);
		
		Collections.sort(unsortedList);
		
		Assert.assertEquals(sortedList, unsortedList);
	}
	
	/**
	 * Test if a list of materials is ordered by name when set to an output
	 */
	@Test
	public void materialsTest() {
		Bank bank = new Bank(new BankManager(), "materialsTestBank", "");

		Output output = new Output(bank, inventory0, 1);
		
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
		Bank bank = new Bank(new BankManager(), "isOverflowTestBank", "");
		
		Output output = new Output(bank, inventory0, 1);
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
