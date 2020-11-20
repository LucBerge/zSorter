package fr.zcraft.zsorter.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.junit.Assert;
import org.junit.Test;

import fr.zcraft.zsorter.ZSorterException;
import fr.zcraft.zsorter.ZSorterTest;
import fr.zcraft.zsorter.model.Input;
import fr.zcraft.zsorter.model.Output;
import fr.zcraft.zsorter.model.Sorter;

/**
 * Tests the {@code Sorter.class} methods.
 * @author Lucas
 */
public class SorterTest extends ZSorterTest {
	
	/**
	 * Tests if wrong constructor arguments are throwing IllegalArgumentException.
	 */
	@Test
	public void illegalTest(){		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Sorter(null, "Description");
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Sorter("Name", null);
		});

		new Sorter("Name", "Description");
	}

	/**
	 * Tests if the inputs are correctly sorted when added to input list
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	@Test
	public void inputSortTest() throws ZSorterException {
		
		//Create a sorter, add input inventories and test if the inputs are empty
		Sorter sorter = new Sorter("inputsTestSorter", "Description");
		Input i2 = sorter.setInput(inventory2, 45);
		Input i1 = sorter.setInput(inventory1, 2);
		Input i0 = sorter.setInput(inventory0, 1);
		Input i3 = sorter.setInput(inventory3, 72);
		
		//Test if the inputs are sorted
		Assert.assertEquals(Arrays.asList(i0,i1,i2,i3), sorter.getInputs());
		
		//Remove one input and test if the inputs are sorted
		sorter.removeInput(inventory1);
		Assert.assertEquals(Arrays.asList(i0,i2,i3), sorter.getInputs());
	}
	
	/**
	 * Tests if the outputs are correctly sorted when added to the InventoryToOutput map
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	@Test
	public void outputSortTest() throws ZSorterException {
		
		//Create a sorter, add output inventories and test if the overflows are sorted
		Sorter sorter = new Sorter("outputsTestSorter", "Description");
		Output o2 = sorter.setOutput(inventory2, 45, new ArrayList<Material>());
		Output o1 = sorter.setOutput(inventory1, 2, new ArrayList<Material>());
		Output o0 = sorter.setOutput(inventory0, 1, new ArrayList<Material>());
		Output o3 = sorter.setOutput(inventory3, 72, new ArrayList<Material>());
		Assert.assertEquals(Arrays.asList(o0, o1, o2, o3), sorter.getOverflows());
		
		//Remove one output and test if the overflows are sorted
		sorter.removeOutput(inventory1);
		Assert.assertEquals(Arrays.asList(o0, o2, o3), sorter.getOverflows());
	}
	
	/**
	 * Tests whether the hasOverflow method returns the expected value.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	@Test
	public void hasOverflowTest() throws ZSorterException {
		
		//Create a sorter, add an output, commit and test if the sorter has an overflow
		Sorter sorter = new Sorter("hasOverflowTestSorter", "Description");
		sorter.setOutput(inventory0, 1, new ArrayList<Material>());
		Assert.assertEquals(true, sorter.hasOverflow());
		
		//Add an output, commit and test if the sorter has an overflow
		sorter.setOutput(inventory1, 4, Arrays.asList(Material.AIR));
		sorter.commit();
		Assert.assertEquals(true, sorter.hasOverflow());
		
		//Remove an output, commit and test if the sorter has no overflow
		sorter.removeOutput(inventory0);
		sorter.commit();
		Assert.assertEquals(false, sorter.hasOverflow());
	}
	
	/**
	 * Tests if the findOutputs method returns the expected values.
	 * @throws ZSorterException if a ZSorter exception occurs.
	 */
	@Test
	public void findOutputsTest() throws ZSorterException {
		//Create a sorter and test if the sorter has no outputs for IRON_BLOCK
		Sorter sorter = new Sorter("findOutputsTestSorter", "Description");
		Assert.assertEquals(new ArrayList<Output>(), sorter.findOutputs(Material.IRON_BLOCK));
		
		//Test when the sorter has one output of cobblestone
		Output coobleStoneOutput1 = sorter.setOutput(inventory0, 2, Arrays.asList(Material.COBBLESTONE));
		Assert.assertEquals(new ArrayList<Output>(), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput1), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has one output of cobblestone and one of iron_block
		Output ironBlockOutput = sorter.setOutput(inventory1, 1, Arrays.asList(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(ironBlockOutput), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput1), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has two output of cobblestone and one of iron_block
		Output coobleStoneOutput2 = sorter.setOutput(inventory2, 1, Arrays.asList(Material.COBBLESTONE));
		Assert.assertEquals(Arrays.asList(ironBlockOutput), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has two output of cobblestone, one of iron_block and one overflow
		Output overflow = sorter.setOutput(inventory3, 10, Arrays.asList());
		Assert.assertEquals(Arrays.asList(ironBlockOutput, overflow), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1, overflow), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has two output of cobblestone and one overflow
		sorter.removeOutput(ironBlockOutput.getInventory());
		Assert.assertEquals(Arrays.asList(overflow), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1, overflow), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has one output of cobblestone and one overflow
		sorter.removeOutput(coobleStoneOutput1.getInventory());
		Assert.assertEquals(Arrays.asList(overflow), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, overflow), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has one overflow
		sorter.removeOutput(coobleStoneOutput2.getInventory());
		Assert.assertEquals(Arrays.asList(overflow), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(overflow), sorter.findOutputs(Material.COBBLESTONE));
		
		//Test when the sorter has not output
		sorter.removeOutput(overflow.getInventory());
		Assert.assertEquals(Arrays.asList(), sorter.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(), sorter.findOutputs(Material.COBBLESTONE));
	}
}
