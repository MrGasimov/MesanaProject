package com.corvolution.cm2.configuration;

import java.util.ArrayList;

public class ConfigurationSets
{
	private ArrayList <ConfigurationSet> configSetList = new ArrayList<>();
	
	public ConfigurationSets(){
		configSetList.add(new ConfigurationSet((byte)1, "mesana", "default mesana configuration set", "1.0"));
	}
	
	public ArrayList<ConfigurationSet> getConfigSetList(){
		return configSetList;
	}
}
