package com.corvolution.cm2.fileadapter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import com.corvolution.cm2.Logger;

/**
 * This class represents adapter for text files.Classes extending this class must call parent constructor with a path to text file for creating their instances.
 * 
 * @author Kirst Malte
 *
 */
public class TextFileAdapter
{
	private Properties prop;

	/**Creates instance of this class for a given File. 
	 * @param path to text file
	 */
	public TextFileAdapter(String path)
	{
		try
		{
			prop = new Properties();
			prop.load(new FileInputStream(path));
		}
		catch (FileNotFoundException e)
		{
			Logger.printLog("File " + path + " does not exist.");
		}
		catch (IOException e)
		{
			Logger.printLog("Cannot read " + path + ".");
		}
	}

	/**This method returns property value from file
	 * @param  property needed from file
	 * @return property value from file
	 */
	public String getProperty(String property)
	{
		return prop.getProperty(property);
	}
}
