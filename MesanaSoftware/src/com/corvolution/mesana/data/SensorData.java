package com.corvolution.mesana.data;

import com.corvolution.mesana.rest.RestApiConnector;

/**SensorData - This class represents object holding information about specific sensor.  
 * @author Suleyman Gasimov
 *
 */
public class SensorData extends RestApiConnector
{
	/** Part of the sensor properties. */
	private String id = "";
	
	/** Part of the sensor properties. */
	private String state = "";
	
	/** Part of the sensor properties. */
	private String firmware = "";
	
	/** Part of the sensor properties. */
	private String name = "";
	
	/** Part of the sensor properties. */
	private String type = "";


	/**getSensorData() - returns information  of a given sensor.
	 * @return String information about sensor (e.g sensor state).
	 */
	public String getSensorData()
	{
		return "SN: " + id + "\r\n" + "State: " + state + "\r\n" + "Firmware: " + firmware + "\r\n" + "Sensor Name: "
				+ name + "\r\n" + "Type: " + type;
	}

	/**getId() - returns id of given sensor.
	 * @return String, id of sensor
	 */
	public String getId()
	{
		return this.id;
	}

	/**getFirmware() - returns firmware of given sensor.
	 * @return String, firmware of sensor.
	 */
	public String getFirmware()
	{
		return this.firmware;
	}

	/**getState() - returns state of given sensor.
	 * @return String, state of sensor.
	 */
	public String getState()
	{
		return this.state;
	}
}
