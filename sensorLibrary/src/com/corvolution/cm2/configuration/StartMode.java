package com.corvolution.cm2.configuration;

/**This class represents start mode of configuration of sensor.
 * @author Suleyman Gasimov
 *
 */
public class StartMode extends AbstractConfigurationElement
{
	
	/**Custom constructor
	 * @param configSet is configuration set as byte 
	 * @param name is name of config set of configuration as String
	 * @param description, details of config set as String
	 * @param compatibleConfigurationVersion  version for checking compatibility of sensor with specific configuration
	 */
	public StartMode(byte configSet, String name, String description, String compatibleConfigurationVersion)
	{
		super(configSet, name, description, compatibleConfigurationVersion);
		
	}	

}
