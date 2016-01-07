package com.corvolution.cm2.fileadapter;
import java.io.File;
import com.corvolution.cm2.Constants;

/**This class represents status file of sensor. When sensor connected if such file exists  instance of this class is created and initialized. 
 * @author Suleyman Gasimov
 *
 */
public class StatusFile extends TextFileAdapter
{	
	public static final String BATTERY_VOLTAGE = "BatteryVoltage";
	public static final String CURRENT_STATE = "CurrentState";
	public static final String SYSTEM_TIME = "SystemTime";
	
	/**Constructs this class object with a path to status file of sensor
	 * @param path to currently connected sensor
	 */
	public StatusFile(String path)
	{
		super(path + ":" + File.separator + Constants.CM2_FEEDBACK_FILE);
	}

}
