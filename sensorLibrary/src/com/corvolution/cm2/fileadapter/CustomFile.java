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
	 * @param path to currently connected sensor.
	 */
	public CustomFile(String path)
	{
		super(path + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
	}

	/**This method writes String data to custom file of sensor.
	 * @param data to be written to the sensor
	 * @param path to currently connected
	 * @throws SensorNotFoundException if sensor connection failed or disconnected
	 */
	public void writeCustomData(String data, String path) throws SensorNotFoundException
	{
		File destination = new File(path + ":" + File.separator + Constants.CM2_CUSTOM_FILE);
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
