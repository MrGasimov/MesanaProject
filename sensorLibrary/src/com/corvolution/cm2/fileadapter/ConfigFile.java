package com.corvolution.cm2.fileadapter;

import java.io.File;
import java.util.Calendar;

import com.corvolution.cm2.Constants;
import com.corvolution.cm2.configuration.SensorConfiguration;

public class ConfigFile extends BinaryFileAdapter
{	
	private static int VERSION_MAJOR = 0;
	private static int VERSION_MINOR = 1;
	public 	static int START_MODE =2;
	public 	static int CONFIG_SET = 3;
	private static int BYTE_STARTTIME_DAY = 4;
	private static int BYTE_STARTTIME_HOUR = 5;
	private static int BYTE_STARTTIME_MINUTE = 6;
	private static int BYTE_DURATION_DAY = 7;
	private static int BYTE_DURATION_HOUR = 8;
	private static int BYTE_DURATION_MINUTE = 9;
	public byte[] buffer;
	
	public ConfigFile(SensorConfiguration sensorConfiguration){
		
		Calendar cal = Calendar.getInstance();	
		
		buffer[VERSION_MAJOR] = 0;
		buffer[VERSION_MINOR] = 1;
		buffer[START_MODE] = 2;
		buffer[CONFIG_SET] = 3;
		cal.setTime(sensorConfiguration.getRecordingStartTime());
		buffer[BYTE_STARTTIME_DAY] = (byte) cal.get(Calendar.DAY_OF_MONTH);
		buffer[BYTE_STARTTIME_HOUR] = (byte) cal.get(Calendar.HOUR_OF_DAY);
		buffer[BYTE_STARTTIME_MINUTE] = (byte) cal.get(Calendar.MINUTE);
		buffer[BYTE_DURATION_DAY] = (byte) Math.abs(sensorConfiguration.getDurationMinutes() / 1440);
		buffer[BYTE_DURATION_HOUR] = (byte) Math.abs((sensorConfiguration.getDurationMinutes() % 1440) / 60);
		buffer[BYTE_DURATION_MINUTE] = (byte) (sensorConfiguration.getDurationMinutes() % 60);
		for(int i=10; i<31; i++)
		{
			buffer[i] = 0;
		}
		
		
	}
	
	
	
	

}
