package fr.zcraft.zsorting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import fr.zcraft.zlib.components.commands.Commands;
import fr.zcraft.zlib.components.i18n.I18n;
import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zsorting.commands.AddCommand;
import fr.zcraft.zsorting.commands.DeleteCommand;
import fr.zcraft.zsorting.commands.InfoCommand;
import fr.zcraft.zsorting.commands.ListCommand;
import fr.zcraft.zsorting.commands.RemoveInputBankCommand;
import fr.zcraft.zsorting.commands.RemoveOutputBankCommand;
import fr.zcraft.zsorting.commands.SetInputBankCommand;
import fr.zcraft.zsorting.commands.SetOutputBankCommand;
import fr.zcraft.zsorting.commands.ToggleCommand;
import fr.zcraft.zsorting.events.HolderBreakEvent;
import fr.zcraft.zsorting.events.ItemMovedEvent;
import fr.zcraft.zsorting.events.SortingTask;
import fr.zcraft.zsorting.model.BankManager;

/**
 * The ZSorting main class.
 * @author Lucas
 */
public final class ZSorting extends ZPlugin implements Listener{

	/**
	 * Instance of the plugin.
	 */
	public static ZSorting instance;
	
	/**
	 * Returns the instance of the plugin.
	 * @return The instance of the plugin.
	 */
	public static ZSorting getInstance() {
		return instance;
	}

	/**
	 * Constructor of the plugin.
	 * This constructor is only needed for unit tests purposes with MockBukkit.
	 */
	public ZSorting()
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
    protected ZSorting(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file)
    {
        super(loader, description, dataFolder, file);
    }
	
	private final String dataFile = this.getDataFolder() + "/zsorting.dat";
	
	private BankManager bankManager;
	
	/**
	 * Returns the data file of the plugin.
	 * @return The data file of the plugin.
	 */
	public String getDataFile() {
		return dataFile;
	}
	
	/**
	 * Returns the bank manager object.
	 * @return The bank manager object.
	 */
	public BankManager getBankManager() {
		return bankManager;
	}
	
	@Override
    public void onEnable() {
		instance = this;
		
		saveDefaultConfig();
        loadComponents(Commands.class, Config.class, I18n.class);
        
        I18n.setPrimaryLocale(Config.LANGUAGE.get());
        this.getServer().getPluginManager().registerEvents(new HolderBreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ItemMovedEvent(), this);
        
        Commands.register("zsorting",
        		ListCommand.class,
        		AddCommand.class,
        		DeleteCommand.class,
        		InfoCommand.class,
        		ToggleCommand.class,
        		SetInputBankCommand.class,
        		SetOutputBankCommand.class,
        		RemoveInputBankCommand.class,
        		RemoveOutputBankCommand.class
        );
        
		Load();
		SortingTask.getInstance().start();
    }
	
    @Override
    public void onDisable() {
    	Save();
    }
	
	/**
	 * Save the bank manager to a file.
	 */
	private void Save() {		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(dataFile)));
			oos.writeObject(bankManager);
			oos.flush();
			oos.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Load the BankManager from a file
	 */
	private void Load() {		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(dataFile)));
			bankManager = (BankManager) ois.readObject();
			ois.close();
		}catch(Exception e) {
			bankManager = new BankManager();
		}
	}	
}
