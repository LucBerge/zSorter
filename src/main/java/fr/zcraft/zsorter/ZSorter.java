package fr.zcraft.zsorter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import fr.zcraft.zlib.components.commands.Commands;
import fr.zcraft.zlib.components.i18n.I18n;
import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zlib.tools.PluginLogger;
import fr.zcraft.zsorter.commands.CreateCommand;
import fr.zcraft.zsorter.commands.DeleteCommand;
import fr.zcraft.zsorter.commands.InfoCommand;
import fr.zcraft.zsorter.commands.ListCommand;
import fr.zcraft.zsorter.commands.RemoveInputCommand;
import fr.zcraft.zsorter.commands.RemoveOutputCommand;
import fr.zcraft.zsorter.commands.SetInputCommand;
import fr.zcraft.zsorter.commands.SetOutputCommand;
import fr.zcraft.zsorter.commands.SpeedCommand;
import fr.zcraft.zsorter.commands.ToggleCommand;
import fr.zcraft.zsorter.commands.UpdateCommand;
import fr.zcraft.zsorter.events.HolderBreakEvent;
import fr.zcraft.zsorter.events.InventoryEvent;
import fr.zcraft.zsorter.events.ItemMoveEvent;
import fr.zcraft.zsorter.model.SorterManager;
import fr.zcraft.zsorter.tasks.SortTask;

/**
 * The ZSorter main class.
 * @author Lucas
 */
public final class ZSorter extends ZPlugin implements Listener{

	/**
	 * Instance of the plugin.
	 */
	public static ZSorter instance;
	
	/**
	 * Returns the instance of the plugin.
	 * @return The instance of the plugin.
	 */
	public static ZSorter getInstance() {
		return instance;
	}

	/**
	 * Constructor of the plugin.
	 * This constructor is only needed for unit tests purposes with MockBukkit.
	 */
	public ZSorter()
    {
        super();
    }

    /**
	 * Constructor of the plugin.
	 * This constructor is only needed for unit tests purposes with MockBukkit.
     * @param loader - The plugin loader.
     * @param description - The plugin description file.
     * @param dataFolder - The plugin folder.
     * @param file - The plugin jar file.
     */
    protected ZSorter(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }
	
	private final String dataPath = this.getDataFolder() + "/zsorter.dat";
	
	private SorterManager sorterManager;
	
	private boolean enable = true;
	
	/**
	 * Returns the data file of the plugin.
	 * @return The data file of the plugin.
	 */
	public String getDataPath() {
		return dataPath;
	}
	
	/**
	 * Returns the sorter manager object.
	 * @return The sorter manager object.
	 */
	public SorterManager getSorterManager() {
		return sorterManager;
	}
	
	/**
	 * Checks whether the plugin is enable or not.
	 * @return {@code true} if the plugin is enable, {@code false} otherwise.
	 */
	public boolean isEnable() {
		return enable;
	}
	
	@Override
    public void onEnable() {
		instance = this;
		
		saveDefaultConfig();
        loadComponents(Commands.class, Config.class, I18n.class);
        
        I18n.setPrimaryLocale(Config.LANGUAGE.get());
        this.getServer().getPluginManager().registerEvents(new HolderBreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new InventoryEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ItemMoveEvent(), this);
        
        Commands.register("sorter",
        		ListCommand.class,
        		CreateCommand.class,
        		UpdateCommand.class,
        		SpeedCommand.class,
        		DeleteCommand.class,
        		InfoCommand.class,
        		ToggleCommand.class,
        		SetInputCommand.class,
        		SetOutputCommand.class,
        		RemoveInputCommand.class,
        		RemoveOutputCommand.class
        );
        
		if(load()) {
			SortTask.getInstance().start();
		}
    }
	
    @Override
    public void onDisable() {
    	save();
    }
	
	/**
	 * Save the sorter manager to a file. Doesn't do anything if the plugin is disabled.
	 */
	private void save() {
		if(enable) {
			try {			
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dataPath)));
				oos.writeObject(sorterManager);
				oos.close();
			}catch (Exception e) {
				PluginLogger.error("Couldn't save the sorter manager instance on the file %s.", e, dataPath);
			}
		}
	}
	
	/**
	 * Load the SorterManager from a file.
	 * @return {@code true} if the data is successfully loaded, {@code false} otherwise.
	 */
	private boolean load() {
		File dataFile = new File(dataPath);
		if(dataFile.exists()) {
			try {			
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile));
				sorterManager = (SorterManager) ois.readObject();
				ois.close();
			}catch(IOException | ClassNotFoundException e) {
				PluginLogger.warning("Cannot read the content of the file {0}. The file might be corrupted.", dataPath);
				PluginLogger.warning("To prevent all lose of data, the plugin is temporary disabled.");
				PluginLogger.warning("To enable it, you can either :");
				PluginLogger.warning("- Fix the file (and keep your data)");
				PluginLogger.warning("- Remove the file (and loose your data)");
				enable = false;
				return false;
			}
		}
		else {
			sorterManager = new SorterManager();
		}
		return true;
	}	
}
