package com.corvolution.cm2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorConfiguration {	
	
	private static final String VERSION_NUMBER = "0001545";
	private static final String RECORDING_DURATION = "245";
	
	private static enum START_MODE{};
	private HashMap <String, HashMap> configSetMap;
	private HashMap <String, String> addParameters;
	
	
	public SensorConfiguration(){
		configSetMap = new HashMap<>();
		ConfigurationSet configSet = new ConfigurationSet();
		//default set for configFile
		configSetMap.put("1", configSet.getSet());
	}

	//set linkId to add paramater file 
	public void addParameter(String key,String value){
		this.addParameters.put(key, value);
	}
	
	//set linkId for configuration file 
	public void addEncryptedParameter(String key, String value, String password){
		//check for correct password and than initialize
		this.addParameters.put(key, value);
	}
	
	//returns map of parameters from additinal parameter file in sensor
	public HashMap<String,String> getParameters(){
		return addParameters ;
	}
	
	public HashMap<String,HashMap>  getConfigurationSet() {
		return configSetMap;
	}
	
	public void setConfigurationSet(ConfigurationSet configurationSet, String byteNumber, ArrayList rateList) {
		configurationSet = new ConfigurationSet(byteNumber,rateList);
	}
	 
	

	
	

}
