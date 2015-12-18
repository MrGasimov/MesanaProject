 package com.corvolution.cm2.connection;

/**Sensor Listener interface. All listeners must implement this interface for receiving event object fired by source.
 * @author Suleyman Gasimov
 *
 */
public interface SensorListener
{
	public void sensorConnection(ConnectionEvent cEvent);
	public void sensorDisconnection(DisconnectionEvent dEvent);
}
