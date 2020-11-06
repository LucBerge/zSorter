package fr.zcraft.zsorting.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
			new Bank(null, "Name", "Description");
		});
		
		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Bank(new BankManager(), null, "Description");
		});

		Assert.assertThrows(IllegalArgumentException.class, () -> {
			new Bank(new BankManager(), "Name", null);
		});

		new Bank(new BankManager(), "Name", "Description");
	}

	/**
	 * Tests if the inputs are correctly sorted when added to the LocationToInput map
	 * @throws ZSortingException If the bank already linked to a bank.
	 */
	@Test
	public void inputSortingTest() throws ZSortingException {
		Bank bank = new Bank(new BankManager(), "inputsTestBank", "Description");
		
		bank.setInput(inventory2, 45);
		bank.setInput(inventory1, 2);
		bank.setInput(inventory0, 1);
		bank.setInput(inventory3, 72);

		Input i1 = new Input(bank, inventory0, 1);
		Input i2 = new Input(bank, inventory1, 2);
		Input i3 = new Input(bank, inventory2, 45);
		Input i4 = new Input(bank, inventory3, 72);

		List<Input> sortedList = new ArrayList<Input>();
		sortedList.add(i1);
		sortedList.add(i2);
		sortedList.add(i3);
		sortedList.add(i4);
		
		List<Input> retrievedCollection = bank.getLocationToInput().values().stream().collect(Collectors.toList());
		Assert.assertEquals(sortedList, retrievedCollection);
		
		
		bank.removeInput(inventory1);
		sortedList.remove(i2);

		retrievedCollection = bank.getLocationToInput().values().stream().collect(Collectors.toList());
		Assert.assertEquals(sortedList, retrievedCollection);
	}
	
	/**
	 * Tests if the outputs are correctly sorted when added to the LocationToOutput map
	 */
	@Test
	public void outputSortingTest() {
		Bank bank = new Bank(new BankManager(), "outputsTestBank", "Description");
		
		bank.setOutput(inventory2, 45, new ArrayList<Material>());
		bank.setOutput(inventory1, 2, new ArrayList<Material>());
		bank.setOutput(inventory0, 1, new ArrayList<Material>());
		bank.setOutput(inventory3, 72, new ArrayList<Material>());

		Output o1 = new Output(bank, inventory0, 1);
		Output o2 = new Output(bank, inventory1, 2);
		Output o3 = new Output(bank, inventory2, 45);
		Output o4 = new Output(bank, inventory3, 72);

		List<Output> sortedList = new ArrayList<Output>();
		sortedList.add(o1);
		sortedList.add(o2);
		sortedList.add(o3);
		sortedList.add(o4);
		
		List<Output> retrievedCollection = bank.getLocationToOutput().values().stream().collect(Collectors.toList());
		Assert.assertEquals(sortedList, retrievedCollection);
		
		bank.removeOutput(inventory1);
		sortedList.remove(o2);

		retrievedCollection = bank.getLocationToOutput().values().stream().collect(Collectors.toList());
		Assert.assertEquals(sortedList, retrievedCollection);
	}
	
	/**
	 * Tests whether the hasOverflow method returns the expected value.
	 */
	@Test
	public void hasOverflowTest() {
		Bank bank = new Bank(new BankManager(), "hasOverflowTestBank", "Description");

		bank.setOutput(inventory0, 1, new ArrayList<Material>());
		Assert.assertEquals(true, bank.hasOverflow());
		
		bank.setOutput(inventory1, 4, Arrays.asList(Material.AIR));
		Assert.assertEquals(true, bank.hasOverflow());
		
		bank.removeOutput(inventory0);
		Assert.assertEquals(false, bank.hasOverflow());
	}
	
	/**
	 * Tests if the findOutputs method returns the expected values.
	 */
	@Test
	public void findOutputsTest() {
		Bank bank = new Bank(new BankManager(), "findOutputsTestBank", "Description");
		
		//Test when the bank has no outputs
		Assert.assertEquals(new ArrayList<Output>(), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(0, bank.getMaterialToOutputs().size());
		
		//Test when the bank has one output of cobblestone
		Output coobleStoneOutput1 = bank.setOutput(inventory0, 2, Arrays.asList(Material.COBBLESTONE));
		Assert.assertEquals(new ArrayList<Output>(), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput1), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has one output of cobblestone and one of iron_block
		Output ironBlockOutput = bank.setOutput(inventory1, 1, Arrays.asList(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(ironBlockOutput), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput1), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has two output of cobblestone and one of iron_block
		Output coobleStoneOutput2 = bank.setOutput(inventory2, 1, Arrays.asList(Material.COBBLESTONE));
		Assert.assertEquals(Arrays.asList(ironBlockOutput), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has two output of cobblestone, one of iron_block and one overflow
		Output overflow = bank.setOutput(inventory3, 10, Arrays.asList());
		Assert.assertEquals(Arrays.asList(ironBlockOutput, overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1, overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has two output of cobblestone and one overflow
		bank.removeOutput(ironBlockOutput.getInventory());
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, coobleStoneOutput1, overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has one output of cobblestone and one overflow
		bank.removeOutput(coobleStoneOutput1.getInventory());
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(coobleStoneOutput2, overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has one overflow
		bank.removeOutput(coobleStoneOutput2.getInventory());
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(overflow), bank.findOutputs(Material.COBBLESTONE));
		
		//Test when the bank has not output
		bank.removeOutput(overflow.getInventory());
		Assert.assertEquals(Arrays.asList(), bank.findOutputs(Material.IRON_BLOCK));
		Assert.assertEquals(Arrays.asList(), bank.findOutputs(Material.COBBLESTONE));
	}
}
