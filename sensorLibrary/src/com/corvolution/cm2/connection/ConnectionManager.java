package com.corvolution.cm2.connection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.corvolution.cm2.Sensor;
import com.corvolution.cm2.SensorNotFoundException;

/**This class manages all sensor connection, holding connected and disconnected sensor objects, sensor state, adding and removing listeners, event source for firing events. Singleton pattern applied for this class. Instance of this class cannot be created. Instead specific method returns reference to this class  
 * @author Suleyman Gasimov
 *
 */
public final class ConnectionManager
{
	private  Vector<SensorListener> connectionListeners;
	private  Vector<SensorListener> disconnectionListeners;
	public  boolean connectionState;
	private  String sensorPath;
	private ConnectionEvent connectionEvent;
	private DisconnectionEvent disconnectionEvent;
	public  int nConnectedSensors;
	public static final int CONNECTION = 100;
	public static final int DISCONNECTION = 101;
	public static final int SINGLE_SENSOR = 102;
	public static final int ALL_SENSORS = 103;
	private  List<Sensor> sensorList ;
	private static ConnectionManager instance = null;

	/**Constructor for defending single instance.
	 * 
	 */
	private ConnectionManager()
	{
		sensorList = new CopyOnWriteArrayList<>();
	}

	/**This method returns single and only instance of this class. If instance is null, this method creates instance.
	 * @return ConnectionManager instance
	 */
	public static ConnectionManager getInstance()
	{
		if (instance == null)
		{
			instance = new ConnectionManager();
		}
		return instance;
	}

	/**This method adds listeners to list.Only listeners implemented SensorListener interface is allowed.Second argument option is for defining which kind listener is added by application.
	 * @param listener, this listener must implement SensorListener interface
	 * @param int - two options, CONNECTION, DISCONNECTION
	 */
	synchronized public void addSensorListener(SensorListener listener, int option)
	{	
		if(option == ConnectionManager.CONNECTION)
		{
			if (connectionListeners == null)
			{
				connectionListeners = new Vector<SensorListener>();
				
			}
			connectionListeners.addElement(listener);
		}else if(option == ConnectionManager.DISCONNECTION) 
		{
			if (disconnectionListeners == null)
			{
				disconnectionListeners = new Vector<SensorListener>();
				
			}
			disconnectionListeners.addElement(listener);
		}
		
		
	}

	/**Same as addSensorListener, except instead of adding it removes listeners from list
	 * @param listener
	 * @param option
	 */
	synchronized public void removeSensorListener(SensorListener listener, int option)
	{	
		if(option == ConnectionManager.CONNECTION)
		{
			if (connectionListeners == null)
			{
				connectionListeners = new Vector<SensorListener>();
				
			}
			connectionListeners.removeElement(listener);
		}else if(option == ConnectionManager.DISCONNECTION) 
		{
			if (disconnectionListeners == null)
			{
				disconnectionListeners = new Vector<SensorListener>();
				
			}
			disconnectionListeners.removeElement(listener);
		}
	}
		

	private synchronized void fireConnectionEvent()
	{
		connectionEvent = new ConnectionEvent(this, connectionState, sensorPath, nConnectedSensors);
		if (connectionListeners != null)
		{
			Iterator<SensorListener> listeners = connectionListeners.iterator();
			while (listeners.hasNext())
			{
				listeners.next().sensorConnection(connectionEvent);

			}
		}

	}
	
	private synchronized void fireDisconnectionEvent()
	{
		disconnectionEvent = new DisconnectionEvent(this, connectionState, sensorPath, nConnectedSensors);
		if (disconnectionListeners != null)
		{
			Iterator<SensorListener> listeners = disconnectionListeners.iterator();
			while (listeners.hasNext())
			{
				listeners.next().sensorDisconnection(disconnectionEvent);

			}
		}

	}


	protected void setConnected(boolean status)
	{
		connectionState = status;
	}

	/**This method returns true if sensor is connected
	 * @return boolean
	 */
	public boolean isConnected()
	{
		return connectionState;
	}

	protected void setNumberOfConnectedSensors(int count)
	{
		nConnectedSensors = count;
	}

	/**This method returns number of connected sensors
	 * @return int
	 */
	public int getNumberOfConnectedSensors()
	{
		return nConnectedSensors;
	}

	protected void addSensorToList(String path) 
	{
		this.sensorPath = path;
		Sensor sensor = null;
		try
		{
			sensor = new Sensor(path);
		}
		catch (SensorNotFoundException e)
		{
			e.getStackTrace();
		}
		sensorList.add(sensor);
		fireConnectionEvent();
	}

	protected void removeSensorFromList(String path)
	{
		for (Sensor device : sensorList)
		{
			if (device.getSensorPath().equals(path))
				sensorList.remove(device);
		}
		fireDisconnectionEvent();
	}

	/**This method starts specific thread. That thread listens connection state of sensors. If any sensor connected it fires event with specific information about connection. Before sensor  configuration  application this method must be called.
	 * 
	 */
	public void startListener()
	{
		Thread sensorListener = new Thread(new SensorNotifier());
		sensorListener.start();
	}

	/**This method returns list of connected sensors. This list holds only sensor objects which states are true.
	 * @return List<sensor>
	 */
	public List<Sensor> getConnectedSensorsList()
	{
		return sensorList;
	}

	/**This method returns single sensor object from list. if three sensors are connected, for retrieving first one must pass 0 (int) as argument, for second 1 (int) as argument,  for third one 2 (int) as argument.
	 * @param int 
	 * @return sensor
	 * @throws SensorNotFoundException
	 */
	public Sensor currentSensor(int i) throws SensorNotFoundException
	{
		if(sensorList.isEmpty())
		{
			throw new SensorNotFoundException("Sensor not found!");
		}
		return sensorList.get(i);
	}

	/**This method returns single sensor's or all sensor's measurement data size
	 * @param int, option as static field of this class- SINGLE_SENSOR  or ALL_SENSORS
	 * @return long 
	 */
	public long measurementDataSize(int option)
	{
		long dataSize = 0;
		if (option == ConnectionManager.SINGLE_SENSOR)
		{
			for (Sensor device : sensorList)
			{
				dataSize += device.getDataSize();
			}
		}
		else if (option== ConnectionManager.ALL_SENSORS)
		{
			dataSize += sensorList.get(0).getDataSize();
		}

		return dataSize;
	}

}