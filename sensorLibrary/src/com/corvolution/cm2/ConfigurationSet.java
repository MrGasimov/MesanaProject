package com.corvolution.cm2;

import java.util.ArrayList;
import java.util.HashMap;



public class ConfigurationSet {
	ArrayList<String> sampleRate = new ArrayList<>();
	HashMap <String, ArrayList> description = new HashMap<>();

	// constructor for default configurationSet which is number 1 
	public ConfigurationSet(){
		sampleRate.add("ACG:250Hz");
		sampleRate.add("ACC:50Hz");
		sampleRate.add("TEMP:120Hz");
		sampleRate.add("IMP:50Hz");
		description.put("Mesana",sampleRate);	
	}
	
	//second constructor for setting another configurationSet
	public ConfigurationSet(String name, ArrayList<String> sampleRateList){
		this.sampleRate = sampleRateList;
		this.description.put(name, sampleRateList);
	}
	
	public HashMap<String, ArrayList> getSet(){
		return description;
	}
}