package com.corvolution.cm2.configuration;

import java.util.Date;

public interface ConfigurationInterface
{
	public static final String VERSION_MAJOR = "1";
	public static final String VERSION_MINOR = "0";
	
	
	public void setStartMode();
	public byte getStartMode();
	public void setConfigurationSet(ConfigurationSet configurationSet);
	public ConfigurationSet getConfigurationSet();
	public void setRecordingStartTime(Date date);
	public Date getRecordingStartTime();
	public void setRecordingDuration(int duration);
	public int getRecordingDuration();
}
