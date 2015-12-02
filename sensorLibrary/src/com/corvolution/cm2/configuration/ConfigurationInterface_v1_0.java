package com.corvolution.cm2.configuration;

import java.util.Date;
import java.util.HashMap;

public interface ConfigurationInterface_v1_0
{
	public static final String VERSION = "1.0";
	public void setStartMode(StartMode startMode);
	public StartMode getStartMode();
	public void setConfigurationSet(ConfigurationSet configurationSet);
	public ConfigurationSet getConfigurationSet();
	public void setRecordingStartTime(Date date);
	public Date getRecordingStartTime();
	public void setRecordingDuration(int duration);
	public int getRecordingDuration();
}
