package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorting.ZSortingException;
import fr.zcraft.zsorting.ZSortingTest;

/**
 * Tests the {@code Bank.class} methods.
 * @author Lucas
 */
public class BankTest extends ZSortingTest {
	
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
	 * Tests if the inputs are correctly sorted when added to input list
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void inputSortingTest() throws ZSortingException {
		
		//Create a bank, add input inventories and test if the inputs are empty
		Bank bank = new Bank("inputsTestBank", "Description");
		Input i2 = bank.setInput(inventory2, 45);
		Input i1 = bank.setInput(inventory1, 2);
		Input i0 = bank.setInput(inventory0, 1);
		Input i3 = bank.setInput(inventory3, 72);
		Assert.assertEquals(Arrays.asList(), bank.getInputs());
		
		//Commit the bank and test if the inputs are sorted
		bank.commit();
		Assert.assertEquals(Arrays.asList(i0,i1,i2,i3), bank.getInputs());
		
		//Remove one input and test if the inputs are the same
		bank.removeInput(inventory1);
		Assert.assertEquals(Arrays.asList(i0,i1,i2,i3), bank.getInputs());
		
		//Commit the bank and test if the inputs are sorted
		bank.commit();
		Assert.assertEquals(Arrays.asList(i0,i2,i3), bank.getInputs());
	}
	
	/**
	 * Tests if the outputs are correctly sorted when added to the InventoryToOutput map
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void outputSortingTest() throws ZSortingException {
		
		//Create a bank, add output inventories and test if the overflows are empty
		Bank bank = new Bank("outputsTestBank", "Description");
		Output o2 = bank.setOutput(inventory2, 45, new ArrayList<Material>());
		Output o1 = bank.setOutput(inventory1, 2, new ArrayList<Material>());
		Output o0 = bank.setOutput(inventory0, 1, new ArrayList<Material>());
		Output o3 = bank.setOutput(inventory3, 72, new ArrayList<Material>());
		Assert.assertEquals(Arrays.asList(), bank.getOverflows());
		
		//Commit the bank and test if the overflows are sorted
		bank.commit();
		Assert.assertEquals(Arrays.asList(o0, o1, o2, o3), bank.getOverflows());
		
		//Remove one output and test if the overflows are the same
		bank.removeOutput(inventory1);
		Assert.assertEquals(Arrays.asList(o0, o1, o2, o3), bank.getOverflows());
		
		//Commit the bank and test if the overflows are stored
		bank.commit();
		Assert.assertEquals(Arrays.asList(o0, o2, o3), bank.getOverflows());
	}
	
	/**
	 * Tests whether the hasOverflow method returns the expected value.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void hasOverflowTest() throws ZSortingException {
		
		//Create a bank, add an output, commit and test if the bank has an overflow
		Bank bank = new Bank("hasOverflowTestBank", "Description");
		bank.setOutput(inventory0, 1, new ArrayList<Material>());
		bank.commit();
		Assert.assertEquals(true, bank.hasOverflow());
		
		//Add an output, commit and test if the bank has an overflow
		bank.setOutput(inventory1, 4, Arrays.asList(Material.AIR));
		bank.commit();
		Assert.assertEquals(true, bank.hasOverflow());
		
		//Remove an output, commit and test if the bank has no overflow
		bank.removeOutput(inventory0);
		bank.commit();
		Assert.assertEquals(false, bank.hasOverflow());
	}
	
	/**
	 * Tests if the findOutputs method returns the expected values.
	 * @throws ZSortingException if a ZSorting exception occurs.
	 */
	@Test
	public void findOutputsTest() throws ZSortingException {
		//Create a bank and test if the bank has no outputs for IRON_BLOCK
		Bank bank = new Bank("findOutputsTestBank", "Description");
		Assert.assertEquals(new ArrayList<Output>(), bank.findOutputs(Material.IRON_BLOCK));
		
		//Test when the bank has one output of cobblestone
		Output coobleStoneOutput1 = bank.setOutput(inventory0, 2, Arrays.asList(Material.COBBLESTONE));
		bank.commit();
		Assert.assertEquals(new ArrayList<Output>(), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput1), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has one output of cobblestone and one of iron_block
		Output ironBlockOutput = bank.setOutput(inventory1, 1, Arrays.asList(Material.IRON_BLOCK));
		bank.commit();
		Assert.assertEquals(Arrays.asList(ironBlockOutput), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput1), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has two output of cobblestone and one of iron_block
		Output coobleStoneOutput2 = bank.setOutput(inventory2, 1, Arrays.asList(Material.COBBLESTONE));
		bank.commit();
		Assert.assertEquals(Arrays.asList(ironBlockOutput), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has two output of cobblestone, one of iron_block and one overflow
		Output overflow = bank.setOutput(inventory3, 10, Arrays.asList());
		bank.commit();
		Assert.assertEquals(Arrays.asList(ironBlockOutput, overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1, overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has two output of cobblestone and one overflow
		bank.removeOutput(ironBlockOutput.getInventory());
		bank.commit();
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1, overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has one output of cobblestone and one overflow
		bank.removeOutput(coobleStoneOutput1.getInventory());
		bank.commit();
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has one overflow
		bank.removeOutput(coobleStoneOutput2.getInventory());
		bank.commit();
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has not output
		bank.removeOutput(overflow.getInventory());
		bank.commit();
		Assert.assertEquals(Arrays.asList(), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(), bank.findOutputs(Material.COBBLESTONE));
	}
}
