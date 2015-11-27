package com.corvolution.mesana.configurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.corvolution.cm2.connection.ConnectionManager;

public class PropertyManager
{
	private static Properties prop;
	private static PropertyManager instance;

	public PropertyManager()
	{
		try
		{
			prop = new Properties();
			prop.load(new FileInputStream(Constants.PROPERTIES_FILE));
		}
		catch (IOException e)
		{
			System.out.println("Cannot read " + Constants.PROPERTIES_FILE + ".");
			e.printStackTrace();
		}
	}

	public String getProperty(String property)
	{
		return prop.getProperty(property);
	}
	public static PropertyManager getInstance()
	{
		if (instance == null)
		{
			instance = new PropertyManager();
		}
		return instance;
	}
	

}
