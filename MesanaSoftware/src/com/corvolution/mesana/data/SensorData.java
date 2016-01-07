package com.corvolution.mesana.data;

import com.corvolution.mesana.rest.RestApiConnector;

/**This class represents object holding information about specific sensor.  
 * @author Suleyman Gasimov
 *
 */
public class SensorData extends RestApiConnector
{
	private String id = "";
	private String state = "";
	private String firmware = "";
	private String name = "";
	private String type = "";

	/**Returns information  of a given sensor.
	 * @return String information about sensor (e.g sensor state).
	 */
	public String getSensorData()
	{
		return "SN: " + id + "\r\n" + "State: " + state + "\r\n" + "Firmware: " + firmware + "\r\n" + "Sensor Name: "
				+ name + "\r\n" + "Type: " + type;
	}

	/**Returns id of given sensor.
	 * @return String, id of sensor
	 */
	public String getId()
	{
		return this.id;
	}

	/**Returns firmware of given sensor.
	 * @return String, firmware of sensor.
	 */
	public String getFirmware()
	{
		return this.firmware;
	}

	/**Returns state of given sensor.
	 * @return String, state of sensor.
	 */
	public String getState()
	{
		return this.state;
	}
}
