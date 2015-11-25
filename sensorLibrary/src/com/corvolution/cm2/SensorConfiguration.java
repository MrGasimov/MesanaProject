package com.corvolution.cm2;

import java.time.Duration;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SensorConfiguration
{

	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte[] startMode = {1, 2, 3};
	public byte[] configSet = {1, 2, 3};
	public byte[] recordDuration = {2, 3, 4, 5, 6, 7};
	public byte[] startTime = {1, 3, 1, 1, 1, 5};
	public byte latency = 0;
	public byte[] checksum = {2, 2, 2, 2};
	private HashMap<String, HashMap> configSetMap;
	private HashMap<String, String> parameters;
	private Date recordingStartTime;
	private int durationMinutes;


	public SensorConfiguration()
	{
		configSetMap = new HashMap<>();
		parameters = new HashMap<>();
		// constructing default configset which is number 1--> configset[0]
		ConfigurationSet configSet = new ConfigurationSet();
		// default set for configFile
		configSetMap.put("1", configSet.getSet());
	}

	// set linkId to add paramater file
	public void addParameter(String key, String value)
	{
		this.parameters.put(key, value);
	}

	// set linkId for configuration file
	public void addEncryptedParameter(String key, String value, String password)
	{
		// TODO check for correct password value
		this.parameters.put(key, value);
		
	}
	
	public String getParameter(String key)
	{
		return parameters.get(key);
	}

	public String getEncryptedParameter(String key, String password)
	{
		// decryption and return parameter
		return null;
	}

	// returns map of parameters from additional parameter file in sensor
	public HashMap<String, String> getParameters()
	{
		return parameters;
	}

	public HashMap<String, HashMap> getConfigurationSet()
	{
		return configSetMap;
	}

	public void setConfigurationSet(ConfigurationSet configurationSet, String byteNumber, ArrayList rateList)
	{
		configurationSet = new ConfigurationSet(byteNumber, rateList);
	}

	/**
	 * Set the recording starting point. Only day, hour and minute will be assessed. 
	 * @param date Starting point of the next measurement
	 */
	public void setRecordingStartTime(Date date)
	{
		this.recordingStartTime = date;
	}
	
	/**
	 * Set the recording duration for the next measurement.	
	 * @param duration Duration of the next measurement in minutes
	 */
	public void setRecordingDuration(int duration)
	{
		this.durationMinutes = duration;
	}
	
	public Date getRecordingStartTime()
	{
		return this.recordingStartTime;
	}
	
	public int getDurationMinutes()
	{
		return this.durationMinutes;
	}
}
