package com.corvolution.cm2.configuration;

public class ConfigurationSet extends AbstractConfigurationElement
{

	/**This is a subclass of AbstarctConfigurationElement and is used to construct configuration object with a specific parameters
	 * @param configSet byte value of configurationSet for sensor configuration
	 * @param name of configuraitonSet
	 * @param description of configurationSet
	 * @param compatibleConfigurationVersion version for checking compatibility of sensor with specific configuration
	 */
	public ConfigurationSet(byte configSet, String name, String description, String compatibleConfigurationVersion)
	{
		super(configSet, name, description, compatibleConfigurationVersion);
		
	}	

}
