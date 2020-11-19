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
     * The magic effect cooldown.
     */
    static public final ConfigurationItem<Integer> MAGIC_EFFECT_DURATION = item("magic_effect_duration", 1200);
}