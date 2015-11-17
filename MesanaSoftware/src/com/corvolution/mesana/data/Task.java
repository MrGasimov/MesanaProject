package com.corvolution.mesana.data;

import com.corvolution.mesana.rest.RestApiConnector;

public class Task extends RestApiConnector {
	
	private String message="";
	private String user = "";
	private String created = "";
	private String type = "";
	private String measurementID = "";
	private String sensorID = "";
		
	public String getTaskDetails() {
		
		return "Message: " + message + "\r\n" + "User: " + user;

	}
	
	
	
}
