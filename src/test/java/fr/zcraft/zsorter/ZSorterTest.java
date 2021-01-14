package fr.zcraft.zsorter;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.InventoryHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import be.seeseemelk.mockbukkit.Coordinate;
import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.WorldMock;
import be.seeseemelk.mockbukkit.block.BlockMock;

/**
 * Super class of unit test.<br>
 * Provides useful methods for tests.
 * @author Lucas
 *
 */
public class ZSorterTest {
	protected ServerMock server;
	protected ZSorter plugin;
	
	protected InventoryHolder holder0, holder1, holder2, holder3;
	
	/**
	 * Load the manager and the inventories.
	 */
	@BeforeClass
	public static void load() {
	}
	
	/**
	 * Mock the server and the plugin.
	 */
	@Before
	public void setUp()
	{
	    server = MockBukkit.mock();
	    plugin = (ZSorter) MockBukkit.load(ZSorter.class);
		WorldMock world = server.addSimpleWorld("world");
	    
		holder0 = createInventoryHolderAtCoordinate(world, new Coordinate(0,0,0));
		holder1 = createInventoryHolderAtCoordinate(world, new Coordinate(0,0,2));
		holder2 = createInventoryHolderAtCoordinate(world, new Coordinate(0,0,4));
		holder3 = createInventoryHolderAtCoordinate(world, new Coordinate(0,0,6));
	}
	
	private InventoryHolder createInventoryHolderAtCoordinate(WorldMock world, Coordinate coordinate) {
		Location location = new Location(world, coordinate.x, coordinate.y, coordinate.z);
		BlockMock block = new BlockMock(Material.CHEST, location);
		return (InventoryHolder) block.getState();
	}

	/**
	 * Unmock the server and the plugin.
	 */
	@After
	public void tearDown()
	{
	    MockBukkit.unmock();
	}
}
