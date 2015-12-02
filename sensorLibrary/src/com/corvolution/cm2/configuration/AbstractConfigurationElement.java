package com.corvolution.cm2.configuration;

public abstract class AbstractConfigurationElement
{
	private byte encodedByte;
	private String name;
	private String description;
	private String compatibleConfigurationVersion;

	// default constructor
	public AbstractConfigurationElement(byte configSet, String name, String description,
			String compatibleConfigurationVersion)
	{
		this.encodedByte = configSet;
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

	public byte getEncodedByte()
	{
		return encodedByte;
	}

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
