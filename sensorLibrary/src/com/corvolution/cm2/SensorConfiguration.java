package com.corvolution.cm2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorConfiguration {	
	
	public static final byte VERSION_MAJOR = 1;
	public static final byte VERSION_MINOR = 1;
	public static final byte[] startMode = {1,2,3};
	public byte[] configSet = {1,2,3};	
	public byte[] recordDuration = {2,3,4,5,6,7};
	public byte[] startTime = {1,3,1,1,1,5};
	public byte latency = 0;
	public byte[] checksum ={2,2,2,2}; 		
    private HashMap <String, HashMap> configSetMap;
	private HashMap <String, String> addParameters;
	
	
	public SensorConfiguration(){
		configSetMap = new HashMap<>();
		addParameters = new HashMap<>();
		//constructing default configset which is number 1--> configset[0]
		ConfigurationSet configSet = new ConfigurationSet();
		//default set for configFile
		configSetMap.put("1", configSet.getSet());
	}

	//set linkId to add paramater file 
	public void addParameter(String key,String value){
		this.addParameters.put(key, value);
	}
	
	public String getParameter(String key){
		return addParameters.get(key);
	}
	//set linkId for configuration file 
	public void addEncryptedParameter(String key, String value, String password){
		//check for correct password and than initialize
		this.addParameters.put(key, value);
	}
	
	//returns map of parameters from additional parameter file in sensor
	public HashMap<String,String> getParametersMap(){
		return addParameters ;
	}
	
	public HashMap<String,HashMap>  getConfigurationSet() {
		return configSetMap;
	}
	
	public void setConfigurationSet(ConfigurationSet configurationSet, String byteNumber, ArrayList rateList) {
		configurationSet = new ConfigurationSet(byteNumber,rateList);
	}
	 
	

	
	

}
