package com.corvolution.cm2.fileadapter;
import java.io.File;
import java.util.Calendar;
import java.util.zip.CRC32;

import com.corvolution.cm2.SensorNotFoundException;
import com.corvolution.cm2.configuration.SensorConfiguration;

public class ConfigFile extends BinaryFileAdapter
{
	private static int BYTE_VERSION_MAJOR = 0;
	private static int BYTE_VERSION_MINOR = 1;
	public 	static int BYTE_START_MODE = 2;
	public 	static int BYTE_CONFIG_SET = 3;
	private static int BYTE_STARTTIME_DAY = 4;
	private static int BYTE_STARTTIME_HOUR = 5;
	private static int BYTE_STARTTIME_MINUTE = 6;
	private static int BYTE_DURATION_DAY = 7;
	private static int BYTE_DURATION_HOUR = 8;
	private static int BYTE_DURATION_MINUTE = 9;
	private final static int CONFIG_FILE_LENGTH = 33;
	public 	byte[] buffer = new byte[CONFIG_FILE_LENGTH];

	public ConfigFile(String absolutePath) throws SensorNotFoundException
	{
		super(absolutePath);
		
		super.readBinaryFile();
		
		
	}

	public void writeBinaryFile(SensorConfiguration sensorConfiguration) throws SensorNotFoundException
	{
		// Initialize buffer with zeros
		for (int i = 0; i < CONFIG_FILE_LENGTH; i++)
		{
			buffer[i] = 0;
		}
		
		buffer[BYTE_VERSION_MAJOR] = sensorConfiguration.getConfigurationInterfaceVersionMajor();
		buffer[BYTE_VERSION_MINOR] = sensorConfiguration.getConfigurationInterfaceVersionMinor();
		buffer[BYTE_START_MODE] = sensorConfiguration.getStartMode().getEncodedByte();		
		buffer[BYTE_CONFIG_SET] = sensorConfiguration.getConfigurationSet().getEncodedByte();	
		
		Calendar cal = Calendar.getInstance();
		if(!sensorConfiguration.getRecordingStartTime().equals(null))
		{
			cal.setTime(sensorConfiguration.getRecordingStartTime());
			buffer[BYTE_STARTTIME_DAY] = (byte) cal.get(Calendar.DAY_OF_MONTH);
			buffer[BYTE_STARTTIME_HOUR] = (byte) cal.get(Calendar.HOUR_OF_DAY);
			buffer[BYTE_STARTTIME_MINUTE] = (byte) cal.get(Calendar.MINUTE);
		}
		
		buffer[BYTE_DURATION_DAY] = (byte) Math.abs(sensorConfiguration.getRecordingDuration() / 1440);
		buffer[BYTE_DURATION_HOUR] = (byte) Math.abs((sensorConfiguration.getRecordingDuration() % 1440) / 60);
		buffer[BYTE_DURATION_MINUTE] = (byte) (sensorConfiguration.getRecordingDuration() % 60);
		// TODO add CRC16
		
		CRC32 myCRC = new CRC32();
		myCRC.update(buffer);

		super.writeBinaryFile(buffer);
		
	}

}
