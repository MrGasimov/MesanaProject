package com.corvolution.cm2.fileadapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.corvolution.cm2.Constants;

public class CustomFile extends TextFileAdapter
{
	public static final String LINK_ID = "LinkId";


	/**
	 * Connects a CM2 info file for reading the info properties defined as static final constants.
	 * @param sensorPath Path to sensor
	 */
	
	public CustomFile(String sensorPath)
	{
		super(sensorPath + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
		
	}
	
	public void writeCustomData(String data, String sensorPath){
		File destination = new File(sensorPath + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
		
		try
		{
			PrintWriter out = new PrintWriter(destination);
			out.println(data);
			out.close();
		}
		catch (FileNotFoundException e)
		{
			System.out.println("The requesting file was not found!");
			e.printStackTrace();
		}
		
	}

}
