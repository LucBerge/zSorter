package fr.zcraft.zsorting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.event.Listener;

import fr.zcraft.zlib.components.commands.Commands;
import fr.zcraft.zlib.components.i18n.I18n;
import fr.zcraft.zlib.core.ZPlugin;
import fr.zcraft.zsorting.commands.CreateBankCommand;
import fr.zcraft.zsorting.commands.InfoBankCommand;
import fr.zcraft.zsorting.commands.ListCommand;
import fr.zcraft.zsorting.commands.RemoveBankCommand;
import fr.zcraft.zsorting.commands.RemoveInputBankCommand;
import fr.zcraft.zsorting.commands.RemoveOutputBankCommand;
import fr.zcraft.zsorting.commands.SetInputBankCommand;
import fr.zcraft.zsorting.commands.SetOutputBankCommand;
import fr.zcraft.zsorting.commands.ToggleBankCommand;
import fr.zcraft.zsorting.events.HolderBreakEvent;
import fr.zcraft.zsorting.events.ItemMovedEvent;
import fr.zcraft.zsorting.events.SortingEvent;
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

	  /************/
	 /** ON/OFF **/
	/************/
	
	@Override
    public void onEnable() {
		instance = this;
		
		saveDefaultConfig();										//Charge config.yml
        loadComponents(Commands.class, Config.class, I18n.class);	//Charge les classes suivantes
        
        I18n.setPrimaryLocale(Config.LANGUAGE.get());				//Definit la langue utilisée
        this.getServer().getPluginManager().registerEvents(new HolderBreakEvent(), this);
        this.getServer().getPluginManager().registerEvents(new ItemMovedEvent(), this);
        
        Commands.register("zsorting",
        		ListCommand.class,
        		CreateBankCommand.class,
        		RemoveBankCommand.class,
        		InfoBankCommand.class,
        		ToggleBankCommand.class,
        		SetInputBankCommand.class,
        		SetOutputBankCommand.class,
        		RemoveInputBankCommand.class,
        		RemoveOutputBankCommand.class
        );
        
		Load();	//Charge les banques a partir d'un fichier
		
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new SortingEvent(), SortingEvent.DELAY, SortingEvent.PERIOD);
    }
	
    @Override
    public void onDisable() {
    	Save();	//Sauvegarde les banque sur un fichier
    }

	  /*************/
	 /** SORTING **/
	/*************/
   
    public void sortItems() {
    	
    }
    
	  /**************/
	 /** METHODES **/
	/**************/
	
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
