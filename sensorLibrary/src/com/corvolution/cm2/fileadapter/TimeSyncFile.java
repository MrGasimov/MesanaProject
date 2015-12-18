package com.corvolution.cm2.fileadapter;
import java.util.Calendar;
import java.util.Date;
import com.corvolution.cm2.SensorNotFoundException;

/**This class represents timeSync file of sensor.
 * @author Suleyman Gasimov
 */
public class TimeSyncFile extends BinaryFileAdapter
{	
	/**Constructs object for connected sensor
	 * @param String absolutePath
	 */
	public TimeSyncFile(String path)
	{
		super(path);
	}
	
	public void writeBinaryFile() throws SensorNotFoundException
	{
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);				 
		
		byte year = (byte) (cal.get(Calendar.YEAR) - 2000); // Year 0 is mapped to year 2000
		byte month = (byte) cal.get(Calendar.MONTH + 1);
		byte day = (byte) cal.get(Calendar.DAY_OF_MONTH);
		byte hour = (byte) cal.get(Calendar.HOUR_OF_DAY);
		byte minute = (byte) cal.get(Calendar.MINUTE);
		byte second = (byte) cal.get(Calendar.SECOND);
		byte[] buffer = {year, month, day, hour, minute, second};
		super.writeBinaryFile(buffer);
		
	}
}
