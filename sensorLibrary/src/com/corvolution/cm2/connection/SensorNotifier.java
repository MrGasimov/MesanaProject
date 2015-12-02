package com.corvolution.cm2.connection;

import java.io.File;
import javax.swing.filechooser.FileSystemView;
import com.corvolution.cm2.Constants;
import com.corvolution.cm2.Logger;

public class SensorNotifier implements Runnable
{
	public void run()
	{
		sensorListener();
	}

	public void sensorListener()
	{
		int nConnectedSensors = 0;
		String[] letters = new String[] {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "O", "P", "Q",
				"R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
		File[] sensors = new File[letters.length];
		boolean[] isDrive = new boolean[letters.length];

		// init the file objects and the initial drive state
		for (int i = 0; i < letters.length; ++i)
		{
			sensors[i] = new File(letters[i] + ":/");

			// isDrive[i] = sensors[i].canRead();
			isDrive[i] = false;
		}
		Logger.getInstance().printLog("Find Sensor: waiting for devices...");
		
		// loop indefinitely
		while (true)
		{
			// check each drive
			for (int i = 0; i < letters.length; ++i)
			{
				boolean pluggedIn = sensors[i].canRead();
				// if the state has changed output a message
				if (pluggedIn != isDrive[i])
				{
					if (pluggedIn)
					{
						FileSystemView view = FileSystemView.getFileSystemView();
						File dir = new File(letters[i] + ":/");
						String name = view.getSystemDisplayName(dir);// .substring(0, 5);
						if (name.equals(Constants.SENSOR_NAME+ " (" + letters[i] + ":)")) 
						{
							nConnectedSensors++;
							ConnectionManager.getInstance().setConnected(true);
							ConnectionManager.getInstance().setNumberOfConnectedSensors(nConnectedSensors);
							ConnectionManager.getInstance().addSensorToList(letters[i]);
						}
					}
					else
					{
						if (nConnectedSensors != 0)
						{
							nConnectedSensors--;
							ConnectionManager.getInstance().setConnected(false);
							ConnectionManager.getInstance().setNumberOfConnectedSensors(nConnectedSensors);
							ConnectionManager.getInstance().removeSensorFromList(letters[i]);
						}

					}
					isDrive[i] = pluggedIn;
				}
			}
			// wait before looping
			try
			{
				Thread.sleep(Constants.SLEEP_TIME);
			}
			catch (InterruptedException e)
			{
			}

		}
	}

}
