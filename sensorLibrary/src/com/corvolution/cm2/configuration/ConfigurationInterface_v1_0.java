package com.corvolution.cm2.configuration;

import java.util.Date;
import java.util.HashMap;

public interface ConfigurationInterface_v1_0
{
//	public static final String VERSION_MAJOR = "1";
//	public static final String VERSION_MINOR = "0";
	public static final String VERSION = "1.0";
	
	
	public void setStartMode(byte startModebyte, String name, String description, String compatibleVersion);
	public StartMode getStartMode();
	public void setConfigurationSet(ConfigurationSet configurationSet);
	public ConfigurationSet getConfigurationSet();
	public void setRecordingStartTime(Date date);
	public Date getRecordingStartTime();
	public void setRecordingDuration(int duration);
	public int getRecordingDuration();
}
