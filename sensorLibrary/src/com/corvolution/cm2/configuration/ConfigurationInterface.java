package com.corvolution.cm2.configuration;

import java.util.Date;

public interface ConfigurationInterface
{
	public static final String VERSION_MAJOR = "1";
	public static final String VERSION_MINOR = "0";
	
	public void setStartMode();
	public byte getStartMode();
	public void setConfigurationSet();
	public byte getConfigurationSet();
	public void setRecordingStartTime();
	public Date getRecordingStartTime();
	public void setRecordingDuration();
	public byte getRecordingDuration();
}
