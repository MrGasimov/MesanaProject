package com.corvolution.mesana.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.corvolution.mesana.rest.RestApiConnector;

/**This class represents Measurement service object.This object must not be mistaken with a sensor data after measuring completed.
 * When customer orders service it is meant to send configurated sensor for measuring.Before configuration one must have specific information and this class is used for that purpose. 
 * @author Suleyman Gasimov
 */
public class Measurement extends RestApiConnector
{

	/** The id. */
	private String id = "";
	
	/** The state. */
	private String state = "";
	
	/** The state change. */
	private String stateChange = "";
	
	/** The link id. */
	private String linkId = "";
	
	/** The delivery date. */
	private String deliveryDate = "";
	
	/** The sensor id. */
	private String sensorId = "";
	
	/** The priority. */
	private String priority = "";

	/**
	 * getMeasurementData - This method returns information about object.
	 *
	 * @return String
	 * @para none
	 */
	public String getMeasurementData()
	{
		if (deliveryDate.equals("0000-00-00"))
			deliveryDate = "ASAP";

		return "ID: " + id + "\r\n" + "State Change: " + stateChange + "\r\n" + "Link ID: " + linkId + "\r\n"
				+ "Delivery Date: " + deliveryDate + "\r\n" + "Priority: " + priority;

	}
	
	
	/**getDate - returns delivery date of the sensor after configuration as Date type.
	 * @return Date - date of sensor delivery.
	 */
	public Date getDate()
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try
		{
			date = df.parse(deliveryDate);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}

		return date;
	}

	/**getPriority - returns priority of measurement service.This priority states how soon sensor must be configurated and sent to customer.
	 * @return String
	 */
	public String getPriority()
	{

		return priority;
	}

	/**
	 * getId - returns id of the specific measurement service.
	 *
	 * @return String
	 */
	public String getId()
	{
		return id;
	}

	/**getLinkId - returns linkId of the measurement service.This id is a link between measurement service and sensor.
	 * @return String
	 */
	public String getLinkId()
	{
		return linkId;
	}

	/**getSensorId - returns id of the sensor.
	 * @return String
	 */
	public String getSensorId()
	{
		return sensorId;
	}

}
