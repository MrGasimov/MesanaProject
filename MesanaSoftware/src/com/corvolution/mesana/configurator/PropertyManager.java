package com.corvolution.mesana.configurator;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**PropertyManager - provides managing ability for properties of Mesana Software (e.g MesanaConfigurator)
 * @author Suleyman Gasimov
 */
public class PropertyManager
{
	private static Properties prop;
	private static PropertyManager instance;

	/**The constructor for creating property instance and loading properties from file.
	 */
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

	/**
	 * This method returns string value of a given property.
	 *
	 * @param property the property
	 * @return String - value of property
	 */
	public String getProperty(String property)
	{
		return prop.getProperty(property);
	}
	
	/**
	 * This method returns instance of this class.
	 *
	 * @return single instance of PropertyManager
	 */
	public static PropertyManager getInstance()
	{
		if (instance == null)
		{
			instance = new PropertyManager();
		}
		return instance;
	}
	

}
