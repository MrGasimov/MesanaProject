package com.corvolution.cm2.configuration;

public class ConfigurationSet extends AbstractConfigurationElement
{

	/**This is a subclass of AbstarctConfigurationElement and is used to construct configuration object with a specific parameters
	 * @param configSet
	 * @param name
	 * @param description
	 * @param compatibleConfigurationVersion
	 */
	public ConfigurationSet(byte configSet, String name, String description, String compatibleConfigurationVersion)
	{
		super(configSet, name, description, compatibleConfigurationVersion);
		
	}	

}
