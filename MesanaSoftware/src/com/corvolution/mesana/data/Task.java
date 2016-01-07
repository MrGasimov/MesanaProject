package com.corvolution.mesana.data;

import com.corvolution.mesana.rest.RestApiConnector;

/**This class represents task object. It holds information about sensor task or measurement task.
 * @author Suleyman Gasimov
 *
 */
public class Task extends RestApiConnector
{
	private String message = "";
	private String user = "";
	private String created = "";
	private String type = "";
	private String measurementID = "";
	private String sensorID = "";

	/**Returns task information.
	 * @return String, information about task.
	 */
	public String getTaskDetails()
	{
		return "Message: " + message + "\r\n" + "User: " + user + "\r\n"+ "Date: "+created+ "\r\n"+ "Type: "+type+"\r\n"
					+"MeasurementId: "+measurementID+"\r\n"+"sensorId: "+sensorID;
	}

}
