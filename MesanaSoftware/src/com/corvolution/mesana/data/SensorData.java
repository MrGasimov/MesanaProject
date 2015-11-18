package com.corvolution.mesana.data;
import com.corvolution.mesana.rest.RestApiConnector;

public class SensorData extends RestApiConnector{

	private  String id = "";	
	private  String state = "";	
	private  String firmware = "";		
	private  String name = "";	
	private  String type = "";

	public  String getSensorData(){
		
		return "SN: " + id +"\r\n" +"State: "+ state + "\r\n"+"Firmware: "+firmware +"\r\n"+"Sensor Name: "+ name +"\r\n"+"Type: "+type;
		
	}
	public String getID(){
		return id;
	}
	public String getFirmware(){
		return firmware;
	}
	
}

