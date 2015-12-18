package com.corvolution.cm2.fileadapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import com.corvolution.cm2.Constants;
import com.corvolution.cm2.Logger;
import com.corvolution.cm2.SensorNotFoundException;

/**This class represents custom file of sensor. When sensor connected if such file exists  instance of this class is created and initialized. 
 * @author Suleyman Gasimov
 *
 */
public class CustomFile extends TextFileAdapter
{
	public static final String LINK_ID = "LinkId";

	/**Constructs this class object with a path to custom file of sensor
	 * @param sensorPath
	 */
	public CustomFile(String sensorPath)
	{
		super(sensorPath + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
	}

	/**This method writes String data to custom file of sensor.
	 * @param data
	 * @param sensorPath
	 * @throws SensorNotFoundException 
	 */
	public void writeCustomData(String data, String sensorPath) throws SensorNotFoundException
	{
		File destination = new File(sensorPath + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
		PrintWriter out;
		try
		{
			out = new PrintWriter(destination);
			out.println(data);
			out.close();
			Logger.printLog("The requesting file was not found!");
		}
		catch (FileNotFoundException e)
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
			
	}

}
