package com.corvolution.mesana.data;

import com.corvolution.mesana.rest.RestApiConnector;

/**This class represents task object. It holds information about sensor task or measurement task.
 * @author Suleyman Gasimov
 *
 */
public class Task extends RestApiConnector
{
	
	/** The message. */
	private String message = "";
	
	/** The user. */
	private String user = "";
	
	/** The created. */
	private String created = "";
	
	/** The type. */
	private String type = "";
	
	/** The measurement id. */
	private String measurementID = "";
	
	/** The sensor id. */
	private String sensorID = "";

	/**getTaskDetails() - returns task information.
	 * @return String, information about task.
	 */
	public String getTaskDetails()
	{
		return "Message: " + message + "\r\n" + "User: " + user + "\r\n"+ "Date: "+created+ "\r\n"+ "Type: "+type+"\r\n"
					+"MeasurementId: "+measurementID+"\r\n"+"sensorId: "+sensorID;
	}

}
