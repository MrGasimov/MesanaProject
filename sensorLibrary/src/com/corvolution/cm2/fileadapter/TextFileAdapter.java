package com.corvolution.cm2.fileadapter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.corvolution.cm2.Logger;

/**
 * This class reads
 * 
 * @author kirst
 *
 */
public class TextFileAdapter
{
	private Properties prop;

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

	public String getProperty(String property)
	{
		return prop.getProperty(property);
	}
}
