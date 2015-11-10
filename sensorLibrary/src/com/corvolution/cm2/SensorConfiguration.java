package com.corvolution.cm2;

import java.util.HashMap;

public class SensorConfiguration {
	private String ECG_SAMPLE_RATE;
	private String ACC_SAMPLE_RATE;
	private String TEMP_SAMPLE_RATE;
	private String IMP_SAMPLE_RATE;
	private String sampleRate;
	private String linkId ="";	
	private String patientData ="";
	private HashMap <String, String> hmap = new HashMap<>();
	//return sample rate 
	public String getSampleRate() {
		return sampleRate;
	}
	
	//set sample rate for configuration file 
	public void setSampleRate(String rate) {
		this.sampleRate = rate;
		
	}
	
	//set linkId for configuration file 
	public void addParameter(String key, String value){
		this.hmap.put(key, value);
	}
	
	//set linkId for configuration file 
	public void addEncryptedParameter(String key, String value, String password){
		this.hmap.put(key, value);
	}
	
	public HashMap<String, String > getParameters(){
		return hmap;
	}
	
	//return linkId 
	public String getLinkId(){
		return linkId;
	}
		
	public void setPatientData(String patientData){
		this.patientData = patientData;
	}

	/**
	 * @uml.property  name="versionNumber"
	 */
	private String versionNumber;

	/**
	 * Getter of the property <tt>versionNumber</tt>
	 * @return  Returns the versionNumber.
	 * @uml.property  name="versionNumber"
	 */
	public String getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Setter of the property <tt>versionNumber</tt>
	 * @param versionNumber  The versionNumber to set.
	 * @uml.property  name="versionNumber"
	 */
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	/**
	 * @uml.property  name="startMode"
	 */
	private Enum startMode;

	/**
	 * Getter of the property <tt>startMode</tt>
	 * @return  Returns the startMode.
	 * @uml.property  name="startMode"
	 */
	public Enum getStartMode() {
		return startMode;
	}

	/**
	 * Setter of the property <tt>startMode</tt>
	 * @param startMode  The startMode to set.
	 * @uml.property  name="startMode"
	 */
	public void setStartMode(Enum startMode) {
		this.startMode = startMode;
	}

	/**
	 * @uml.property  name="configurationSet"
	 */
	private ConfigurationSet configurationSet;

	/**
	 * Getter of the property <tt>configurationSet</tt>
	 * @return  Returns the configurationSet.
	 * @uml.property  name="configurationSet"
	 */
	public ConfigurationSet getConfigurationSet() {
		return configurationSet;
	}

	/**
	 * Setter of the property <tt>configurationSet</tt>
	 * @param configurationSet  The configurationSet to set.
	 * @uml.property  name="configurationSet"
	 */
	public void setConfigurationSet(ConfigurationSet configurationSet) {
		this.configurationSet = configurationSet;
	}

}
