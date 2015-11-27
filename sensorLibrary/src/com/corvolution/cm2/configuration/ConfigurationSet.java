package com.corvolution.cm2.configuration;

public class ConfigurationSet
{
	private byte configSetByte;
	private String name;
	private String description;
	private String compatibleConfigurationVersion;

	// default constructor
	public ConfigurationSet(byte configSet, String name, String description, String compatibleConfigurationVersion)
	{
		this.configSetByte = configSet;
		this.name = name;
		this.description = description;
		this.compatibleConfigurationVersion = compatibleConfigurationVersion;

	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public String getCompatibleConfigurationVersion()
	{
		return compatibleConfigurationVersion;
	}

	public byte getConfigSetByte()
	{
		return configSetByte;
	}
	
	public  boolean isCompatibleWithSensor(String configurationVersion)
	{	
		boolean compatible;
		if(Double.parseDouble(configurationVersion) >= Double.parseDouble(compatibleConfigurationVersion))
		{
			compatible = true;
		} else if( Double.parseDouble(configurationVersion) < Double.parseDouble(compatibleConfigurationVersion)){
			compatible =false;
		} else {
			compatible =false;
		}
				
		return compatible;			
		//	configurationVersion	compatibleConfigurationVersion	return
		//	1.0						1.0								true
		// 	1.1						1.0								true
		//	1.0						1.1								false
		//  2.0						1.1								false
		// major version difference is always false
	}
}
