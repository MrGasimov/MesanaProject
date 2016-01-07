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
	private String id = "";
	private String stateChange = "";
	private String linkId = "";
	private String deliveryDate = "";
	private String sensorId = "";
	private String priority = "";

	/**
	 * This method returns information about object.
	 * @return String
	 */
	public String getMeasurementData()
	{
		if (deliveryDate.equals("0000-00-00"))
			deliveryDate = "ASAP";

		return "ID: " + id + "\r\n" + "State Change: " + stateChange + "\r\n" + "Link ID: " + linkId + "\r\n"
				+ "Delivery Date: " + deliveryDate + "\r\n" + "Priority: " + priority;

	}
	
	
	/**Returns delivery date of the sensor after configuration as Date type.
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

	/**Returns priority of measurement service.This priority states how soon sensor must be configurated and sent to customer.
	 * @return String
	 */
	public String getPriority()
	{

		return priority;
	}

	/**
	 * Returns id of the specific measurement service.
	 *
	 * @return String
	 */
	public String getId()
	{
		return id;
	}

	/**Returns linkId of the measurement service.This id is a link between measurement service and sensor.
	 * @return String
	 */
	public String getLinkId()
	{
		return linkId;
	}

	/**Returns id of the sensor.
	 * @return String
	 */
	public String getSensorId()
	{
		return sensorId;
	}

}
