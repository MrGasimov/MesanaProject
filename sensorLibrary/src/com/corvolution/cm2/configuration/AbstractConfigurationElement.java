package com.corvolution.cm2.configuration;

/**This  abstract class represents parent of configurationSet and StartMode classes. All subclasses instances are created from this class. Instance of this class is forbidden. 
 * @author Suleyman Gasimov
 *
 */
public abstract class AbstractConfigurationElement
{
	private byte encodedByte;
	private String name;
	private String description;
	private String compatibleConfigurationVersion;

	
	/**Constructor constructs object with given parameters.
	 * @param configSet
	 * @param name
	 * @param description
	 * @param compatibleConfigurationVersion
	 */
	public AbstractConfigurationElement(byte configSet, String name, String description,
			String compatibleConfigurationVersion)
	{
		this.encodedByte = configSet;
		this.name = name;
		this.description = description;
		this.compatibleConfigurationVersion = compatibleConfigurationVersion;

	}

	/**This method returns name of configuration parameter
	 * @return String 
	 */
	public String getName()
	{
		return name;
	}

	/**This method returns description of configuration parameter
	 * @return String
	 */
	public String getDescription()
	{
		return description;
	}

	/**This method returns compatibility version of sensor configuration 
	 * @return String 
	 */
	public String getCompatibleConfigurationVersion()
	{
		return compatibleConfigurationVersion;
	}

	/**This method returns encoded byte of configurationSet 
	 * @return byte
	 */
	public byte getEncodedByte()
	{
		return encodedByte;
	}

	/**This method returns true if configuration version Compatible with sensor 
	 * @param configurationVersion
	 * @return boolean
	 */
	public boolean isCompatibleWithSensor(String configurationVersion)
	{	
		
		boolean compatible = false;
		int configVerMajor = Integer.parseInt(configurationVersion.substring(0, configurationVersion.indexOf('.')));
		int configVerMinor = Integer.parseInt(configurationVersion.substring(configurationVersion.indexOf('.')+1));
		
		int compatibleVerMajor = Integer.parseInt(compatibleConfigurationVersion.substring(0,configurationVersion.indexOf('.')));
		int compatibleVerMinor = Integer.parseInt(compatibleConfigurationVersion.substring(configurationVersion.indexOf('.') + 1));
		
		if (configVerMajor != compatibleVerMajor)
		{
			compatible = false;
		}
		else if (configVerMajor == compatibleVerMajor && configVerMinor == compatibleVerMinor )
		{
			compatible = true ;
		}
		else if(configVerMajor == compatibleVerMajor && configVerMinor < compatibleVerMinor)
		{
			compatible = false;
		} else if(configVerMajor == compatibleVerMajor && configVerMinor > compatibleVerMinor)
		{
			compatible = true ;
		}

		return compatible;
		// configurationVersion compatibleConfigurationVersion return
		// 1.0 1.0 true
		// 1.1 1.0 true
		// 1.0 1.1 false
		// 2.0 1.1 false
		// major version difference is always false
	}
}
