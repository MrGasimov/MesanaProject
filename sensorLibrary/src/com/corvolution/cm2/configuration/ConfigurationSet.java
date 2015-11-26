package com.corvolution.cm2.configuration;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfigurationSet
{
	ArrayList<String> sampleRate = new ArrayList<>();
	HashMap<String, ArrayList> description = new HashMap<>();

	// constructor for default configurationSet which is number 1
	public ConfigurationSet()
	{
		
	}

	// second constructor for setting another configurationSet
	public ConfigurationSet(String name, ArrayList<String> sampleRateList)
	{
		this.sampleRate = sampleRateList;
		this.description.put(name, sampleRateList);
	}

	public HashMap<String, ArrayList> getSet()
	{
		return description;
	}
}
