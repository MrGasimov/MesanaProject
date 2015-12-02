package com.corvolution.cm2.fileadapter;

import java.io.File;

import com.corvolution.cm2.Constants;

public class StatusFile extends TextFileAdapter
{	
	public static final String BATTERY_VOLTAGE = "BatteryVoltage";
	public static final String CURRENT_STATE = "CurrentState";
	public static final String SYSTEM_TIME = "SystemTime";
	
	public StatusFile(String sensorPath)
	{
		super(sensorPath + ":" + File.separator + Constants.CM2_FEEDBACK_FILE);
	}

}
