package fr.zcraft.zsorter;

import org.bukkit.inventory.Inventory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import fr.zcraft.zsorter.ZSorter;
import fr.zcraft.zsorter.model.SorterManager;

/**
 * Super class of unit test.<br>
 * Provides useful methods for tests.
 * @author Lucas
 *
 */
public class ZSorterTest {
	protected ServerMock server;
	protected ZSorter plugin;
	
	protected SorterManager manager;
	protected Inventory inventory0, inventory1, inventory2, inventory3;
	
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
		manager = new SorterManager();
		inventory0 = server.createInventory(null, 9);
		inventory1 = server.createInventory(null, 9);
		inventory2 = server.createInventory(null, 9);
		inventory3 = server.createInventory(null, 9);
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
