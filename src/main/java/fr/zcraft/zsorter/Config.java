package fr.zcraft.zsorter;

import java.util.Locale;

import fr.zcraft.quartzlib.components.configuration.Configuration;
import fr.zcraft.quartzlib.components.configuration.ConfigurationItem;
import static fr.zcraft.quartzlib.components.configuration.ConfigurationItem.item;

/**
 * This class provides the configuration constants for the plugin.
 * @author Lucas
 */
public class Config extends Configuration
{
	/**
	 * The default language of the plugin.
	 */
	static public final ConfigurationItem<Locale> LANGUAGE = item("language", Locale.class);
	
    /**
     * The maximum number of sorter per map
     */
    static public final ConfigurationItem<Integer> MAX_BANKS = item("max_sorters", 100);
    
    /**
     * The maximum inputs for one sorter.
     */
    static public final ConfigurationItem<Integer> MAX_INPUTS = item("max_inputs", 100);
    
    /**
     * The maximum outputs for one sorter.
     */
    static public final ConfigurationItem<Integer> MAX_OUTPUTS = item("max_ouputs", 10000);
}