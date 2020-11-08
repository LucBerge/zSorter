package fr.zcraft.zsorting;

import org.bukkit.inventory.Inventory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import fr.zcraft.zsorting.ZSorting;
import fr.zcraft.zsorting.model.BankManager;

/**
 * Super class of unit test.<br>
 * Provides useful methods for tests.
 * @author Lucas
 *
 */
public class ZSortingTest {
	protected ServerMock server;
	protected ZSorting plugin;
	
	protected BankManager manager;
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
	    plugin = (ZSorting) MockBukkit.load(ZSorting.class);
		manager = new BankManager();
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
	    MockBukkit.unload();
	}
}
