package com.corvolution.cm2.connection;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.corvolution.cm2.Sensor;

public final class ConnectionManager
{
	public static final int CONNECTION = 100;
	public static final int DISCONNECTION = 101;
	private  Vector<SensorListener> connectionListeners;
	private  Vector<SensorListener> disconnectionListeners;
	public  boolean connectionState;
	private  String sensorPath;
	private ConnectionEvent connectionEvent;
	private DisconnectionEvent disconnectionEvent;
	public  int nConnectedSensors;
	private  List<Sensor> sensorList = new CopyOnWriteArrayList<>();
	private static ConnectionManager instance = null;

	private ConnectionManager()
	{
		// only for defending single instance
	}

	public static ConnectionManager getInstance()
	{
		if (instance == null)
		{
			instance = new ConnectionManager();
		}
		return instance;
	}

	/** Register a listener for Events */
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

	/** Remove a listener for Events */
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

	public boolean isConnected()
	{
		return connectionState;
	}

	protected void setNumberOfConnectedSensors(int count)
	{
		nConnectedSensors = count;
	}

	public int getNumberOfConnectedSensors()
	{
		return nConnectedSensors;
	}

	// adds connected sensor path to array
	protected void addSensorToList(String path) 
	{
		this.sensorPath = path;
		Sensor sensor;
		try
		{
			sensor = new Sensor(path);
			sensorList.add(sensor);
			fireConnectionEvent();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		

	}

	// deletes disconnected sensor from array
	protected void removeSensorFromList(String path)
	{
		for (Sensor device : sensorList)
		{
			if (device.getSensorPath().equals(path))
				sensorList.remove(device);
		}
		fireDisconnectionEvent();
	}

	// starts thread for listening and notifies Manager about any connection
	public void startListener()
	{
		Thread sensorListener = new Thread(new SensorNotifier());
		sensorListener.start();
	}

	// returns list of connected sensors
	public List<Sensor> getConnectedSensorsList()
	{
		return sensorList;
	}

	public Sensor currentSensor(int i)
	{

		return sensorList.get(i);
	}

	// sum of all connected sensors mearuementData
	public long measurementDataSize(String option)
	{
		long dataSize = 0;
		if (option.equalsIgnoreCase("all"))
		{
			for (Sensor device : sensorList)
			{
				dataSize += device.getDataSize();
			}
		}
		else if (option.equalsIgnoreCase("single"))
		{
			dataSize += sensorList.get(0).getDataSize();
		}

		return dataSize;
	}

}