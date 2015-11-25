package com.corvolution.cm2;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ConnectionManager
{
	private static Vector<SensorListener> sensorListeners;
	public static boolean state;
	private static String sensorPath;
	private SensorEvent sensorEvent;
	public static int numberOfConnectedSensors;
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

	/** Register a listener for SunEvents */
	synchronized public void addSensorListener(SensorListener listener)
	{
		if (sensorListeners == null)
			sensorListeners = new Vector<SensorListener>();
		sensorListeners.addElement(listener);
	}

	/** Remove a listener for SunEvents */
	synchronized public void removeSensorListener(SensorListener listener)
	{
		if (sensorListeners == null)
			sensorListeners = new Vector<>();
		sensorListeners.removeElement(listener);
	}

	private synchronized void fireSensorEvent()
	{
		sensorEvent = new SensorEvent(this, state, sensorPath, numberOfConnectedSensors);
		if (sensorListeners != null)
		{
			Iterator<SensorListener> listeners = sensorListeners.iterator();
			while (listeners.hasNext())
			{
				listeners.next().sensorConnection(sensorEvent);

			}
		}

	}

	public static void setState(boolean status)
	{
		state = status;
	}

	public static boolean getState()
	{
		return state;
	}

	public static void setCounter(int count)
	{
		numberOfConnectedSensors = count;
	}

	public static int getCounter()
	{
		return numberOfConnectedSensors;
	}

	// adds connected sensor path to array
	public void addSensorToList(String path) throws IOException
	{
		sensorPath = path;
		Sensor sensor = new Sensor(path);
		fireSensorEvent();
		sensorList.add(sensor);

	}

	// deletes disconnected sensor from array
	public void removeSensorFromList(String path)
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
		Thread sensorListener = new Thread(new UsbListener());
		sensorListener.start();
	}

	// returns list of connected sensors
	public static List<Sensor> getConnectedSensorsList()
	{
		return sensorList;
	}

	public static Sensor currentSensor(int i)
	{

		return sensorList.get(i);
	}

	// sum of all connected sensors mearuementData
	public static long measurementDataSize(String option)
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