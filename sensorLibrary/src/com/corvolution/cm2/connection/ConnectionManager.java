package com.corvolution.cm2.connection;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import com.corvolution.cm2.Sensor;

public final class ConnectionManager
{
	private static Vector<SensorListener> sensorListeners;
	public static boolean connectionState;
	private static String sensorPath;
	private SensorEvent sensorEvent;
	public static int nConnectedSensors;
	private static List<Sensor> sensorList = new CopyOnWriteArrayList<>();
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
	synchronized public void addSensorListener(SensorListener listener)
	{
		if (sensorListeners == null)
		{
			sensorListeners = new Vector<SensorListener>();
		}
		sensorListeners.addElement(listener);
	}

	/** Remove a listener for Events */
	synchronized public void removeSensorListener(SensorListener listener)
	{
		if (sensorListeners == null)
			sensorListeners = new Vector<>();
		sensorListeners.removeElement(listener);
	}

	private synchronized void fireSensorEvent()
	{
		sensorEvent = new SensorEvent(this, connectionState, sensorPath, nConnectedSensors);
		if (sensorListeners != null)
		{
			Iterator<SensorListener> listeners = sensorListeners.iterator();
			while (listeners.hasNext())
			{
				listeners.next().sensorConnection(sensorEvent);

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
	protected void addSensorToList(String path) throws IOException
	{
		sensorPath = path;
		Sensor sensor = new Sensor(path);
		sensorList.add(sensor);
		fireSensorEvent();

	}

	// deletes disconnected sensor from array
	protected void removeSensorFromList(String path)
	{
		for (Sensor device : sensorList)
		{
			if (device.getSensorPath().equals(path))
				sensorList.remove(device);
		}
		fireSensorEvent();
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