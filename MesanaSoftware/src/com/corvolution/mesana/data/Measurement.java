package com.corvolution.mesana.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.corvolution.mesana.rest.RestApiConnector;

public class Measurement extends RestApiConnector
{

	private String id = "";
	private String state = "";
	private String stateChange = "";
	private String linkId = "";
	private String deliveryDate = "";
	private String sensorId = "";
	private String priority = "";

	public String getMeasurementData()
	{
		if (deliveryDate.equals("0000-00-00"))
			deliveryDate = "ASAP";

		return "ID: " + id + "\r\n" + "State Change: " + stateChange + "\r\n" + "Link ID: " + linkId + "\r\n"
				+ "Delivery Date: " + deliveryDate + "\r\n" + "Priority: " + priority;

	}

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

	public String getPriority()
	{

		return priority;
	}

	public String getID()
	{
		return id;
	}

	public String getLinkId()
	{
		return linkId;
	}

	public String getSensorId()
	{
		return sensorId;
	}

}
