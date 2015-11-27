package com.corvolution.cm2.configuration;

import java.util.ArrayList;

public class StartMode extends ConfigurationSet
{	
	protected ArrayList<StartMode> startModeList = new ArrayList<>();

	public StartMode(byte startModeByte, String name, String description, String compatibleConfigurationVersion)
	{
		super(startModeByte, name, description, compatibleConfigurationVersion);
		
		
	}
	
	public void addStartModeToList(StartMode modeObject){
		startModeList.add(modeObject);
	}

}
