package com.corvolution.cm2.fileadapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.corvolution.cm2.Constants;
import com.corvolution.cm2.Logger;

public class CustomFile extends TextFileAdapter
{
	public static final String LINK_ID = "LinkId";

	public CustomFile(String sensorPath)
	{
		super(sensorPath + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
	}

	public void writeCustomData(String data, String sensorPath)
	{
		File destination = new File(sensorPath + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
		try
		{
			PrintWriter out = new PrintWriter(destination);
			out.println(data);
			out.close();
		}
		catch (FileNotFoundException e)
		{	
			Logger.printLog("The requesting file was not found!");
			e.printStackTrace();
		}

	}

}
