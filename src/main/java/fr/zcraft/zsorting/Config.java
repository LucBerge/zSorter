package fr.zcraft.zsorting;

import java.util.Locale;

import fr.zcraft.zlib.components.configuration.Configuration;
import fr.zcraft.zlib.components.configuration.ConfigurationItem;
import static fr.zcraft.zlib.components.configuration.ConfigurationItem.item;

/**
 * This class provides the configuration constants for the plugin.
 * @author Lucas
 */
public class Config extends Configuration
{
	/**
	 * The default language of the plugin.
	 */
	static public final ConfigurationItem<Locale> LANGUAGE = item("language", Locale.FRENCH);
	
    /**
     * The maximum number of bank per map
     */
    static public final ConfigurationItem<Integer> MAX_BANKS = item("max_banks", 100);
    
    /**
     * The maximum inputs for one bank.
     */
    static public final ConfigurationItem<Integer> MAX_INPUTS = item("max_inputs", 100);
    
    /**
     * The maximum outputs for one bank.
     */
    static public final ConfigurationItem<Integer> MAX_OUTPUTS = item("max_ouputs", 10000);
}