package com.corvolution.cm2.connection;
import java.util.EventObject;

/**This class represents connection event object for sensor listener
 * @author Suleyman Gasimov
 *
 */
public class ConnectionEvent extends EventObject
{
	private static final long serialVersionUID = 1L;
	private boolean connectionState;
	private String path;
	private int nConnectedSensors;

	/**Connection event object created with a source, state and number of connected sensors arguments
	 * @param source object of fired event
	 * @param state of current sensor
	 * @param path to current sensor
	 * @param num of connected sensors
	 */
	public ConnectionEvent(Object source, boolean state, String path, int num)
	{
		super(source);
		this.connectionState = state;
		this.path = path;
		this.nConnectedSensors = num;

	}

	/**This method returns connection state of a sensor
	 * @return boolean
	 */
	public boolean getState()
	{
		return connectionState;
	}

	/**This method returns letter assigned to sensor by system 
	 * @return String
	 */
	public String getSensorPath()
	{
		return path;
	}

	/**This method returns number of connected sensor
	 * @return int
	 */
	public int getNumOfConnectedSensors()
	{
		return nConnectedSensors;
	}
}
